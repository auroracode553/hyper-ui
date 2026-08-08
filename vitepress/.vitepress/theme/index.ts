/** 文件职责：在 hyper_ui 中负责承载 vitepress/.vitepress/theme/index 模块实现，并集中维护其依赖协作与核心逻辑。 */
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
