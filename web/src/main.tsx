import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { PreviewShell } from './App'
// Self-hosted fonts so the preview works offline and through the sandbox proxy
import '@fontsource-variable/vazirmatn'
import '@fontsource/material-symbols-outlined/300.css'
import './styles.css'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <PreviewShell />
  </StrictMode>,
)
