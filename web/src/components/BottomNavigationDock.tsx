import type { AppNavTab } from '../lib/models'
import { Icon, type IconName } from './Icon'

interface DockEntry {
  tab: AppNavTab
  label: string
  icon: IconName
  testTag: string
}

const ENTRIES: DockEntry[] = [
  { tab: 'CHAT', label: 'Chat', icon: 'chat_bubble', testTag: 'nav_chat_tab' },
  { tab: 'MAP', label: 'Map', icon: 'map', testTag: 'nav_map_tab' },
  { tab: 'ORDERS', label: 'Orders', icon: 'assignment', testTag: 'nav_orders_tab' },
  { tab: 'CONTEXT', label: 'Context', icon: 'shield', testTag: 'nav_context_tab' },
  { tab: 'PARTNER', label: 'Partner', icon: 'storefront', testTag: 'nav_partner_tab' },
]

export function BottomNavigationDock({
  currentTab,
  onTabSelected,
}: {
  currentTab: AppNavTab
  onTabSelected: (tab: AppNavTab) => void
}) {
  return (
    <nav className="dock">
      {ENTRIES.map((entry) => {
        const isSelected = currentTab === entry.tab
        return (
          <button
            key={entry.tab}
            type="button"
            data-testid={entry.testTag}
            aria-current={isSelected ? 'page' : undefined}
            className={`dock-item ${isSelected ? 'active' : ''}`}
            onClick={() => onTabSelected(entry.tab)}
          >
            <span className="halo">
              <Icon name={entry.icon} filled={isSelected} size={20} />
            </span>
            <span className="label">{entry.label}</span>
          </button>
        )
      })}
    </nav>
  )
}
