import type { Theme } from 'vitepress'
import DefaultTheme from 'vitepress/theme'
import WasmPreview from './components/WasmPreview.vue'
import './custom.css'

export default {
  extends: DefaultTheme,
  enhanceApp({ app }) {
    app.component('WasmPreview', WasmPreview)
  }
} satisfies Theme
