<!-- 文件职责：在 hyper_ui 中负责实现 vitepress/.vitepress/theme/components/WasmPreview 页面展示、状态呈现与用户交互编排。 -->
<script setup lang="ts">
import { withBase } from 'vitepress'
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'

// 开发期热更新：dev-watch.mjs 每次发布 Wasm 产物后会更新该标记文件，
// 页面内所有 WasmPreview 共享一个轮询器，检测到变化即重载各自的 iframe。
const VERSION_FILE = '/wasm-preview/.build-version'
const VERSION_EVENT = 'hyper-ui:wasm-preview-updated'
const versionPollInterval = 2000
let lastVersion: string | null = null
let versionPollStarted = false

async function pollPreviewVersion() {
  if (versionPollStarted) return
  versionPollStarted = true
  const check = async () => {
    try {
      const response = await fetch(withBase(VERSION_FILE), { cache: 'no-store' })
      if (!response.ok) return
      const version = (await response.text()).trim()
      if (lastVersion === null) {
        lastVersion = version
      } else if (version && version !== lastVersion) {
        lastVersion = version
        window.dispatchEvent(new Event(VERSION_EVENT))
      }
    } catch {
      // 标记文件尚不存在（未运行过 publish）时静默忽略。
    }
  }
  await check()
  setInterval(check, versionPollInterval)
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

const resolvedSource = computed(() => {
  const source = withBase(DEFAULT_PREVIEW_SOURCE)
  const demo = props.demo?.trim()

  if (!demo) {
    return source
  }

  const demoHash = demo
    .split('/')
    .map((segment) => encodeURIComponent(segment))
    .join('/')

  return `${source}#${demoHash}`
})

const frameHeight = computed(() =>
  typeof props.height === 'number' ? `${props.height}px` : props.height
)

const frame = ref<HTMLIFrameElement | null>(null)

function reloadFrame() {
  try {
    frame.value?.contentWindow?.location.reload()
  } catch {
    // iframe 跨域或尚未加载完成时忽略，等待下一次轮询。
  }
}

onMounted(() => {
  if (!import.meta.env.DEV) return
  void pollPreviewVersion()
  window.addEventListener(VERSION_EVENT, reloadFrame)
})

onBeforeUnmount(() => {
  window.removeEventListener(VERSION_EVENT, reloadFrame)
})
</script>

<template>
  <figure class="wasm-preview">
    <figcaption class="wasm-preview__header">
      <span class="wasm-preview__title">{{ title }}</span>
      <a
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
        ref="frame"
        class="wasm-preview__frame"
        :src="resolvedSource"
        :title="title"
        :style="{ height: frameHeight }"
        loading="lazy"
        referrerpolicy="strict-origin-when-cross-origin"
        allow="clipboard-write; fullscreen"
        allowfullscreen
      />
    </div>

    <div v-if="$slots.default" class="wasm-preview__caption">
      <slot />
    </div>
  </figure>
</template>
