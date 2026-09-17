/**
 * Icon set — mirrors the Material icon usage of the Compose screens
 * (Icons.Outlined.* / Icons.Filled.*) through the Material Symbols font.
 */
export type IconName =
  | 'auto_awesome'
  | 'close'
  | 'send'
  | 'mic'
  | 'layers'
  | 'storefront'
  | 'map'
  | 'shield'
  | 'chat_bubble'
  | 'assignment'
  | 'keyboard_arrow_up'
  | 'keyboard_arrow_down'
  | 'add'
  | 'delete'
  | 'location_on'
  | 'directions_car'
  | 'laptop_mac'
  | 'ac_unit'
  | 'smartphone'
  | 'dns'
  | 'build'
  | 'my_location'
  | 'place'

export function Icon({
  name,
  filled = false,
  size = 20,
  className = '',
}: {
  name: IconName
  filled?: boolean
  size?: number
  className?: string
}) {
  return (
    <span
      className={`icon ${filled ? 'filled' : ''} ${className}`.trim()}
      style={{ fontSize: `${size}px`, width: `${size}px`, height: `${size}px` }}
      aria-hidden="true"
    >
      {name}
    </span>
  )
}
