// 文件职责：采集 Gradle 输出；由 dev-watch.mjs 注入路径和批次，preview-status-writer.mjs 负责可靠写入。
import { createPreviewStatusWriter } from './preview-status-writer.mjs';

const OUTPUT_LIMIT = 360;
const WRITE_INTERVAL_MS = 250;

function describeTask(task, fallback) {
  if (/compile.*Kotlin/i.test(task)) return '正在编译 Kotlin/Wasm';
  if (/link|binaryen/i.test(task)) return '正在链接与优化 Wasm';
  if (/webpack|distribution/i.test(task)) return '正在打包预览资源';
  if (/npm|yarn|packageLock/i.test(task)) return '正在处理 Wasm 依赖';
  if (/publishWasmToVitePress/i.test(task)) return '正在发布组件预览';
  return fallback;
}

export function createPreviewBuildProgress({ statusFile, attempt }) {
  const writer = createPreviewStatusWriter(statusFile);
  const startedAt = Date.now();
  let lastWriteAt = 0;
  let phaseMessage = '';
  let status = {
    phase: 'preparing', message: '', attempt, startedAt, phaseStartedAt: startedAt,
    updatedAt: startedAt, command: '', currentTask: '', taskStartedAt: startedAt,
    lastOutput: '', lastOutputAt: startedAt, failure: '',
  };

  function publish(force = false) {
    const now = Date.now();
    if (!force && now - lastWriteAt < WRITE_INTERVAL_MS) return;
    status.updatedAt = now;
    writer.enqueue(`${JSON.stringify(status)}\n`);
    lastWriteAt = now;
  }

  return {
    setPhase(phase, message) {
      const now = Date.now();
      phaseMessage = message;
      status = {
        ...status, phase, message,
        ...(phase === 'preparing' || phase === 'compiling' ? {
          phaseStartedAt: now, command: '', currentTask: '', taskStartedAt: now,
          lastOutput: '', lastOutputAt: now, failure: '',
        } : {}),
      };
      publish(true);
    },
    startCommand(command) {
      status.command = command;
      publish(true);
    },
    recordOutput(rawLine) {
      const line = rawLine.replace(/\u001b\[[0-9;]*[A-Za-z]/g, '').trim();
      if (!line) return;
      const now = Date.now();
      status.lastOutput = line.slice(0, OUTPUT_LIMIT);
      status.lastOutputAt = now;
      const task = /^> Task (:\S+)/.exec(line)?.[1];
      if (task && task !== status.currentTask) {
        status.currentTask = task;
        status.taskStartedAt = now;
        status.message = describeTask(task, phaseMessage);
      }
      if (!status.failure && /^(e:|error:|Execution failed for task|Could not|Caused by:)/i.test(line)) {
        status.failure = line.slice(0, OUTPUT_LIMIT);
      }
      publish(Boolean(task));
    },
    heartbeat() { publish(true); },
    flush() { return writer.flush(); },
  };
}
