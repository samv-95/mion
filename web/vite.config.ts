import { defineConfig } from 'vitest/config'
import react from '@vitejs/plugin-react'

// The dev server must be reachable through the Arena preview proxy
// (https://<port>-<sandbox>.e2b.app), so it binds 0.0.0.0 and accepts any host.
export default defineConfig({
  plugins: [react()],
  server: {
    host: true,
    port: 5173,
    strictPort: true,
    allowedHosts: true,
    cors: true,
  },
  preview: {
    host: true,
    port: 4173,
    strictPort: true,
    allowedHosts: true,
  },
  test: {
    environment: 'jsdom',
    globals: true,
    include: ['src/**/*.test.tsx'],
    setupFiles: ['./src/test/setup.ts'],
  },
})
