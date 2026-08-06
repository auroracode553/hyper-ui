<script setup lang="ts">
import { withBase } from 'vitepress'
import { computed } from 'vue'

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
