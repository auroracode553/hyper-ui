import { env } from 'node:process'
import { fileURLToPath } from 'node:url'
import { defineConfig } from 'vitepress'
import { createAiDocsConfig } from './ai-docs.mts'

const siteBase = env.VITEPRESS_BASE || '/'
const siteOrigin = env.VITEPRESS_SITE_ORIGIN || 'https://auroracode553.github.io'
const siteUrl = new URL(siteBase, siteOrigin)
const publicDirectory = fileURLToPath(new URL('../public', import.meta.url))
// 动态获取当前年份，避免版权年份写死
const currentYear = new Date().getFullYear()

export default defineConfig({
  srcDir: 'docs',
  base: siteBase,
  lang: 'zh-CN',
  title: 'HyperUI',
  titleTemplate: ':title | HyperUI',
  description: 'HyperUI Android 手机端 Compose UI 组件库文档与交互预览',
  lastUpdated: true,
  ...createAiDocsConfig({ siteUrl }),
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
      message: '面向 Android 手机端的 Jetpack Compose UI 组件库',
      copyright: `Copyright © ${currentYear} HyperUI`
    },
    // top navigation removed because the sidebar already provides menu navigation
    nav: [],
    sidebar: [
      {
        text: '开始使用',
        items: [
          { text: '接入与最小配置', link: '/getting-started' },
          { text: 'Android 手机端规范', link: '/mobile-guidelines' },
          { text: '主题配置', link: '/theme' },
          { text: '组件索引', link: '/component-index' }
        ]
      },
      {
        text: '基础组件',
        items: [
          { text: 'Button', link: '/components/basic/hyper-button' },
          { text: 'IconButton', link: '/components/basic/hyper-icon-button' }
        ]
      },
      {
        text: '表单组件',
        items: [
          { text: 'TextField', link: '/components/form/hyper-text-field' },
          { text: 'Switch', link: '/components/form/hyper-switch' },
          { text: 'Checkbox', link: '/components/form/hyper-checkbox' },
          { text: 'Radio', link: '/components/form/hyper-radio' },
          { text: 'Segmented', link: '/components/form/hyper-segmented' },
          { text: 'Slider', link: '/components/form/hyper-slider' }
        ]
      },
      {
        text: '容器组件',
        items: [
          { text: 'Backdrop', link: '/components/container/hyper-backdrop' },
          { text: 'Panel', link: '/components/container/hyper-panel' },
          { text: 'ColorPicker', link: '/components/container/hyper-color-picker' }
        ]
      },
      {
        text: '导航组件',
        items: [
          { text: 'NavBar', link: '/components/navigation/hyper-nav-bar' },
          { text: 'Drawer', link: '/components/navigation/hyper-drawer' },
          { text: 'SlideMenu', link: '/components/navigation/hyper-slide-menu' },
          { text: 'FilterBar', link: '/components/navigation/hyper-filter-bar' },
          { text: 'TabBar', link: '/components/navigation/hyper-tab-bar' }
        ]
      },
      {
        text: '列表组件',
        items: [
          { text: 'List', link: '/components/list/hyper-list' },
          { text: 'SectionedList', link: '/components/list/hyper-sectioned-list' },
          { text: 'MenuList', link: '/components/list/hyper-menu-list' },
          { text: 'ListItem', link: '/components/list/hyper-list-item' }
        ]
      },
      {
        text: '反馈组件',
        items: [
          { text: 'EmptyState', link: '/components/feedback/hyper-empty-state' },
          { text: 'Dropdown', link: '/components/feedback/hyper-dropdown' },
          { text: 'Toast', link: '/components/feedback/hyper-toast' },
          { text: 'ProgressIndicator', link: '/components/feedback/hyper-progress-indicator' },
          { text: 'LevelCapsule', link: '/components/feedback/hyper-level-capsule' },
          { text: 'PlaybackSpeedPanel', link: '/components/feedback/hyper-playback-speed-panel' },
          { text: 'PlaybackSpeedScale', link: '/components/feedback/hyper-playback-speed-scale' },
          { text: 'BatteryIndicator', link: '/components/feedback/hyper-battery-indicator' },
          { text: 'BatteryState', link: '/components/tools/hyper-battery-state' },
          { text: 'Tooltip', link: '/components/feedback/hyper-tooltip' },
          { text: 'Popup', link: '/components/feedback/hyper-popup' },
          { text: 'Dialog', link: '/components/feedback/hyper-dialog' },
          { text: 'AlertDialog', link: '/components/feedback/hyper-alert-dialog' },
          { text: 'UpdateDialog', link: '/components/feedback/hyper-update-dialog' }
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
