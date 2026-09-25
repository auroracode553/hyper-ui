// 文件职责：开发期一键启动 VitePress 文档站，并监听 Kotlin 源码变化自动重新发布 Wasm 预览产物。
// 保存 library/ 或 preview/ 下的 .kt/.kts 文件后，自动执行 publishWasmToVitePress，
// 浏览器中的 <WasmPreview> iframe 检测到版本标记变化后会自行重载，无需手动刷新。
// 手动运行：在 vitepress/ 目录执行 `npm run dev:watch`，按 Ctrl+C 停止全部进程。
import { spawn, spawnSync } from 'node:child_process';
import { existsSync, writeFileSync, watch } from 'node:fs';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const scriptDirectory = dirname(fileURLToPath(import.meta.url));
const repositoryRoot = resolve(scriptDirectory, '..');
const libraryDirectory = resolve(repositoryRoot, 'library');
const previewDirectory = resolve(repositoryRoot, 'preview');
const vitepressDirectory = resolve(repositoryRoot, 'vitepress');
const vitepressPort = process.env.HYPER_UI_VITE_PORT || '5173';
const debounceMs = Number(process.env.HYPER_UI_WATCH_DEBOUNCE || 2000);

// WasmPreview.vue 在开发期轮询该文件，内容变化即重载预览 iframe。
const versionFile = resolve(vitepressDirectory, 'public', 'wasm-preview', '.build-version');

const gradleCommand = process.platform === 'win32' ? 'gradlew.bat' : './gradlew';

for (const directory of [libraryDirectory, previewDirectory, vitepressDirectory]) {
  if (!existsSync(directory)) throw new Error(`缺少目录：${directory}`);
}

// 当前正在执行的 Gradle 子进程（首次构建与增量重建共用），退出时统一清理，避免残留僵尸进程。
let activeGradleChild = null;

function start(command, args, options) {
  return spawn([command, ...args].join(' '), {
    shell: true,
    windowsHide: true,
    ...options,
  });
}

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

function publishVersion() {
  writeFileSync(versionFile, `${Date.now()}\n`);
}

function runPublish() {
  console.log('执行 publishWasmToVitePress …');
  const startedAt = Date.now();
  // 心跳：Gradle 在下载依赖/编译 Wasm 时长时间无输出，定时打印耗时以确认进程存活。
  const heartbeat = setInterval(() => {
    const seconds = Math.round((Date.now() - startedAt) / 1000);
    console.log(`  …仍在构建中，已耗时 ${seconds}s（首次构建需下载 Wasm 依赖，耗时 5-10 分钟属正常；后续增量构建通常 1 分钟内）`);
  }, 15000);

  return new Promise((resolvePromise) => {
    // 仓库 gradle.properties 为 CI 确定性关闭了 daemon；开发期用命令行参数单独开启，
    // 避免每次源码变化重建都重新 fork JVM、重复支付配置预热开销。
    const child = spawn(gradleCommand, ['--daemon', 'publishWasmToVitePress'], {
      cwd: previewDirectory,
      stdio: 'inherit',
      shell: process.platform === 'win32',
    });
    activeGradleChild = child;
    child.on('close', (code) => {
      clearInterval(heartbeat);
      if (activeGradleChild === child) activeGradleChild = null;
      if (code === 0) {
        publishVersion();
        console.log(`Wasm 预览产物已更新（耗时 ${Math.round((Date.now() - startedAt) / 1000)}s），浏览器中的预览将自动重载。`);
        resolvePromise(true);
      } else {
        console.error(`publishWasmToVitePress 失败（退出码 ${code ?? '未知'}）。若为依赖下载中断，重新运行 npm run dev:watch 可续传；修正后保存 Kotlin 文件即可重新触发。`);
        resolvePromise(false);
      }
    });
    child.on('error', (error) => {
      clearInterval(heartbeat);
      console.error(`publishWasmToVitePress 无法启动：${error.message}`);
      resolvePromise(false);
    });
  });
}

// 先启动 VitePress（文档站立即可访问），再在后台执行首次 Wasm 构建。
// WasmPreview iframe 每 2 秒轮询版本标记，产物就绪后会自动重载，无需阻塞等待。
console.log(`启动 VitePress（端口 ${vitepressPort}）…`);
const vitepress = start('node', [
  'node_modules/vitepress/bin/vitepress.js', 'dev', '--port', vitepressPort,
], {
  cwd: vitepressDirectory,
  stdio: 'inherit',
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
  return !segments.includes('build');
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
console.log('正在后台执行首次 Wasm 预览构建（首次需下载依赖，可能耗时数分钟；文档站已可访问，产物就绪后交互预览自动加载）…');
building = true;
runPublish().then((ok) => {
  building = false;
  if (!ok) {
    console.log('首次 Wasm 构建未完成：文档站仍可访问，Wasm 交互预览暂不可用；修正后保存 Kotlin 文件会自动重试。');
    return;
  }
  console.log('首次构建完成，交互预览已就绪。保存 library/ 或 preview/ 下的 Kotlin 文件会自动重建；按 Ctrl+C 同时停止全部进程。');
  if (pendingRebuild) {
    pendingRebuild = false;
    scheduleRebuild('首次构建期间的文件变化');
  }
});
