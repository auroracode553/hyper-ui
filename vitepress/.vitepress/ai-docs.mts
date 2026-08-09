/**
 * 文件职责：从权威 Markdown 源码派生 AI 可读取的站点入口。
 *
 * 这些文件只写入 VitePress 构建目录，不在源码树中维护第二份组件文档。
 */
import { mkdir, readdir, readFile, writeFile } from 'node:fs/promises'
import { dirname, isAbsolute, relative, resolve } from 'node:path'
import type { HeadConfig, SiteConfig, TransformContext, UserConfig } from 'vitepress'

interface AiDocsOptions {
  siteUrl: URL
}

interface MarkdownDocument {
  content: string
  path: string
  title: string
}

type AiDocsConfig = Pick<UserConfig, 'buildEnd' | 'sitemap' | 'transformHead'>

const PRIMARY_DOCUMENTS = [
  'index.md',
  'getting-started.md',
  'theme.md',
  'state-model.md',
  'component-index.md'
]

export function createAiDocsConfig(options: AiDocsOptions): AiDocsConfig {
  return {
    sitemap: {
      hostname: options.siteUrl.href
    },
    transformHead: (context) => createAiHead(context, options.siteUrl),
    buildEnd: (siteConfig) => writeAiArtifacts(siteConfig, options.siteUrl)
  }
}

function createAiHead(context: TransformContext, siteUrl: URL): HeadConfig[] {
  const markdownPath = normalizePath(context.page)

  return [
    ['link', { rel: 'canonical', href: toHtmlUrl(markdownPath, siteUrl) }],
    [
      'link',
      {
        rel: 'alternate',
        type: 'text/markdown',
        href: new URL(markdownPath, siteUrl).href,
        title: `${context.title} Markdown`
      }
    ],
    [
      'link',
      {
        rel: 'alternate',
        type: 'text/plain',
        href: new URL('llms.txt', siteUrl).href,
        title: 'HyperUI AI documentation index'
      }
    ],
    [
      'link',
      {
        rel: 'sitemap',
        type: 'application/xml',
        href: new URL('sitemap.xml', siteUrl).href,
        title: 'HyperUI sitemap'
      }
    ],
    [
      'meta',
      {
        name: 'robots',
        content: 'index,follow,max-snippet:-1,max-image-preview:large,max-video-preview:-1'
      }
    ]
  ]
}

async function writeAiArtifacts(siteConfig: SiteConfig, siteUrl: URL): Promise<void> {
  const sourceDirectory = isAbsolute(siteConfig.srcDir)
    ? siteConfig.srcDir
    : resolve(siteConfig.root, siteConfig.srcDir)
  const outputDirectory = isAbsolute(siteConfig.outDir)
    ? siteConfig.outDir
    : resolve(siteConfig.root, siteConfig.outDir)

  const markdownPaths = await collectMarkdownPaths(sourceDirectory)
  const documents = await Promise.all(
    markdownPaths.map((path) => readDocument(sourceDirectory, path))
  )
  const orderedDocuments = orderDocuments(documents)

  await Promise.all(
    orderedDocuments.map((document) =>
      writeDerivedFile(outputDirectory, document.path, document.content)
    )
  )
  await Promise.all([
    writeDerivedFile(outputDirectory, 'llms.txt', createLlmsIndex(orderedDocuments, siteUrl)),
    writeDerivedFile(
      outputDirectory,
      'llms-full.txt',
      createFullCorpus(orderedDocuments, siteUrl)
    ),
    writeDerivedFile(outputDirectory, 'robots.txt', createRobotsFile(siteUrl))
  ])
}

async function collectMarkdownPaths(directory: string): Promise<string[]> {
  const entries = await readdir(directory, { withFileTypes: true })
  const nestedPaths = await Promise.all(
    entries.map(async (entry) => {
      const entryPath = resolve(directory, entry.name)

      if (entry.isDirectory()) {
        return collectMarkdownPaths(entryPath)
      }
      if (entry.isFile() && entry.name.endsWith('.md')) {
        return [entryPath]
      }
      return []
    })
  )

  return nestedPaths.flat().sort((left, right) => left.localeCompare(right, 'en'))
}

async function readDocument(sourceDirectory: string, filePath: string): Promise<MarkdownDocument> {
  const content = await readFile(filePath, 'utf8')
  const path = normalizePath(relative(sourceDirectory, filePath))
  const heading = content.match(/^#\s+(.+)$/m)?.[1]?.trim()

  return {
    content: normalizeNewlines(content).trimEnd() + '\n',
    path,
    title: heading || path.replace(/\.md$/, '')
  }
}

function orderDocuments(documents: MarkdownDocument[]): MarkdownDocument[] {
  const primaryOrder = new Map(PRIMARY_DOCUMENTS.map((path, index) => [path, index]))

  return [...documents].sort((left, right) => {
    const leftOrder = primaryOrder.get(left.path)
    const rightOrder = primaryOrder.get(right.path)

    if (leftOrder !== undefined || rightOrder !== undefined) {
      return (leftOrder ?? Number.MAX_SAFE_INTEGER) - (rightOrder ?? Number.MAX_SAFE_INTEGER)
    }
    return left.path.localeCompare(right.path, 'en')
  })
}

function createLlmsIndex(documents: MarkdownDocument[], siteUrl: URL): string {
  const primary = documents.filter((document) => PRIMARY_DOCUMENTS.includes(document.path))
  const components = documents.filter((document) => document.path.startsWith('components/'))
  const patterns = documents.filter((document) => document.path.startsWith('patterns/'))
  const additional = documents.filter(
    (document) =>
      !PRIMARY_DOCUMENTS.includes(document.path) &&
      !document.path.startsWith('components/') &&
      !document.path.startsWith('patterns/')
  )

  return [
    '# HyperUI',
    '',
    '> Android Jetpack Compose UI component library. Markdown is the authoritative API source; Wasm is only an interactive preview.',
    '',
    `- [Complete documentation corpus](${new URL('llms-full.txt', siteUrl).href}): All documentation in one plain-text response.`,
    `- [HTML documentation](${siteUrl.href}): Human-readable website.`,
    '',
    createLinkSection('Start here', primary, siteUrl),
    createLinkSection('Components', components, siteUrl),
    createLinkSection('Patterns', patterns, siteUrl),
    createLinkSection('Additional guides', additional, siteUrl)
  ]
    .filter(Boolean)
    .join('\n')
    .trimEnd() + '\n'
}

function createLinkSection(
  heading: string,
  documents: MarkdownDocument[],
  siteUrl: URL
): string {
  if (documents.length === 0) {
    return ''
  }

  const links = documents.map(
    (document) => `- [${document.title}](${new URL(document.path, siteUrl).href})`
  )

  return [`## ${heading}`, '', ...links, ''].join('\n')
}

function createFullCorpus(documents: MarkdownDocument[], siteUrl: URL): string {
  const sections = documents.map((document) =>
    [
      `<!-- Source: ${new URL(document.path, siteUrl).href} -->`,
      '',
      document.content.trimEnd()
    ].join('\n')
  )

  return [
    '# HyperUI complete documentation corpus',
    '',
    '> Generated from the authoritative Markdown files during the VitePress build. Do not infer APIs from the Wasm preview.',
    '',
    `Documentation home: ${siteUrl.href}`,
    `Document index: ${new URL('llms.txt', siteUrl).href}`,
    '',
    ...sections.map((section) => `${section}\n`)
  ].join('\n')
}

function createRobotsFile(siteUrl: URL): string {
  return [
    'User-agent: *',
    `Allow: ${siteUrl.pathname}`,
    '',
    `Sitemap: ${new URL('sitemap.xml', siteUrl).href}`,
    `# AI index: ${new URL('llms.txt', siteUrl).href}`,
    ''
  ].join('\n')
}

async function writeDerivedFile(
  outputDirectory: string,
  relativePath: string,
  content: string
): Promise<void> {
  const destination = resolve(outputDirectory, relativePath)
  const relativeDestination = relative(outputDirectory, destination)

  if (relativeDestination.startsWith('..') || isAbsolute(relativeDestination)) {
    throw new Error(`Refusing to write outside VitePress output: ${relativePath}`)
  }

  await mkdir(dirname(destination), { recursive: true })
  await writeFile(destination, content, 'utf8')
}

function toHtmlUrl(markdownPath: string, siteUrl: URL): string {
  const route = markdownPath
    .replace(/(^|\/)index\.md$/, '$1')
    .replace(/\.md$/, '.html')

  return new URL(route, siteUrl).href
}

function normalizePath(path: string): string {
  return path.replace(/\\/g, '/')
}

function normalizeNewlines(content: string): string {
  return content.replace(/\r\n?/g, '\n')
}
