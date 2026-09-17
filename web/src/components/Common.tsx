import type { ReactNode } from 'react'

/** ui/components/CommonComponents.kt — AuraBadge */
export function AuraBadge({
  text,
  highlight = false,
}: {
  text: string
  highlight?: boolean
}) {
  return <span className={`badge ${highlight ? 'highlight' : ''}`}>{text}</span>
}

/** ui/components/CommonComponents.kt — ContextTag */
export function ContextTag({ label }: { label: string }) {
  return (
    <span className="tag" title={label}>
      <span className="dot" />
      {label}
    </span>
  )
}

/** ui/components/CommonComponents.kt — AuraCard */
export function AuraCard({
  children,
  background,
  border,
  onClick,
  className = '',
}: {
  children: ReactNode
  background?: string
  border?: string
  onClick?: () => void
  className?: string
}) {
  return (
    <div
      className={`card ${onClick ? 'clickable' : ''} ${className}`.trim()}
      style={{ background, borderColor: border }}
      onClick={onClick}
    >
      {children}
    </div>
  )
}

/** ui/navigation/BottomNavigationDock.kt */
export function Toggle({
  checked,
  onChange,
  label,
}: {
  checked: boolean
  onChange: (v: boolean) => void
  label: string
}) {
  return (
    <button
      type="button"
      role="switch"
      aria-checked={checked}
      aria-label={label}
      className="switch"
      onClick={() => onChange(!checked)}
    >
      <span className="knob" />
    </button>
  )
}

export function Modal({
  title,
  children,
  confirmLabel,
  onConfirm,
  onDismiss,
  confirmDisabled = false,
}: {
  title: string
  children: ReactNode
  confirmLabel: string
  onConfirm: () => void
  onDismiss: () => void
  confirmDisabled?: boolean
}) {
  return (
    <div className="modal-backdrop" onClick={onDismiss}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <h3>{title}</h3>
        {children}
        <div className="modal-actions">
          <button type="button" className="btn text muted" onClick={onDismiss}>
            Cancel
          </button>
          <button
            type="button"
            className="btn primary"
            disabled={confirmDisabled}
            style={confirmDisabled ? { opacity: 0.5, cursor: 'not-allowed' } : undefined}
            onClick={onConfirm}
          >
            {confirmLabel}
          </button>
        </div>
      </div>
    </div>
  )
}
