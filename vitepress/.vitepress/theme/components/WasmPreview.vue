<!-- 文件职责：展示 Wasm 构建与组件首帧加载阶段，并嵌入纯组件交互示例。 -->
<script setup lang="ts">
import { withBase } from 'vitepress'
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue'

type BuildPhase = 'preparing' | 'compiling' | 'ready' | 'error'
type FramePhase = 'loading' | 'rendering' | 'ready' | 'error'
type DisplayPhase = 'checking' | 'preparing' | 'compiling' | 'loading' | 'rendering' | 'ready' | 'missing' | 'error'

interface BuildStatus {
  phase: BuildPhase
  message: string
  updatedAt: number
}

interface WasmPreviewProps {
  demo?: string
  title?: string
  height?: number | string
}

const props = withDefaults(defineProps<WasmPreviewProps>(), {
  title: 'HyperUI 交互预览',
  height: 480
})

const READINESS_FILE = '/wasm-preview/preview-ready.json'
const BUILD_STATUS_FILE = '/wasm-preview/preview-build-status.json'
const POLL_INTERVAL_MS = 2000
const FRAME_TIMEOUT_MS = 60000
const fromDevWatch = import.meta.env.DEV && import.meta.env.VITE_HYPER_UI_DEV_WATCH === '1'

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
let pollTimer: ReturnType<typeof setInterval> | undefined
let frameTimer: ReturnType<typeof setTimeout> | undefined
let canvasTimer: ReturnType<typeof setInterval> | undefined

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
  if (buildStatus.value?.phase === 'preparing') return 'preparing'
  if (buildStatus.value?.phase === 'compiling') return 'compiling'
  if (buildStatus.value?.phase === 'error') return 'error'
  if (!sourceAvailable.value) return checked.value ? 'missing' : 'checking'
  return framePhase.value
})

const progressSteps: Record<DisplayPhase, number> = {
  checking: 0, preparing: 1, compiling: 2, loading: 3,
  rendering: 4, ready: 0, missing: 0, error: 0
}
const progressStep = computed(() => progressSteps[displayPhase.value])

const statusText = computed(() => {
  switch (displayPhase.value) {
    case 'preparing': return buildStatus.value?.message || '正在准备 Wasm 依赖与包锁文件'
    case 'compiling': return buildStatus.value?.message || '正在编译组件预览'
    case 'loading': return '正在加载 Wasm 预览资源'
    case 'rendering': return '正在渲染组件首帧'
    case 'error': return buildStatus.value?.phase === 'error'
      ? buildStatus.value.message : '组件预览加载超时，请重新检查'
    case 'missing': return developmentSource
      ? 'Wasm 开发服务器尚未连接。' : '交互预览尚未生成。'
    default: return '正在检查组件预览…'
  }
})

const statusDetail = computed(() => {
  if (displayPhase.value === 'preparing' || displayPhase.value === 'compiling') {
    const elapsedSeconds = Math.max(0, Math.floor((clock.value - (buildStatus.value?.updatedAt || clock.value)) / 1000))
    return `本阶段已耗时 ${Math.floor(elapsedSeconds / 60)} 分 ${elapsedSeconds % 60} 秒；详细任务进度请查看 dev:watch 终端。`
  }
  if (displayPhase.value === 'error') return buildStatus.value?.phase === 'error'
    ? '修复终端中的错误后保存 Kotlin 文件，预览会自动重试。'
    : '请检查浏览器控制台或 Wasm 开发服务器，再重新检查。'
  if (displayPhase.value === 'missing') return '启动 dev:watch 后，组件编译状态和预览会自动出现在这里。'
  return '页面已可阅读，组件就绪后会自动显示。'
})

function beginFrameLoad() {
  if (frameTimer) clearTimeout(frameTimer)
  if (canvasTimer) clearInterval(canvasTimer)
  framePhase.value = 'loading'
  frameAttempt.value += 1
  frameTimer = setTimeout(() => {
    if (framePhase.value !== 'ready') framePhase.value = 'error'
  }, FRAME_TIMEOUT_MS)
}

function finishFrameLoad() {
  if (frameTimer) clearTimeout(frameTimer)
  if (canvasTimer) clearInterval(canvasTimer)
  const targetOrigin = new URL(resolvedSource.value, window.location.href).origin
  frame.value?.contentWindow?.postMessage({ type: 'hyper-ui-preview:ack' }, targetOrigin)
  framePhase.value = 'ready'
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
    if (canvas && canvas.width > 0 && canvas.height > 0) finishFrameLoad()
  } catch {
    // 独立 Wasm 开发服务器可能跨域，由 iframe 的 postMessage 通知就绪。
  }
}

function onFrameLoad() {
  if (framePhase.value !== 'loading') return
  framePhase.value = 'rendering'
  checkFrameCanvas()
  if (framePhase.value !== 'ready') {
    canvasTimer = setInterval(checkFrameCanvas, 250)
  }
}

function onPreviewMessage(event: MessageEvent) {
  if (event.source !== frame.value?.contentWindow) return
  if (event.origin !== new URL(resolvedSource.value, window.location.href).origin) return
  if (event.data?.type !== 'hyper-ui-preview:ready') return
  finishFrameLoad()
}

async function readBuildStatus(): Promise<BuildStatus | null> {
  if (!fromDevWatch || developmentSource) return null
  try {
    const response = await fetch(withBase(BUILD_STATUS_FILE), { cache: 'no-store' })
    if (!response.ok) return null
    const value: unknown = await response.json()
    if (typeof value !== 'object' || value === null || !('phase' in value)
      || !('message' in value) || !('updatedAt' in value)) return null
    if (!['preparing', 'compiling', 'ready', 'error'].includes(String(value.phase))
      || typeof value.message !== 'string' || typeof value.updatedAt !== 'number') return null
    return value as BuildStatus
  } catch {
    return null
  }
}

async function readPublishedVersion(): Promise<string | null> {
  try {
    const response = await fetch(withBase(READINESS_FILE), { cache: 'no-store' })
    if (!response.ok) return null
    const manifest: unknown = await response.json()
    return typeof manifest === 'object' && manifest !== null && 'version' in manifest
      && typeof manifest.version === 'string' && manifest.version ? manifest.version : null
  } catch {
    return null
  }
}

async function checkPreview() {
  if (developmentSource) {
    if (!sourceAvailable.value) {
      sourceAvailable.value = true
      beginFrameLoad()
    }
    checked.value = true
    return
  }
  const [status, version] = await Promise.all([readBuildStatus(), readPublishedVersion()])
  buildStatus.value = status
  checked.value = true
  if (version && version !== publishedVersion.value) {
    publishedVersion.value = version
    sourceAvailable.value = true
    beginFrameLoad()
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
  if (import.meta.env.DEV) {
    pollTimer = setInterval(() => {
      clock.value = Date.now()
      void checkPreview()
    }, POLL_INTERVAL_MS)
  }
})

onBeforeUnmount(() => {
  window.removeEventListener('message', onPreviewMessage)
  if (pollTimer) clearInterval(pollTimer)
  if (frameTimer) clearTimeout(frameTimer)
  if (canvasTimer) clearInterval(canvasTimer)
})
</script>

<template>
  <figure class="wasm-preview">
    <figcaption class="wasm-preview__header">
      <span class="wasm-preview__title">{{ title }}</span>
      <a
        v-if="sourceAvailable"
        class="wasm-preview__open-link"
        :href="resolvedSource"
        target="_blank"
        rel="noreferrer"
        :aria-label="`在新窗口打开：${title}`"
      >在新窗口打开</a>
    </figcaption>

    <div class="wasm-preview__stage">
      <div class="wasm-preview__device">
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
            <div class="wasm-preview__skeleton" aria-hidden="true">
              <span /><span /><span />
            </div>
            <div class="wasm-preview__status-copy">
              <p class="wasm-preview__status-title" role="status">{{ statusText }}</p>
              <p class="wasm-preview__status-detail">{{ statusDetail }}</p>
              <div v-if="progressStep" class="wasm-preview__steps" :aria-label="`加载阶段 ${progressStep}/4`">
                <span
                  v-for="step in 4"
                  :key="step"
                  :class="{ 'is-complete': step < progressStep, 'is-active': step === progressStep }"
                />
              </div>
              <p v-if="progressStep" class="wasm-preview__step-label">{{ progressStep }} / 4</p>
              <div v-if="displayPhase === 'error' || displayPhase === 'missing'" class="wasm-preview__status-actions">
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
