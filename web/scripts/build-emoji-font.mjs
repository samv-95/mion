#!/usr/bin/env node
/**
 * Builds a tiny colour-emoji webfont containing only the glyphs the app copy
 * actually uses. Without this, emoji fall back to the (missing) system emoji
 * font on Linux/CI browsers and render as empty boxes.
 *
 * Requires: python3 + fonttools + brotli  ->  pip install fonttools brotli
 * Usage:    npm run emoji:subset
 */
import { execFileSync } from 'node:child_process'
import { existsSync, mkdirSync } from 'node:fs'
import { dirname, resolve } from 'node:path'
import { fileURLToPath } from 'node:url'

const root = resolve(dirname(fileURLToPath(import.meta.url)), '..')
const source = resolve(
  root,
  'node_modules/@fontsource/noto-color-emoji/files/noto-color-emoji-emoji-400-normal.woff2',
)
const output = resolve(root, 'src/assets/noto-color-emoji-subset.woff2')

// every emoji used in the UI copy, plus the variation selector / ZWJ joiners
// needed so multi-codepoint sequences (e.g. ⚙️) keep rendering as one glyph
const EMOJI = [
  'U+2705', // ✅ payment confirmed
  'U+1F4E6', // 📦 product order
  'U+1F527', // 🔧 service booking
  'U+1F69A', // 🚚 courier delivery
  'U+2699', // ⚙️ asset maintenance
  'U+26A1', // ⚡ general orchestration / AI decision
  'U+1F4A1', // 💡 asset insight note
  'U+2B50', // ⭐ provider rating
  'U+1F4CD', // 📍 location
  'U+FE0F', // variation selector-16
  'U+200D', // zero width joiner
]

if (!existsSync(source)) {
  console.error(
    `[emoji] source font not found: ${source}\n        run \`npm install\` first (needs @fontsource/noto-color-emoji).`,
  )
  process.exit(1)
}

mkdirSync(dirname(output), { recursive: true })

try {
  execFileSync(
    'python3',
    [
      '-m',
      'fontTools.subset',
      source,
      `--unicodes=${EMOJI.join(',')}`,
      '--flavor=woff2',
      '--layout-features=*',
      '--no-hinting',
      '--desubroutinize',
      `--output-file=${output}`,
    ],
    { stdio: 'inherit' },
  )
  console.log(`[emoji] wrote ${output}`)
} catch (error) {
  console.error(
    '[emoji] subsetting failed — install the tooling with `pip install fonttools brotli`.\n',
    error instanceof Error ? error.message : error,
  )
  process.exit(1)
}
