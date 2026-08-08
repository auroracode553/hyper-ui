import { env } from 'node:process'
import { fileURLToPath } from 'node:url'
import { defineConfig } from 'vitepress'

const siteBase = env.VITEPRESS_BASE || '/'
const publicDirectory = fileURLToPath(new URL('../public', import.meta.url))
// 动态获取当前年份，避免版权年份写死
const currentYear = new Date().getFullYear()

export default defineConfig({
  srcDir: 'docs',
  base: siteBase,
  lang: 'zh-CN',
  title: 'HyperUI',
  titleTemplate: ':title | HyperUI',
  description: 'HyperUI Compose UI 组件库文档与交互预览',
  lastUpdated: true,
  vite: {
    // Markdown 源码位于 vitepress/docs/，静态预览产物集中保存在 vitepress/public/。
    publicDir: publicDirectory
  },
  themeConfig: {
    // 仓库地址配置
    repo: 'auroracode553/hyper-ui',
    repoLabel: 'GitHub',
    // 编辑链接配置：点击可直接跳转到 GitHub 编辑当前文档页
    editLink: {
      pattern: 'https://github.com/auroracode553/hyper-ui/edit/main/vitepress/docs/:path',
      text: '在 GitHub 上编辑此页'
    },
    // 社交链接：导航栏右侧显示 GitHub 图标，点击跳转仓库
    socialLinks: [
      { icon: 'github', link: 'https://github.com/auroracode553/hyper-ui' }
    ],
    // 页脚配置
    footer: {
      message: '基于 Compose Multiplatform 构建的跨平台 UI 组件库',
      copyright: `Copyright © ${currentYear} HyperUI`
    },
    nav: [
      { text: '指南', link: '/getting-started' },
      { text: '组件', link: '/component-index' },
      { text: '交互预览', link: '/preview' }
    ],
    sidebar: [
      {
        text: '开始使用',
        items: [
          { text: '文档首页', link: '/' },
          { text: '接入与最小配置', link: '/getting-started' },
          { text: '主题与颜色', link: '/theme' },
          { text: '状态与架构边界', link: '/state-model' },
          { text: '组件索引', link: '/component-index' },
          { text: '交互预览', link: '/preview' }
        ]
      },
      {
        text: '基础组件',
        items: [
          { text: 'HyperButton', link: '/components/basic/hyper-button' },
          { text: 'HyperIconButton', link: '/components/basic/hyper-icon-button' }
        ]
      },
      {
        text: '表单组件',
        items: [
          { text: 'HyperTextField', link: '/components/form/hyper-text-field' },
          { text: 'HyperSearchField', link: '/components/form/hyper-search-field' },
          { text: 'HyperSwitch', link: '/components/form/hyper-switch' },
          { text: 'HyperCheckbox', link: '/components/form/hyper-checkbox' },
          { text: 'HyperRadioButton', link: '/components/form/hyper-radio-button' },
          { text: 'HyperFilterChip', link: '/components/form/hyper-filter-chip' }
        ]
      },
      {
        text: '容器组件',
        items: [
          { text: 'HyperPanel', link: '/components/container/hyper-panel' },
          { text: 'HyperColorPicker', link: '/components/container/hyper-color-picker' }
        ]
      },
      {
        text: '导航组件',
        items: [
          { text: 'HyperTopBar', link: '/components/navigation/hyper-top-bar' },
          { text: 'HyperDrawer', link: '/components/navigation/hyper-drawer' },
          { text: 'HyperBottomBar', link: '/components/navigation/hyper-bottom-bar' }
        ]
      },
      {
        text: '列表组件',
        items: [
          { text: 'HyperLazyList', link: '/components/list/hyper-lazy-list' },
          { text: 'HyperList', link: '/components/list/hyper-list' },
          { text: 'HyperListItem', link: '/components/list/hyper-list-item' },
          { text: 'HyperMenuGroup', link: '/components/list/hyper-menu-group' }
        ]
      },
      {
        text: '反馈组件',
        items: [
          { text: 'HyperDropdownMenu', link: '/components/feedback/hyper-dropdown-menu' },
          { text: 'HyperProgressBar', link: '/components/feedback/hyper-progress-bar' },
          { text: 'HyperDialog', link: '/components/feedback/hyper-dialog' },
          { text: 'HyperConfirmDialog', link: '/components/feedback/hyper-confirm-dialog' }
        ]
      },
      {
        text: '组合与维护',
        items: [
          { text: '常见页面组合', link: '/patterns/' },
          { text: '组件更新后刷新预览', link: '/preview-update-workflow' },
          { text: '文档维护规则', link: '/maintenance' }
        ]
      }
    ],
    search: {
      provider: 'local',
      options: {
        locales: {
          root: {
            translations: {
              button: {
                buttonText: '搜索',
                buttonAriaLabel: '搜索文档'
              },
              modal: {
                displayDetails: '显示详细列表',
                resetButtonTitle: '重置搜索',
                backButtonTitle: '关闭搜索',
                noResultsText: '没有找到相关内容',
                footer: {
                  selectText: '选择',
                  selectKeyAriaLabel: '回车键',
                  navigateText: '切换',
                  navigateUpKeyAriaLabel: '上箭头',
                  navigateDownKeyAriaLabel: '下箭头',
                  closeText: '关闭',
                  closeKeyAriaLabel: 'Esc 键'
                }
              }
            }
          }
        }
      }
    },
    outline: {
      level: [2, 3],
      label: '本页目录'
    },
    docFooter: {
      prev: '上一篇',
      next: '下一篇'
    },
    lastUpdated: {
      text: '最后更新于',
      formatOptions: {
        dateStyle: 'medium',
        timeStyle: 'short',
        forceLocale: true
      }
    },
    darkModeSwitchLabel: '外观',
    lightModeSwitchTitle: '切换到浅色主题',
    darkModeSwitchTitle: '切换到深色主题',
    sidebarMenuLabel: '菜单',
    returnToTopLabel: '返回顶部',
    skipToContentLabel: '跳到正文'
  }
})
