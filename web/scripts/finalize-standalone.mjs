#!/usr/bin/env node
/**
 * Post-processes the single-file build produced by vite.standalone.config.ts:
 *  - drops the `.woff` fallbacks that only bloat the base64 payload (every
 *    browser that can run the app supports woff2),
 *  - writes the result to web/standalone/aura-ai-app.html and to
 *    docs/aura-ai-app.html so it can be downloaded straight from GitHub.
 */
import { mkdirSync, readFileSync, statSync, writeFileSync } from 'node:fs'
import { dirname, resolve } from 'node:path'
import { fileURLToPath } from 'node:url'

const webRoot = resolve(dirname(fileURLToPath(import.meta.url)), '..')
const repoRoot = resolve(webRoot, '..')
const source = resolve(webRoot, 'standalone/index.html')
const outputs = [
  resolve(webRoot, 'standalone/aura-ai-app.html'),
  resolve(repoRoot, 'docs/aura-ai-app.html'),
]

let html = readFileSync(source, 'utf8')
const before = Buffer.byteLength(html)

// strip the woff fallback entries from the inlined @font-face rules
html = html.replace(/url\(data:font\/woff;base64,[^)]+\)\s*format\('woff'\),?/g, '')
html = html.replace(/,\s*url\(data:font\/woff;base64,[^)]+\)\s*format\('woff'\)/g, '')

for (const output of outputs) {
  mkdirSync(dirname(output), { recursive: true })
  writeFileSync(output, html)
}

const after = statSync(resolve(webRoot, 'standalone/aura-ai-app.html')).size
console.log(
  `[standalone] inlined bundle: ${(before / 1024 / 1024).toFixed(2)} MB -> ${(after / 1024 / 1024).toFixed(2)} MB`,
)
for (const output of outputs) console.log(`[standalone] wrote ${output}`)
