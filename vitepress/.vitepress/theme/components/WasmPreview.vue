<!-- 文件职责：展示 Wasm 构建与组件首帧加载阶段，并嵌入纯组件交互示例。 -->
<script setup lang="ts">
import { withBase } from 'vitepress'
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue'

type BuildPhase = 'preparing' | 'compiling' | 'ready' | 'error' | 'stopped'
type FramePhase = 'loading' | 'rendering' | 'ready' | 'outdated' | 'error'
type DisplayPhase = 'checking' | BuildPhase | FramePhase | 'missing' | 'stale'

interface BuildStatus {
  phase: BuildPhase
  message: string
  updatedAt: number
  startedAt: number
  phaseStartedAt: number
  currentTask: string
  taskStartedAt: number
  lastOutput: string
  lastOutputAt: number
  failure: string
}

interface WasmPreviewProps {
  demo?: string
  title?: string
}

const props = withDefaults(defineProps<WasmPreviewProps>(), {
  title: 'HyperUI 交互预览'
})

const READINESS_FILE = '/wasm-preview/preview-ready.json'
const BUILD_STATUS_FILE = '/wasm-preview/preview-build-status.json'
const POLL_INTERVAL_MS = 1000
const FRAME_TIMEOUT_MS = 60000
const BUILD_HEARTBEAT_TIMEOUT_MS = 15000

function resolveDevelopmentSource(rawUrl: string | undefined): string | null {
  if (!import.meta.env.DEV || !rawUrl?.trim()) return null
  try {
    const base = new URL(rawUrl.endsWith('/') ? rawUrl : `${rawUrl}/`)
    if (base.protocol !== 'http:' && base.protocol !== 'https:') return null
    return new URL('index.html', base).href
  } catch {
    return null
  }
}

const developmentSource = resolveDevelopmentSource(import.meta.env.VITE_HYPER_UI_PREVIEW_DEV_URL)
const buildStatus = ref<BuildStatus | null>(null)
const publishedVersion = ref('')
const sourceAvailable = ref(false)
const checked = ref(false)
const clock = ref(Date.now())
const framePhase = ref<FramePhase>('loading')
const frameAttempt = ref(0)
const frame = ref<HTMLIFrameElement | null>(null)
const previewWidth = ref(350)
const viewportWidths = [320, 350, 390, 430]
const deviceStyle = computed(() => ({ '--preview-width': `${previewWidth.value}px` }))
let pollTimer: ReturnType<typeof setInterval> | undefined
let frameTimer: ReturnType<typeof setTimeout> | undefined
let canvasTimer: ReturnType<typeof setInterval> | undefined
let clockTimer: ReturnType<typeof setInterval> | undefined
let checkingPreview = false
let disposed = false

function encodeDemoHash(demo: string | undefined): string {
  return (demo?.trim() || '').split('/').map(encodeURIComponent).join('/')
}

const resolvedSource = computed(() => {
  const source = developmentSource || `${withBase('/wasm-preview/index.html')}?v=${encodeURIComponent(publishedVersion.value)}`
  const separator = source.includes('?') ? '&' : '?'
  const demoHash = encodeDemoHash(props.demo)
  return `${source}${separator}embedded=1${demoHash ? `#${demoHash}` : ''}`
})

const displayPhase = computed<DisplayPhase>(() => {
  const build = buildStatus.value
  if (build?.phase === 'preparing' || build?.phase === 'compiling') {
    return clock.value - build.updatedAt > BUILD_HEARTBEAT_TIMEOUT_MS ? 'stale' : build.phase
  }
  if (buildStatus.value?.phase === 'error') return 'error'
  if (buildStatus.value?.phase === 'stopped') return 'stopped'
  if (!sourceAvailable.value) return checked.value ? 'missing' : 'checking'
  return framePhase.value
})

// 编译和发布期间显示真实进度，不能把上次产物当成当前源码的预览。
const showBuildDetails = computed(() => buildStatus.value && buildStatus.value.phase !== 'ready' &&
  ['preparing', 'compiling', 'stale', 'stopped', 'error'].includes(displayPhase.value))
const canRetry = computed(() => ['error', 'missing', 'outdated', 'stale', 'stopped'].includes(displayPhase.value))

function elapsedTime(since: number, until = clock.value): string {
  const seconds = Math.max(0, Math.floor((until - since) / 1000))
  return `${Math.floor(seconds / 60)} 分 ${seconds % 60} 秒`
}

const taskDetail = computed(() => {
  const build = buildStatus.value
  if (!build?.currentTask) return ''
  const running = displayPhase.value === 'preparing' || displayPhase.value === 'compiling'
  return `${build.currentTask} · ${elapsedTime(build.taskStartedAt, running ? clock.value : build.updatedAt)}`
})
const outputDetail = computed(() => {
  const build = buildStatus.value
  if (!build) return ''
  if (build.failure && build.phase === 'error') return build.failure
  return build.lastOutput || '等待 Gradle 输出任务状态…'
})

const progressSteps: Record<DisplayPhase, number> = {
  checking: 0, preparing: 1, compiling: 2, loading: 3,
  rendering: 4, ready: 0, missing: 0, error: 0, outdated: 0, stale: 0, stopped: 0
}
const progressStep = computed(() => progressSteps[displayPhase.value])

const statusText = computed(() => {
  switch (displayPhase.value) {
    case 'preparing': return buildStatus.value?.message || '正在准备预览'
    case 'compiling': return buildStatus.value?.message || '正在编译 Kotlin/Wasm'
    case 'loading': return '正在加载示例'
    case 'rendering': return '正在显示示例'
    case 'outdated': return '预览产物尚未更新'
    case 'stale': return '编译进度连接已中断'
    case 'stopped': return '预览编译已停止'
    case 'error': return buildStatus.value?.phase === 'error'
      ? buildStatus.value.message : '组件预览加载超时，请重新检查'
    case 'missing': return developmentSource
      ? 'Wasm 开发服务器尚未连接。' : '交互预览尚未生成。'
    default: return '正在检查组件预览…'
  }
})

const statusDetail = computed(() => {
  if (displayPhase.value === 'preparing' || displayPhase.value === 'compiling') {
    const build = buildStatus.value!
    const quiet = clock.value - build.lastOutputAt > 10000
      ? `；最近 ${elapsedTime(build.lastOutputAt)} 没有新输出，进程心跳正常` : ''
    return `本次构建 ${elapsedTime(build.startedAt)}，当前阶段 ${elapsedTime(build.phaseStartedAt)}${quiet}。`
  }
  if (displayPhase.value === 'outdated') return `当前产物未能确认 ${props.demo || '指定组件'} 示例。请使用当前源码重新生成 Preview 产物。`
  if (displayPhase.value === 'stale') return '已超过 15 秒未收到编译心跳，请查看维护者终端。页面不会把旧产物视为已更新。'
  if (displayPhase.value === 'stopped') return '请由维护者手动恢复预览编译，再重新检查。'
  if (displayPhase.value === 'error') return buildStatus.value?.phase === 'error'
    ? '修复终端中的错误后保存 Kotlin 文件，预览会自动重试。'
    : '请检查浏览器控制台或 Wasm 开发服务器，再重新检查。'
  if (displayPhase.value === 'missing') return '请由维护者生成 Preview 产物，然后重新检查。'
  return '页面已可阅读，组件就绪后会自动显示。'
})

function clearFrameTimers() {
  if (frameTimer) clearTimeout(frameTimer)
  if (canvasTimer) clearInterval(canvasTimer)
}

function beginFrameLoad() {
  clearFrameTimers()
  framePhase.value = 'loading'
  frameAttempt.value += 1
  frameTimer = setTimeout(() => {
    clearFrameTimers()
    framePhase.value = 'error'
  }, FRAME_TIMEOUT_MS)
}

function finishFrameLoad(actualDemoId: unknown) {
  if (framePhase.value !== 'loading' && framePhase.value !== 'rendering') return
  clearFrameTimers()
  const targetOrigin = new URL(resolvedSource.value, window.location.href).origin
  frame.value?.contentWindow?.postMessage({ type: 'hyper-ui-preview:ack' }, targetOrigin)
  // Canvas 存在只代表程序已启动；必须确认实际组件 ID，阻止旧产物回退到 Button。
  framePhase.value = typeof actualDemoId === 'string' && actualDemoId.length > 0
    && actualDemoId === props.demo?.trim() ? 'ready' : 'outdated'
}

function findCanvas(root: Document | Element | ShadowRoot): HTMLCanvasElement | null {
  const directCanvas = root.querySelector('canvas')
  if (directCanvas) return directCanvas
  for (const element of root.querySelectorAll('*')) {
    if (!element.shadowRoot) continue
    const canvas = findCanvas(element.shadowRoot)
    if (canvas) return canvas
  }
  return null
}

function checkFrameCanvas() {
  try {
    const document = frame.value?.contentDocument
    const canvas = document ? findCanvas(document) : null
    if (canvas && canvas.width > 0 && canvas.height > 0) {
      finishFrameLoad(document?.getElementById('hyper-ui-preview-root')?.getAttribute('data-preview-demo'))
    }
  } catch {
    // 独立 Wasm 开发服务器可能跨域，由 iframe 的 postMessage 通知就绪。
  }
}

function onFrameLoad() {
  if (framePhase.value !== 'loading') return
  framePhase.value = 'rendering'
  checkFrameCanvas()
  if (framePhase.value === 'rendering') {
    canvasTimer = setInterval(checkFrameCanvas, 250)
  }
}

function onPreviewMessage(event: MessageEvent) {
  if (event.source !== frame.value?.contentWindow) return
  if (event.origin !== new URL(resolvedSource.value, window.location.href).origin) return
  if (event.data?.type !== 'hyper-ui-preview:ready') return
  finishFrameLoad(event.data.demoId)
}

async function readPreviewJson(path: string): Promise<unknown> {
  const controller = new AbortController()
  const timeout = setTimeout(() => controller.abort(), 5000)
  try {
    const response = await fetch(withBase(path), { cache: 'no-store', signal: controller.signal })
    return response.ok ? await response.json() : null
  } catch {
    return null
  } finally {
    clearTimeout(timeout)
  }
}

async function readBuildStatus(): Promise<BuildStatus | null> {
  if (!import.meta.env.DEV || developmentSource) return null
  const value = await readPreviewJson(BUILD_STATUS_FILE)
  if (typeof value !== 'object' || value === null || !('phase' in value)
    || !('message' in value) || !('updatedAt' in value)) return null
  if (!['preparing', 'compiling', 'ready', 'error', 'stopped'].includes(String(value.phase))
    || typeof value.message !== 'string' || typeof value.updatedAt !== 'number'
    || !Number.isFinite(value.updatedAt)) return null
  const fields = value as Record<string, unknown>
  const timestamp = (key: string): number => {
    const field = fields[key]
    return typeof field === 'number' && Number.isFinite(field) ? field : value.updatedAt as number
  }
  const text = (key: string) => typeof fields[key] === 'string' ? fields[key] as string : ''
  return {
    phase: value.phase as BuildPhase, message: value.message, updatedAt: value.updatedAt,
    startedAt: timestamp('startedAt'), phaseStartedAt: timestamp('phaseStartedAt'),
    currentTask: text('currentTask'), taskStartedAt: timestamp('taskStartedAt'),
    lastOutput: text('lastOutput'), lastOutputAt: timestamp('lastOutputAt'), failure: text('failure'),
  }
}

async function readPublishedVersion(): Promise<string | null> {
  const manifest = await readPreviewJson(READINESS_FILE)
  return typeof manifest === 'object' && manifest !== null && 'version' in manifest
    && typeof manifest.version === 'string' && manifest.version ? manifest.version : null
}

async function checkPreview() {
  if (checkingPreview) return
  checkingPreview = true
  try {
    if (developmentSource) {
      if (!sourceAvailable.value) {
        sourceAvailable.value = true
        beginFrameLoad()
      }
      checked.value = true
      return
    }
    const [status, version] = await Promise.all([readBuildStatus(), readPublishedVersion()])
    if (disposed) return
    if (status) buildStatus.value = status
    checked.value = true
    // 新打开的页面先等待正在进行的构建，不加载已知过期的 iframe。
    if (buildStatus.value && buildStatus.value.phase !== 'ready') return
    if (version && version !== publishedVersion.value) {
      publishedVersion.value = version
      sourceAvailable.value = true
      beginFrameLoad()
    }
  } finally {
    checkingPreview = false
  }
}

function retryPreview() {
  if (sourceAvailable.value) beginFrameLoad()
  void checkPreview()
}

watch(() => props.demo, () => {
  if (sourceAvailable.value) beginFrameLoad()
})

onMounted(() => {
  window.addEventListener('message', onPreviewMessage)
  void checkPreview()
  clockTimer = setInterval(() => { clock.value = Date.now() }, 1000)
  if (import.meta.env.DEV) {
    pollTimer = setInterval(() => {
      void checkPreview()
    }, POLL_INTERVAL_MS)
  }
})

onBeforeUnmount(() => {
  disposed = true
  window.removeEventListener('message', onPreviewMessage)
  if (pollTimer) clearInterval(pollTimer)
  if (clockTimer) clearInterval(clockTimer)
  clearFrameTimers()
})
</script>

<template>
  <figure class="wasm-preview">
    <figcaption class="wasm-preview__header">
      <span class="wasm-preview__title">{{ title }}</span>
      <div class="wasm-preview__toolbar" aria-label="预览工具">
        <label class="wasm-preview__width-control">
          <span>手机宽度</span>
          <select v-model.number="previewWidth" aria-label="手机预览宽度">
            <option v-for="width in viewportWidths" :key="width" :value="width">{{ width }}dp</option>
          </select>
        </label>
        <button type="button" class="wasm-preview__tool-button" :disabled="!sourceAvailable" @click="retryPreview">重置</button>
        <a
        v-if="displayPhase === 'ready'"
        class="wasm-preview__open-link"
        :href="resolvedSource"
        target="_blank"
        rel="noreferrer"
        :aria-label="`在新窗口打开：${title}`"
        >打开预览</a>
      </div>
    </figcaption>

    <div class="wasm-preview__stage">
      <div class="wasm-preview__device" :style="deviceStyle">
        <div class="wasm-preview__notch" aria-hidden="true">
          <span class="wasm-preview__camera" />
        </div>
        <div class="wasm-preview__screen">
          <iframe
            v-if="sourceAvailable"
            :key="`${publishedVersion}-${props.demo || ''}-${frameAttempt}`"
            ref="frame"
            class="wasm-preview__frame"
            :src="resolvedSource"
            :title="title"
            referrerpolicy="strict-origin-when-cross-origin"
            allow="clipboard-write; fullscreen"
            allowfullscreen
            @load="onFrameLoad"
          />
          <div
            v-if="displayPhase !== 'ready'"
            class="wasm-preview__status"
            :aria-busy="progressStep > 0 || displayPhase === 'checking'"
          >
            <div v-if="displayPhase === 'loading' || displayPhase === 'rendering'" class="wasm-preview__skeleton" aria-hidden="true">
              <span /><span /><span />
            </div>
            <div class="wasm-preview__status-copy">
              <p class="wasm-preview__status-title" role="status">{{ statusText }}</p>
              <p class="wasm-preview__status-detail">{{ statusDetail }}</p>
              <div v-if="showBuildDetails" class="wasm-preview__build-progress">
                <p v-if="taskDetail" class="wasm-preview__current-task">{{ taskDetail }}</p>
                <p class="wasm-preview__build-output">{{ outputDetail }}</p>
              </div>
              <div v-if="progressStep" class="wasm-preview__steps" :aria-label="`加载阶段 ${progressStep}/4`">
                <span
                  v-for="step in 4"
                  :key="step"
                  :class="{ 'is-complete': step < progressStep, 'is-active': step === progressStep }"
                />
              </div>
              <p v-if="progressStep" class="wasm-preview__step-label">阶段 {{ progressStep }} / 4 · 实时更新</p>
              <div v-if="canRetry" class="wasm-preview__status-actions">
                <button type="button" @click="retryPreview">重新检查</button>
                <a :href="withBase('/preview-update-workflow.html')">查看构建说明</a>
              </div>
            </div>
          </div>
        </div>
        <div class="wasm-preview__home-indicator" aria-hidden="true" />
      </div>
    </div>

    <div v-if="$slots.default" class="wasm-preview__caption"><slot /></div>
  </figure>
</template>
