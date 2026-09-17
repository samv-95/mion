import { useEffect, useRef, useState } from 'react'
import { AuraBadge, AuraCard, ContextTag } from '../components/Common'
import { Icon } from '../components/Icon'
import type { ChatMessage, DetectedIntent, FulfillmentPlan, Offer, PlanStatus } from '../lib/models'
import { SAMPLE_PROMPTS, ICONIC_PROMPT } from '../lib/seed'
import { repository, useAppState } from '../lib/store'

export function ChatScreen() {
  const { messages, isAiTyping, proactiveAlerts } = useAppState()
  const [inputText, setInputText] = useState('')
  const listRef = useRef<HTMLDivElement>(null)

  // Auto-scroll when messages change or while Aura is typing
  useEffect(() => {
    const el = listRef.current
    if (el) el.scrollTop = el.scrollHeight
  }, [messages.length, isAiTyping])

  const submit = () => {
    if (!inputText.trim()) return
    repository.sendPrompt(inputText)
    setInputText('')
  }

  const alert = proactiveAlerts[0]

  return (
    <div className="app">
      {/* Top header */}
      <header className="chat-header">
        <div>
          <div className="chat-brand">
            <span className="logo">AURA</span>
            <span className="dot" />
            <AuraBadge text="Everyday Life AI" highlight />
          </div>
          <p className="screen-subtitle">Natural Language Orchestrator</p>
        </div>

        <div className="shield-chip">
          <Icon name="shield" size={14} />
          Context Active
        </div>
      </header>

      <hr className="divider" />

      {/* Proactive suggestion banner (Section 7) */}
      {alert && (
        <div className="alert-banner">
          <div className="alert-head">
            <span className="alert-title">
              <Icon name="auto_awesome" size={16} />
              {alert.title}
            </span>
            <button
              type="button"
              className="btn text muted"
              aria-label="Dismiss"
              onClick={() => repository.dismissProactiveAlert(alert.id)}
            >
              <Icon name="close" size={14} />
            </button>
          </div>
          <p className="alert-body" dir="auto">
            {alert.message}
          </p>
          <div className="alert-action">
            <button
              type="button"
              className="btn text"
              onClick={() => {
                repository.sendPrompt(alert.suggestedPrompt)
                repository.dismissProactiveAlert(alert.id)
              }}
            >
              Ask Aura to Coordinate →
            </button>
          </div>
        </div>
      )}

      {/* Messages */}
      <div className="messages" ref={listRef} data-testid="messages">
        {messages.map((message) => (
          <ChatMessageItem key={message.id} message={message} />
        ))}

        {isAiTyping && (
          <div className="typing">
            <span className="spinner" />
            Orchestrating intents across Sellers, Services &amp; APIs...
          </div>
        )}
      </div>

      {/* Quick suggestion chips */}
      <div className="chips-row">
        {SAMPLE_PROMPTS.map((prompt) => (
          <button
            key={prompt}
            type="button"
            className="chip"
            dir="auto"
            title={prompt}
            onClick={() => setInputText(prompt)}
          >
            {prompt.length > 38 ? `${prompt.slice(0, 38)}...` : prompt}
          </button>
        ))}
      </div>

      {/* Composer */}
      <div className="composer">
        <button
          type="button"
          className="icon-btn"
          aria-label="Voice Input"
          onClick={() => setInputText(ICONIC_PROMPT)}
        >
          <Icon name="mic" size={20} />
        </button>

        <textarea
          value={inputText}
          dir="auto"
          rows={1}
          placeholder="هر کاری دارید به زبان طبیعی بگویید..."
          data-testid="chat_input_field"
          onChange={(e) => setInputText(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === 'Enter' && !e.shiftKey) {
              e.preventDefault()
              submit()
            }
          }}
        />

        <button
          type="button"
          className={`icon-btn send ${inputText.trim() ? 'armed' : ''}`}
          aria-label="Send"
          data-testid="send_prompt_button"
          onClick={submit}
        >
          <Icon name="send" filled size={18} />
        </button>
      </div>
    </div>
  )
}

export function ChatMessageItem({ message }: { message: ChatMessage }) {
  if (message.isUser) {
    return (
      <div className="msg-user">
        <div>
          <div className="bubble" dir="auto">
            {message.message}
            <div className="time">{message.timestamp}</div>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div>
      <div className="ai-head">
        <span className="ai-avatar">
          <Icon name="auto_awesome" size={14} />
        </span>
        <span className="ai-name">Aura AI Orchestrator</span>
        <span className="ai-time">{message.timestamp}</span>
      </div>

      <AuraCard background="var(--deep-charcoal)">
        <div className="ai-text" dir="auto">
          {message.message}
        </div>

        {message.usedContextTags.length > 0 && (
          <div className="ctx-row">
            <span className="ctx-label">Context Applied:</span>
            {message.usedContextTags.slice(0, 3).map((tag) => (
              <ContextTag key={tag} label={tag} />
            ))}
          </div>
        )}
      </AuraCard>

      {message.fulfillmentPlan && <FulfillmentPlanCard plan={message.fulfillmentPlan} />}
    </div>
  )
}

const STATUS_LABEL: Record<PlanStatus, { text: string; color: string; bg: string }> = {
  DRAFT_PROPOSED: { text: 'Ready for Review', color: 'var(--blue-light)', bg: 'var(--blue-muted)' },
  CONFIRMED_PAID: { text: 'Paid & Assigned', color: 'var(--green)', bg: 'var(--green-muted)' },
  IN_FULFILLMENT: { text: 'En Route / Active', color: 'var(--green)', bg: 'var(--green-muted)' },
  COMPLETED: { text: 'Completed', color: 'var(--text-muted)', bg: 'rgba(107,114,128,0.15)' },
}

export function FulfillmentPlanCard({ plan }: { plan: FulfillmentPlan }) {
  const status = STATUS_LABEL[plan.status]
  const settled = plan.status !== 'DRAFT_PROPOSED'

  return (
    <div className={`plan-card ${settled ? 'done' : ''}`} data-testid="plan-card">
      <div className="plan-head">
        <span className="plan-title">
          <Icon name="layers" size={18} />
          Fulfillment Plan {plan.planCode}
        </span>
        <span
          className="status-tag"
          style={{ background: status.bg, color: status.color }}
        >
          {status.text}
        </span>
      </div>

      <hr className="divider" style={{ margin: '10px 0' }} />

      <div>
        {plan.intents.map((intent, index) => (
          <IntentItemRow
            key={intent.id}
            index={index + 1}
            intent={intent}
            onSwitchOffer={(offer) => repository.switchOffer(plan.id, intent.id, offer)}
          />
        ))}
      </div>

      <div className="plan-foot">
        <div>
          <div className="total-label">Total Unified Cost</div>
          <div className="total-value">
            {plan.currency}
            {Math.round(plan.totalCost)}
          </div>
        </div>

        <div className="row">
          <button
            type="button"
            className="btn outline"
            onClick={() => repository.selectTab('MAP')}
          >
            <Icon name="map" size={16} />
            Map Layer
          </button>

          {plan.status === 'DRAFT_PROPOSED' ? (
            <button
              type="button"
              className="btn primary"
              data-testid={`approve-${plan.planCode}`}
              onClick={() => repository.confirmFulfillmentPlan(plan.id)}
            >
              Approve &amp; Pay All
            </button>
          ) : (
            <span className="state-pill">Tracking Dispatched</span>
          )}
        </div>
      </div>
    </div>
  )
}

function IntentItemRow({
  index,
  intent,
  onSwitchOffer,
}: {
  index: number
  intent: DetectedIntent
  onSwitchOffer: (offer: Offer) => void
}) {
  const [showAlternatives, setShowAlternatives] = useState(false)
  const offer = intent.selectedOffer

  return (
    <div className="intent-block">
      <div className="intent-row">
        <div className="row" style={{ flex: '1 1 auto', alignItems: 'center' }}>
          <span className="intent-index">{index}</span>
          <div>
            <div className="intent-name" dir="auto">
              {intent.targetItemOrService}
            </div>
            <div className="intent-dest" dir="auto">
              To: {intent.resolvedDestinationLabel}
            </div>
          </div>
        </div>
        <span className="intent-price">
          {offer.currency}
          {Math.round(offer.price)}
        </span>
      </div>

      <div className="offer-row">
        <span className="offer-provider">
          <Icon name="storefront" size={13} />
          {offer.providerName} • ⭐ {offer.rating}
        </span>
        <span className="offer-eta">{offer.deliveryOrServiceTime}</span>
      </div>

      {offer.matchReason && (
        <div className="decision" dir="auto">
          ⚡ AI Decision: {offer.matchReason}
        </div>
      )}

      {intent.alternativeOffers.length > 0 && (
        <>
          <div className="alt-toggle" onClick={() => setShowAlternatives((v) => !v)}>
            {showAlternatives
              ? 'Hide Alternatives'
              : `Compare with alternative (${intent.alternativeOffers[0].providerName})`}
            <Icon name={showAlternatives ? 'keyboard_arrow_up' : 'keyboard_arrow_down'} size={14} />
          </div>

          {showAlternatives &&
            intent.alternativeOffers.map((alt) => (
              <div className="alt-item" key={alt.id}>
                <div>
                  <div style={{ fontWeight: 500 }}>
                    {alt.providerName} ({alt.currency}
                    {Math.round(alt.price)})
                  </div>
                  <div className="muted" style={{ fontSize: 10 }}>
                    {alt.deliveryOrServiceTime}
                  </div>
                </div>
                <button
                  type="button"
                  className="btn text"
                  data-testid={`select-alt-${alt.id}`}
                  onClick={() => {
                    onSwitchOffer(alt)
                    setShowAlternatives(false)
                  }}
                >
                  Select
                </button>
              </div>
            ))}
        </>
      )}
    </div>
  )
}
