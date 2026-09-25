<!-- 文件职责：在 hyper_ui 中负责实现 vitepress/.vitepress/theme/components/WasmPreview 页面展示、状态呈现与用户交互编排。 -->
<script setup lang="ts">
import { withBase } from 'vitepress'
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'

const READINESS_FILE = '/wasm-preview/preview-ready.json'
const POLL_INTERVAL_MS = 5000

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

// 开发服务器独立运行；VitePress 只嵌入它的页面，不负责启动 Gradle。
const developmentSource = resolveDevelopmentSource(import.meta.env.VITE_HYPER_UI_PREVIEW_DEV_URL)

function encodeDemoHash(demo: string | undefined): string {
  return (demo?.trim() || '').split('/').map(encodeURIComponent).join('/')
}

interface WasmPreviewProps {
  demo?: string
  title?: string
  height?: number | string
}

const DEFAULT_PREVIEW_SOURCE = '/wasm-preview/index.html'

const props = withDefaults(defineProps<WasmPreviewProps>(), {
  title: 'HyperUI 交互预览',
  height: 720
})

const previewState = ref<'checking' | 'ready' | 'missing'>('checking')
const publishedVersion = ref('')
let pollTimer: ReturnType<typeof setInterval> | undefined

async function checkPreview() {
  try {
    if (developmentSource) {
      // 跨端口开发服务器无需 CORS 响应头；网络请求失败时保留可操作的占位提示。
      await fetch(developmentSource, { mode: 'no-cors', cache: 'no-store' })
      previewState.value = 'ready'
      return
    }
    const response = await fetch(withBase(READINESS_FILE), { cache: 'no-store' })
    if (!response.ok) throw new Error('Preview is not published')
    const manifest: unknown = await response.json()
    if (typeof manifest !== 'object' || manifest === null || !('version' in manifest)
      || typeof manifest.version !== 'string' || !manifest.version) {
      throw new Error('Invalid preview manifest')
    }
    publishedVersion.value = manifest.version
    previewState.value = 'ready'
  } catch {
    previewState.value = 'missing'
  }
}

const resolvedSource = computed(() => {
  if (developmentSource) {
    const demoHash = encodeDemoHash(props.demo)
    return demoHash ? `${developmentSource}#${demoHash}` : developmentSource
  }
  const source = `${withBase(DEFAULT_PREVIEW_SOURCE)}?v=${encodeURIComponent(publishedVersion.value)}`
  const demoHash = encodeDemoHash(props.demo)
  if (!demoHash) {
    return source
  }
  return `${source}#${demoHash}`
})

const frameHeight = computed(() =>
  typeof props.height === 'number' ? `${props.height}px` : props.height
)

onMounted(() => {
  void checkPreview()
  if (import.meta.env.DEV) {
    pollTimer = setInterval(() => { void checkPreview() }, POLL_INTERVAL_MS)
  }
})

onBeforeUnmount(() => {
  if (pollTimer) clearInterval(pollTimer)
})
</script>

<template>
  <figure class="wasm-preview">
    <figcaption class="wasm-preview__header">
      <span class="wasm-preview__title">{{ title }}</span>
      <a
        v-if="previewState === 'ready'"
        class="wasm-preview__open-link"
        :href="resolvedSource"
        target="_blank"
        rel="noreferrer"
        :aria-label="`在新窗口打开：${title}`"
      >
        在新窗口打开
      </a>
    </figcaption>

    <div class="wasm-preview__viewport">
      <iframe
        v-if="previewState === 'ready'"
        :key="publishedVersion"
        class="wasm-preview__frame"
        :src="resolvedSource"
        :title="title"
        :style="{ height: frameHeight }"
        loading="lazy"
        referrerpolicy="strict-origin-when-cross-origin"
        allow="clipboard-write; fullscreen"
        allowfullscreen
      />
      <div v-else class="wasm-preview__status" :aria-busy="previewState === 'checking'">
        <p role="status">
          {{ previewState === 'checking'
            ? '正在检查交互预览…'
            : developmentSource
              ? 'Wasm 开发服务器尚未连接。启动后预览会自动加载。'
              : '交互预览尚未发布，文档内容仍可正常阅读。' }}
        </p>
        <div v-if="previewState === 'missing'" class="wasm-preview__status-actions">
          <button type="button" @click="checkPreview">重新检查</button>
          <a :href="withBase('/preview-update-workflow.html')">查看手动发布说明</a>
        </div>
      </div>
    </div>

    <div v-if="$slots.default" class="wasm-preview__caption">
      <slot />
    </div>
  </figure>
</template>
