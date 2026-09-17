import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'
import { viteSingleFile } from 'vite-plugin-singlefile'

/**
 * Produces ONE self-contained HTML file (JS, CSS, fonts and emoji all inlined)
 * that can be opened from the file system or sent to a phone without any server.
 * Output: web/standalone/index.html  ->  copied to docs/aura-ai-app.html
 */
export default defineConfig({
  plugins: [react(), viteSingleFile()],
  build: {
    outDir: 'standalone',
    emptyOutDir: true,
    cssCodeSplit: false,
    // inline every asset (fonts, emoji subset) as a data URI
    assetsInlineLimit: 100_000_000,
    reportCompressedSize: false,
    target: 'es2022',
  },
})
