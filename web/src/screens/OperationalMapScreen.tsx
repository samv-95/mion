import { useCallback, useEffect, useRef, useState } from 'react'
import { AuraBadge, AuraCard } from '../components/Common'
import { Icon, type IconName } from '../components/Icon'

interface MapNode {
  id: string
  label: string
  type: 'DESTINATION' | 'STORE' | 'SERVICE_HQ' | 'TAXI'
  xRatio: number
  yRatio: number
  detail: string
  status: string
  eta: string
}

/** Same hard-coded operational nodes as OperationalMapScreen.kt */
const NODES: MapNode[] = [
  { id: 'node-office', label: 'TechCorp Tower', type: 'DESTINATION', xRatio: 0.72, yRatio: 0.35, detail: '4x SSD Package Delivery', status: 'Destination A', eta: 'ETA: 14:00' },
  { id: 'node-home', label: 'Home (Maple Ave)', type: 'DESTINATION', xRatio: 0.32, yRatio: 0.68, detail: 'Fresh Flowers & HVAC Service', status: 'Destination B', eta: 'ETA: 16:00' },
  { id: 'node-techdirect', label: 'TechDirect MegaStore', type: 'STORE', xRatio: 0.85, yRatio: 0.2, detail: 'Dispatched via Express Courier', status: 'Pickup', eta: 'Departed' },
  { id: 'node-florist', label: 'Bloom Atelier', type: 'STORE', xRatio: 0.2, yRatio: 0.85, detail: 'Packaged in Climate Carrier', status: 'Pickup', eta: 'Departed' },
  { id: 'node-hvac', label: 'HVAC Master Service Van', type: 'SERVICE_HQ', xRatio: 0.15, yRatio: 0.45, detail: 'Technician En Route', status: 'Dispatch', eta: 'Wed 14:00' },
  { id: 'node-taxi', label: 'SwiftMobility Sedan', type: 'TAXI', xRatio: 0.6, yRatio: 0.5, detail: 'Scheduled for 18:00 Commute', status: 'Active Ride', eta: '18:00 Sharp' },
]

const NODE_ICON: Record<MapNode['type'], IconName> = {
  DESTINATION: 'location_on',
  STORE: 'storefront',
  SERVICE_HQ: 'build',
  TAXI: 'directions_car',
}

export function OperationalMapScreen() {
  const [selected, setSelected] = useState<MapNode>(NODES[0])
  const [pan, setPan] = useState({ x: 0, y: 0 })
  const wrapRef = useRef<HTMLDivElement>(null)
  const canvasRef = useRef<HTMLCanvasElement>(null)
  const dragState = useRef<{ active: boolean; x: number; y: number }>({
    active: false,
    x: 0,
    y: 0,
  })

  /** Re-draws the minimalist road network + multi-stop route polylines. */
  const draw = useCallback((panX: number, panY: number) => {
    const canvas = canvasRef.current
    const wrap = wrapRef.current
    if (!canvas || !wrap) return
    const ctx = canvas.getContext('2d')
    if (!ctx) return

    const dpr = window.devicePixelRatio || 1
    const width = wrap.clientWidth
    const height = wrap.clientHeight
    canvas.width = width * dpr
    canvas.height = height * dpr
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0)
    ctx.clearRect(0, 0, width, height)

    const roadColor = '#1B202A'
    const primaryRoadColor = '#262C3A'

    ctx.lineWidth = 2
    ctx.strokeStyle = roadColor
    for (let i = 1; i <= 8; i++) {
      const y = height * (i / 9) + panY * 0.1
      ctx.beginPath()
      ctx.moveTo(0, y)
      ctx.lineTo(width, y)
      ctx.stroke()
    }
    for (let j = 1; j <= 6; j++) {
      const x = width * (j / 7) + panX * 0.1
      ctx.beginPath()
      ctx.moveTo(x, 0)
      ctx.lineTo(x, height)
      ctx.stroke()
    }

    // Diagonal expressway corridor
    ctx.lineWidth = 4
    ctx.strokeStyle = primaryRoadColor
    ctx.beginPath()
    ctx.moveTo(0, height * 0.8 + panY * 0.1)
    ctx.lineTo(width, height * 0.2 + panY * 0.1)
    ctx.stroke()

    const pos = (xRatio: number, yRatio: number): [number, number] => [
      width * xRatio + panX,
      height * yRatio + panY,
    ]
    const techDirect = pos(0.85, 0.2)
    const office = pos(0.72, 0.35)
    const florist = pos(0.2, 0.85)
    const home = pos(0.32, 0.68)
    const taxi = pos(0.6, 0.5)

    ctx.lineCap = 'round'
    ctx.lineWidth = 3

    // Route 1: Courier -> Office
    ctx.strokeStyle = '#3B82F6'
    ctx.beginPath()
    ctx.moveTo(techDirect[0], techDirect[1])
    ctx.lineTo(techDirect[0] - 30, office[1])
    ctx.lineTo(office[0], office[1])
    ctx.stroke()

    // Route 2: Florist -> Home
    ctx.strokeStyle = '#2563EB'
    ctx.beginPath()
    ctx.moveTo(florist[0], florist[1])
    ctx.lineTo(home[0], florist[1] - 40)
    ctx.lineTo(home[0], home[1])
    ctx.stroke()

    // Route 3: Taxi office -> home
    ctx.lineWidth = 2
    ctx.strokeStyle = 'rgba(37, 99, 235, 0.4)'
    ctx.beginPath()
    ctx.moveTo(office[0], office[1])
    ctx.lineTo(taxi[0], taxi[1])
    ctx.lineTo(home[0], home[1])
    ctx.stroke()
  }, [])

  useEffect(() => {
    draw(pan.x, pan.y)
    const onResize = () => draw(pan.x, pan.y)
    window.addEventListener('resize', onResize)
    return () => window.removeEventListener('resize', onResize)
  }, [draw, pan.x, pan.y])

  const onPointerDown = (e: React.PointerEvent<HTMLCanvasElement>) => {
    dragState.current = { active: true, x: e.clientX, y: e.clientY }
    e.currentTarget.setPointerCapture(e.pointerId)
  }

  const onPointerMove = (e: React.PointerEvent<HTMLCanvasElement>) => {
    const s = dragState.current
    if (!s.active) return
    const dx = (e.clientX - s.x) * 0.4
    const dy = (e.clientY - s.y) * 0.4
    s.x = e.clientX
    s.y = e.clientY
    setPan((p) => ({ x: p.x + dx, y: p.y + dy }))
  }

  const onPointerUp = (e: React.PointerEvent<HTMLCanvasElement>) => {
    dragState.current.active = false
    if (e.currentTarget.hasPointerCapture(e.pointerId)) {
      e.currentTarget.releasePointerCapture(e.pointerId)
    }
  }

  return (
    <div className="map-screen" ref={wrapRef}>
      <canvas
        ref={canvasRef}
        className="map-canvas"
        onPointerDown={onPointerDown}
        onPointerMove={onPointerMove}
        onPointerUp={onPointerUp}
        onPointerCancel={onPointerUp}
      />

      {/* Interactive overlay nodes */}
      {NODES.map((node) => {
        const isSelected = selected.id === node.id
        const left = `calc(${node.xRatio * 100}% + ${pan.x - 22}px)`
        const top = `calc(${node.yRatio * 100}% + ${pan.y - 22}px)`
        return (
          <button
            key={node.id}
            type="button"
            aria-label={node.label}
            className={`map-node ${isSelected ? 'selected' : ''}`}
            style={{ left, top }}
            onClick={() => setSelected(node)}
          >
            <Icon name={NODE_ICON[node.type]} filled={isSelected} size={18} />
          </button>
        )
      })}

      {/* Top control overlay */}
      <div className="map-top">
        <div className="row">
          <h2 className="screen-title">OPERATIONAL MAP</h2>
          <AuraBadge text="Live Multi-Route" highlight />
        </div>

        <button
          type="button"
          className="shield-chip"
          style={{ cursor: 'pointer' }}
          onClick={() => setPan({ x: 0, y: 0 })}
        >
          <Icon name="my_location" size={13} />
          Recenter
        </button>
      </div>

      {/* Bottom inspection sheet */}
      <div className="map-bottom">
        <AuraCard background="var(--deep-charcoal)" border="var(--border-medium)">
          <div className="row-between">
            <div>
              <div className="row">
                <Icon name={NODE_ICON[selected.type]} size={16} />
                <span style={{ fontSize: 14, fontWeight: 700 }}>{selected.label}</span>
              </div>
              <p className="screen-subtitle" style={{ fontSize: 12, color: 'var(--text-secondary)' }}>
                {selected.detail}
              </p>
            </div>
            <span
              className="status-tag"
              style={{ background: 'var(--blue-muted)', color: 'var(--blue-light)' }}
            >
              {selected.eta}
            </span>
          </div>

          <hr className="divider" style={{ margin: '10px 0 8px' }} />

          <div className="row" style={{ gap: 6 }}>
            {NODES.slice(0, 4).map((node) => (
              <button
                key={node.id}
                type="button"
                className={`stop-pill ${selected.id === node.id ? 'active' : ''}`}
                onClick={() => setSelected(node)}
              >
                {node.label.slice(0, 10)}..
              </button>
            ))}
          </div>
        </AuraCard>
      </div>
    </div>
  )
}
