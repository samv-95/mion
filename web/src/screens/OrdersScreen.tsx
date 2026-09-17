import { AuraBadge, AuraCard } from '../components/Common'
import { Icon } from '../components/Icon'
import type { FulfillmentPlan, PlanStatus } from '../lib/models'
import { repository, useAppState } from '../lib/store'

const STATUS_COLOR: Record<PlanStatus, string> = {
  DRAFT_PROPOSED: 'var(--blue-light)',
  CONFIRMED_PAID: 'var(--green)',
  IN_FULFILLMENT: 'var(--green)',
  COMPLETED: 'var(--text-muted)',
}

export function OrdersScreen() {
  const { fulfillmentPlans } = useAppState()

  return (
    <div className="screen">
      <div className="pad-top" />
      <div className="row-between">
        <div>
          <h2 className="screen-title">ORDERS &amp; FULFILLMENT</h2>
          <p className="screen-subtitle">Multi-Destination &amp; Multi-Provider Plans</p>
        </div>
        <AuraBadge text={`${fulfillmentPlans.length} Active`} highlight />
      </div>

      <div style={{ height: 16 }} />
      <hr className="divider" />
      <div style={{ height: 16 }} />

      {fulfillmentPlans.length === 0 ? (
        <div className="empty-state">
          <div>
            <Icon name="assignment" size={48} />
            <div style={{ fontSize: 15, fontWeight: 600, marginTop: 12 }}>No Orders Yet</div>
            <p className="screen-subtitle" style={{ fontSize: 12, marginTop: 4 }}>
              Ask Aura AI in the chat to order products or book services.
            </p>
            <div style={{ marginTop: 16 }}>
              <button
                type="button"
                className="btn primary"
                onClick={() => repository.selectTab('CHAT')}
              >
                Go to Chat
              </button>
            </div>
          </div>
        </div>
      ) : (
        <div className="scroll-area list">
          {fulfillmentPlans.map((plan) => (
            <OrderPlanItem key={plan.id} plan={plan} />
          ))}
        </div>
      )}
    </div>
  )
}

function OrderPlanItem({ plan }: { plan: FulfillmentPlan }) {
  const statusColor = STATUS_COLOR[plan.status]

  return (
    <AuraCard background="var(--deep-charcoal)">
      <div className="row-between">
        <div>
          <div style={{ fontSize: 15, fontWeight: 700 }}>Plan {plan.planCode}</div>
          <div className="muted" style={{ fontSize: 11 }}>
            Created {plan.createdAt} • {plan.intents.length} Sub-Orders
          </div>
        </div>
        <span
          className="status-tag"
          style={{ background: 'rgba(148,163,184,0.12)', color: statusColor }}
        >
          {plan.status.replace('_', ' ')}
        </span>
      </div>

      <hr className="divider" style={{ margin: '12px 0 10px' }} />

      {plan.intents.map((intent) => (
        <div
          key={intent.id}
          className="row-between"
          style={{ padding: '4px 0', alignItems: 'center' }}
        >
          <div className="row" style={{ flex: '1 1 auto', alignItems: 'center' }}>
            <span className="dot" />
            <div>
              <div style={{ fontSize: 12, fontWeight: 500 }} dir="auto">
                {intent.targetItemOrService}
              </div>
              <div className="muted" style={{ fontSize: 10 }} dir="auto">
                {intent.selectedOffer.providerName} → {intent.resolvedDestinationLabel}
              </div>
            </div>
          </div>
          <span style={{ fontSize: 12, fontWeight: 600, color: 'var(--text-secondary)' }}>
            {intent.selectedOffer.currency}
            {Math.round(intent.selectedOffer.price)}
          </span>
        </div>
      ))}

      <hr className="divider" style={{ margin: '12px 0 10px' }} />

      <div className="row-between">
        <div>
          <div className="total-label">Total Unified</div>
          <div style={{ fontSize: 16, fontWeight: 700 }}>
            {plan.currency}
            {Math.round(plan.totalCost)}
          </div>
        </div>

        <div className="row">
          <button
            type="button"
            className="btn outline small"
            onClick={() => repository.selectTab('MAP')}
          >
            Map Tracking
          </button>
          {plan.status === 'DRAFT_PROPOSED' && (
            <button
              type="button"
              className="btn primary small"
              onClick={() => repository.confirmFulfillmentPlan(plan.id)}
            >
              Approve All
            </button>
          )}
        </div>
      </div>
    </AuraCard>
  )
}
