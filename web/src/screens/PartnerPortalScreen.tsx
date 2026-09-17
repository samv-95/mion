import { useState } from 'react'
import { AuraBadge, AuraCard, Modal } from '../components/Common'
import { Icon } from '../components/Icon'
import { repository, useAppState } from '../lib/store'

export function PartnerPortalScreen() {
  const { sellerProducts, serviceListings } = useAppState()
  const [role, setRole] = useState<'SELLER' | 'SERVICE_PROVIDER'>('SELLER')
  const [showProductDialog, setShowProductDialog] = useState(false)
  const [showServiceDialog, setShowServiceDialog] = useState(false)

  return (
    <div className="screen">
      <div className="pad-top" />
      <div className="row-between">
        <div>
          <h2 className="screen-title">PARTNER CONSOLE</h2>
          <p className="screen-subtitle">Merchant &amp; Service Ecosystem Hub</p>
        </div>
        <AuraBadge text="Verified Merchant" highlight />
      </div>

      <div style={{ height: 14 }} />

      <div className="segment">
        <button
          type="button"
          className={role === 'SELLER' ? 'active' : ''}
          onClick={() => setRole('SELLER')}
        >
          Seller / Retail Hub
        </button>
        <button
          type="button"
          className={role === 'SERVICE_PROVIDER' ? 'active' : ''}
          onClick={() => setRole('SERVICE_PROVIDER')}
        >
          Service Specialists
        </button>
      </div>

      <div style={{ height: 16 }} />
      <hr className="divider" />
      <div style={{ height: 14 }} />

      {role === 'SELLER' ? (
        <>
          <div className="section-head">
            <span className="section-title">Live Catalog ({sellerProducts.length} Items)</span>
            <button
              type="button"
              className="btn text"
              onClick={() => setShowProductDialog(true)}
            >
              <Icon name="add" size={16} />
              Add Product
            </button>
          </div>

          <div className="scroll-area list tight" style={{ marginTop: 12 }}>
            {sellerProducts.map((product) => (
              <AuraCard key={product.id} background="var(--deep-charcoal)">
                <div className="row-between" style={{ alignItems: 'flex-start' }}>
                  <div style={{ flex: '1 1 auto' }}>
                    <div style={{ fontSize: 13, fontWeight: 700 }}>{product.title}</div>
                    <div className="muted" style={{ fontSize: 11 }}>
                      {product.category} • Dispatch window: {product.dispatchWindowHours}h
                    </div>
                  </div>
                  <span style={{ fontSize: 14, fontWeight: 700 }}>
                    ${Math.round(product.unitPrice)}
                  </span>
                </div>

                <div className="row-between" style={{ marginTop: 8 }}>
                  <span
                    style={{
                      fontSize: 11,
                      fontWeight: 500,
                      color: product.stockCount > 5 ? 'var(--green)' : 'var(--amber)',
                    }}
                  >
                    In Stock: {product.stockCount} units
                  </span>
                  <span className="muted" style={{ fontSize: 11 }}>
                    Warranty: {product.warrantyMonths} Months
                  </span>
                </div>
              </AuraCard>
            ))}
          </div>
        </>
      ) : (
        <>
          <div className="section-head">
            <span className="section-title">Active Services ({serviceListings.length})</span>
            <button
              type="button"
              className="btn text"
              onClick={() => setShowServiceDialog(true)}
            >
              <Icon name="add" size={16} />
              Add Service
            </button>
          </div>

          <div className="scroll-area list tight" style={{ marginTop: 12 }}>
            {serviceListings.map((service) => (
              <AuraCard key={service.id} background="var(--deep-charcoal)">
                <div className="row-between" style={{ alignItems: 'flex-start' }}>
                  <div style={{ flex: '1 1 auto' }}>
                    <div style={{ fontSize: 13, fontWeight: 700 }}>{service.serviceName}</div>
                    <div className="muted" style={{ fontSize: 11 }}>
                      Estimated Duration: {service.durationMinutes} mins • Radius:{' '}
                      {service.coverageRadiusKm}km
                    </div>
                  </div>
                  <span style={{ fontSize: 14, fontWeight: 700 }}>
                    ${Math.round(service.basePrice)}
                  </span>
                </div>

                <div
                  style={{
                    marginTop: 8,
                    fontSize: 11,
                    color: 'var(--blue-light)',
                    fontWeight: 500,
                  }}
                >
                  Next Available: {service.availableSlots.join(', ')}
                </div>
              </AuraCard>
            ))}
          </div>
        </>
      )}

      {showProductDialog && <AddProductDialog onDismiss={() => setShowProductDialog(false)} />}
      {showServiceDialog && <AddServiceDialog onDismiss={() => setShowServiceDialog(false)} />}
    </div>
  )
}

function AddProductDialog({ onDismiss }: { onDismiss: () => void }) {
  const [title, setTitle] = useState('')
  const [price, setPrice] = useState('')
  const [stock, setStock] = useState('')

  return (
    <Modal
      title="List New Product"
      confirmLabel="Publish to AI Engine"
      confirmDisabled={!title.trim()}
      onDismiss={onDismiss}
      onConfirm={() => {
        if (!title.trim()) return
        repository.addSellerProduct(
          title,
          'Electronics & Supplies',
          Number.parseFloat(price) || 50,
          Number.parseInt(stock, 10) || 10,
          12,
        )
        onDismiss()
      }}
    >
      <div className="field">
        <label htmlFor="p-title">Product Name</label>
        <input id="p-title" value={title} onChange={(e) => setTitle(e.target.value)} autoFocus />
      </div>
      <div className="field">
        <label htmlFor="p-price">Price ($)</label>
        <input id="p-price" value={price} onChange={(e) => setPrice(e.target.value)} />
      </div>
      <div className="field">
        <label htmlFor="p-stock">Stock Quantity</label>
        <input id="p-stock" value={stock} onChange={(e) => setStock(e.target.value)} />
      </div>
    </Modal>
  )
}

function AddServiceDialog({ onDismiss }: { onDismiss: () => void }) {
  const [name, setName] = useState('')
  const [price, setPrice] = useState('')

  return (
    <Modal
      title="Register Service"
      confirmLabel="Register Service"
      confirmDisabled={!name.trim()}
      onDismiss={onDismiss}
      onConfirm={() => {
        if (!name.trim()) return
        repository.addServiceListing(name, Number.parseFloat(price) || 60, 60, 'Tomorrow 14:00')
        onDismiss()
      }}
    >
      <div className="field">
        <label htmlFor="s-name">Service Title</label>
        <input id="s-name" value={name} onChange={(e) => setName(e.target.value)} autoFocus />
      </div>
      <div className="field">
        <label htmlFor="s-price">Base Price ($)</label>
        <input id="s-price" value={price} onChange={(e) => setPrice(e.target.value)} />
      </div>
    </Modal>
  )
}
