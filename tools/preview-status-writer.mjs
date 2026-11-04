// 文件职责：串行写入预览状态，处理 Windows 文件占用；由 preview-build-progress.mjs 组合使用。
import { rename, unlink, writeFile } from 'node:fs/promises';
import { setTimeout as delay } from 'node:timers/promises';

const LOCK_ERRORS = new Set(['EPERM', 'EACCES', 'EBUSY']);
const RETRY_DELAYS_MS = [50, 100, 200, 400];

export function createPreviewStatusWriter(statusFile) {
  const temporaryFile = `${statusFile}.${process.pid}.tmp`;
  let pending = null;
  let writing = null;
  let retryTimer;
  let lastWarningAt = 0;

  async function writeSnapshot(snapshot) {
    try {
      await writeFile(temporaryFile, snapshot);
      for (let attempt = 0; ; attempt += 1) {
        try {
          await rename(temporaryFile, statusFile);
          return true;
        } catch (error) {
          if (!LOCK_ERRORS.has(error.code)) throw error;
          if (attempt < RETRY_DELAYS_MS.length) {
            await delay(RETRY_DELAYS_MS[attempt]);
            continue;
          }
          // Windows 可能允许写入却禁止替换。读取端会保留上次可解析的完整状态。
          await writeFile(statusFile, snapshot);
          return true;
        }
      }
    } catch (error) {
      if (Date.now() - lastWarningAt > 15000) {
        console.warn(`[预览进度] 状态写入暂时失败（${error.code || error.message}），将重试；Gradle 编译继续。`);
        lastWarningAt = Date.now();
      }
      return false;
    } finally {
      await unlink(temporaryFile).catch(() => {});
    }
  }

  function startWriting() {
    if (writing || pending === null) return;
    clearTimeout(retryTimer);
    writing = (async () => {
      while (pending !== null) {
        const snapshot = pending;
        pending = null;
        if (!await writeSnapshot(snapshot)) {
          pending ??= snapshot;
          retryTimer = setTimeout(startWriting, 1000);
          retryTimer.unref();
          break;
        }
      }
    })().finally(() => { writing = null; });
  }

  return {
    enqueue(snapshot) {
      // 高频输出只保留最新快照；单一写入队列避免临时文件互相覆盖。
      pending = snapshot;
      startWriting();
    },
    async flush() {
      startWriting();
      await writing;
      return pending === null;
    },
  };
}
