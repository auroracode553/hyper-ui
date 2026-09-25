// 文件职责：启动 VitePress 并串行发布 Wasm 预览；源码变化后重新发布。
// WasmPreview 读取 Gradle 发布任务写入的 preview-ready.json 并自动加载新版本。
// 手动运行：在 vitepress/ 目录执行 `npm run dev:watch`，按 Ctrl+C 停止全部进程。
import { spawn, spawnSync } from 'node:child_process';
import { existsSync, watch } from 'node:fs';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const scriptDirectory = dirname(fileURLToPath(import.meta.url));
const repositoryRoot = resolve(scriptDirectory, '..');
const libraryDirectory = resolve(repositoryRoot, 'library');
const previewDirectory = resolve(repositoryRoot, 'preview');
const vitepressDirectory = resolve(repositoryRoot, 'vitepress');
const vitepressPort = process.env.HYPER_UI_VITE_PORT || '5173';
const debounceMs = Number(process.env.HYPER_UI_WATCH_DEBOUNCE || 2000);
const generatedDirectories = new Set(['build', '.gradle', '.kotlin', 'kotlin-js-store', 'node_modules', 'dist']);

for (const directory of [libraryDirectory, previewDirectory, vitepressDirectory]) {
  if (!existsSync(directory)) throw new Error(`缺少目录：${directory}`);
}

// 当前正在执行的 Gradle 子进程（首次构建与增量重建共用），退出时统一清理，避免残留僵尸进程。
let activeGradleChild = null;

function stopProcessTree(child) {
  if (!child?.pid || child.exitCode !== null) return;
  if (process.platform === 'win32') {
    spawnSync('taskkill', ['/pid', String(child.pid), '/t', '/f'], {
      stdio: 'ignore',
      windowsHide: true,
    });
  } else {
    child.kill('SIGTERM');
  }
}

function runGradleTask(taskName) {
  console.log(`执行 ${taskName} …`);
  const startedAt = Date.now();
  // Gradle 下载依赖或编译 Wasm 时可能长时间无输出，显示进程仍在运行。
  const heartbeat = setInterval(() => {
    const seconds = Math.round((Date.now() - startedAt) / 1000);
    console.log(`  …${taskName} 仍在运行，已耗时 ${seconds}s`);
  }, 15000);

  return new Promise((resolvePromise) => {
    // Windows 通过 cmd 执行固定的 .bat 命令，避免 shell:true 参数拼接警告。
    const command = process.platform === 'win32' ? (process.env.ComSpec || 'cmd.exe') : './gradlew';
    const args = process.platform === 'win32'
      ? ['/d', '/s', '/c', `.\\gradlew.bat ${taskName}`]
      : [taskName];
    const child = spawn(command, args, {
      cwd: previewDirectory,
      stdio: 'inherit',
      windowsHide: true,
    });
    activeGradleChild = child;
    child.on('close', (code) => {
      clearInterval(heartbeat);
      if (activeGradleChild === child) activeGradleChild = null;
      if (code === 0) {
        resolvePromise(true);
      } else {
        console.error(`${taskName} 失败（退出码 ${code ?? '未知'}）。文档仍可访问；检查 Gradle 日志后再重试。`);
        resolvePromise(false);
      }
    });
    child.on('error', (error) => {
      clearInterval(heartbeat);
      console.error(`${taskName} 无法启动：${error.message}`);
      resolvePromise(false);
    });
  });
}

async function runPublish() {
  // CI 也先更新 Kotlin/Wasm 的 npm 锁文件，再发布预览。
  if (!(await runGradleTask('kotlinWasmUpgradePackageLock'))) return false;
  if (!(await runGradleTask('publishWasmToVitePress'))) return false;
  console.log('Wasm 预览产物已更新，浏览器中的预览将自动重载。');
  return true;
}

// 先启动 VitePress（文档站立即可访问），再在后台执行首次 Wasm 构建。
// WasmPreview 检测就绪标记，产物就绪后会自动加载，无需阻塞文档站。
console.log(`启动 VitePress（端口 ${vitepressPort}）…`);
const vitepress = spawn(process.execPath, [
  resolve(vitepressDirectory, 'node_modules/vitepress/bin/vitepress.js'), 'dev', '--port', vitepressPort,
], {
  cwd: vitepressDirectory,
  stdio: 'inherit',
  windowsHide: true,
  // 此命令负责静态产物发布，避免继承另一个终端的开发服务器地址。
  env: { ...process.env, VITE_HYPER_UI_PREVIEW_DEV_URL: '' },
});

// 构建期间的文件变化先记账，构建结束后补跑一次，避免并发调用 Gradle。
let building = false;
let pendingRebuild = false;
let debounceTimer;

async function scheduleRebuild(reason) {
  clearTimeout(debounceTimer);
  debounceTimer = setTimeout(async () => {
    if (building) {
      pendingRebuild = true;
      return;
    }
    building = true;
    console.log(`检测到 Kotlin 源码变化：${reason}`);
    await runPublish();
    building = false;
    if (pendingRebuild) {
      pendingRebuild = false;
      scheduleRebuild('构建期间又有文件变化');
    }
  }, debounceMs);
}

function isWatchable(file) {
  if (!/\.(kt|kts)$/i.test(file)) return false;
  const segments = file.split(/[\\/]/);
  return !segments.some((segment) => generatedDirectories.has(segment));
}

const watchers = [libraryDirectory, previewDirectory].map((directory) =>
  watch(directory, { recursive: true }, (_event, filename) => {
    if (!filename || !isWatchable(filename)) return;
    scheduleRebuild(filename);
  }),
);

let shuttingDown = false;
function shutdown(exitCode = 0) {
  if (shuttingDown) return;
  shuttingDown = true;
  clearTimeout(debounceTimer);
  for (const watcher of watchers) watcher.close();
  stopProcessTree(activeGradleChild);
  stopProcessTree(vitepress);
  process.exit(exitCode);
}

vitepress.on('error', (error) => {
  console.error(`VitePress 启动失败：${error.message}`);
  shutdown(1);
});
vitepress.on('exit', (code) => {
  if (!shuttingDown) shutdown(code ?? 1);
});
process.on('SIGINT', () => shutdown(0));
process.on('SIGTERM', () => shutdown(0));

console.log(`开发文档：http://localhost:${vitepressPort}`);
console.log('正在后台执行首次 Wasm 预览构建；文档站可先访问，产物就绪后交互预览自动加载。');
building = true;
runPublish().then((ok) => {
  building = false;
  if (!ok) {
    console.log('首次 Wasm 构建未完成：文档站仍可访问，Wasm 交互预览暂不可用；修正后保存 Kotlin 文件会自动重试。');
    if (pendingRebuild) {
      pendingRebuild = false;
      scheduleRebuild('首次构建期间的文件变化');
    }
    return;
  }
  console.log('首次构建完成，交互预览已就绪。保存 library/ 或 preview/ 下的 Kotlin 文件会自动重建；按 Ctrl+C 同时停止全部进程。');
  if (pendingRebuild) {
    pendingRebuild = false;
    scheduleRebuild('首次构建期间的文件变化');
  }
});
