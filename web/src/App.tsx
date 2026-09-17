import { useEffect, useState } from 'react'
import { BottomNavigationDock } from './components/BottomNavigationDock'
import { ChatScreen } from './screens/ChatScreen'
import { ContextScreen } from './screens/ContextScreen'
import { OperationalMapScreen } from './screens/OperationalMapScreen'
import { OrdersScreen } from './screens/OrdersScreen'
import { PartnerPortalScreen } from './screens/PartnerPortalScreen'
import { repository, useAppState } from './lib/store'

/** MainActivity.MainAppScreen — Scaffold + Crossfade between the five tabs. */
export function App() {
  const { currentTab } = useAppState()

  return (
    <div className="app" data-testid="app-root">
      <div className="screen" key={currentTab} style={{ padding: 0 }}>
        {currentTab === 'CHAT' && <ChatScreen />}
        {currentTab === 'MAP' && <OperationalMapScreen />}
        {currentTab === 'ORDERS' && <OrdersScreen />}
        {currentTab === 'CONTEXT' && <ContextScreen />}
        {currentTab === 'PARTNER' && <PartnerPortalScreen />}
      </div>
      <BottomNavigationDock
        currentTab={currentTab}
        onTabSelected={(tab) => repository.selectTab(tab)}
      />
    </div>
  )
}

/**
 * Marketing shell around the phone frame so the preview also explains what it
 * is on a desktop screen.
 */
export function PreviewShell() {
  const { fulfillmentPlans } = useAppState()
  const [clock, setClock] = useState(() => new Date())

  useEffect(() => {
    const timer = window.setInterval(() => setClock(new Date()), 30_000)
    return () => window.clearInterval(timer)
  }, [])

  return (
    <div className="stage">
      <aside className="side-note">
        <div className="kicker">Android app • interactive web preview</div>
        <h1>AURA AI</h1>
        <p style={{ marginTop: 0 }}>
          An AI-powered everyday life assistant that orchestrates multi-intent tasks, products,
          services and fulfillment through natural conversation.
        </p>
        <ul>
          <li>Type (or tap a chip for) a natural-language request — Persian or English.</li>
          <li>Aura splits it into intents, chooses offers and builds one Fulfillment Plan.</li>
          <li>Approve the plan, compare alternatives, then check the operational map.</li>
        </ul>
        <p className="hint">
          This is a faithful port of the Jetpack Compose screens (same colours, copy, data and
          orchestration rules). {fulfillmentPlans.length} active plan(s) • local time{' '}
          {clock.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}.
        </p>
      </aside>

      <div className="device">
        <App />
      </div>
    </div>
  )
}
