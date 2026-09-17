import { useState } from 'react'
import { AuraBadge, AuraCard, Modal, Toggle } from '../components/Common'
import { Icon, type IconName } from '../components/Icon'
import type { AssetCategory } from '../lib/models'
import { repository, useAppState } from '../lib/store'

const CATEGORY_ICON: Record<AssetCategory, IconName> = {
  VEHICLE: 'directions_car',
  COMPUTING: 'laptop_mac',
  HOME_APPLIANCE: 'ac_unit',
  MOBILE_DEVICE: 'smartphone',
  OFFICE_EQUIPMENT: 'dns',
}

export function ContextScreen() {
  const { assets, locations, privacySettings } = useAppState()
  const [showAddAsset, setShowAddAsset] = useState(false)
  const [assetName, setAssetName] = useState('')
  const [assetSpecs, setAssetSpecs] = useState('')
  const [assetCategory, setAssetCategory] = useState<AssetCategory>('HOME_APPLIANCE')

  const registerAsset = () => {
    if (!assetName.trim()) return
    repository.addAsset(
      assetName,
      assetCategory,
      assetSpecs.trim() || 'Registered Model',
      'AURA-REG',
      'Healthy / Monitored by AI',
    )
    setAssetName('')
    setAssetSpecs('')
    setShowAddAsset(false)
  }

  return (
    <div className="screen">
      <div className="pad-top" />
      <div className="row-between">
        <div>
          <h2 className="screen-title">CONTEXT &amp; PERSONALIZATION</h2>
          <p className="screen-subtitle">Explicit Knowledge Verified by You</p>
        </div>
        <AuraBadge text="Privacy Shield" highlight />
      </div>

      <div style={{ height: 16 }} />
      <hr className="divider" />
      <div style={{ height: 14 }} />

      <div className="scroll-area list">
        {/* Registered assets */}
        <div className="section-head">
          <span className="section-title">Registered Assets ({assets.length})</span>
          <button type="button" className="btn text" onClick={() => setShowAddAsset(true)}>
            <Icon name="add" size={16} />
            Add Asset
          </button>
        </div>

        {assets.map((asset) => (
          <AuraCard key={asset.id} background="var(--deep-charcoal)">
            <div className="row-between" style={{ alignItems: 'flex-start' }}>
              <div className="row" style={{ flex: '1 1 auto', alignItems: 'center' }}>
                <span className="asset-avatar">
                  <Icon name={CATEGORY_ICON[asset.category]} size={18} />
                </span>
                <div>
                  <div style={{ fontSize: 13, fontWeight: 600 }}>{asset.name}</div>
                  <div className="muted" style={{ fontSize: 11 }}>
                    {asset.specification}
                  </div>
                </div>
              </div>
              <button
                type="button"
                className="btn text muted"
                aria-label="Delete"
                onClick={() => repository.removeAsset(asset.id)}
              >
                <Icon name="delete" size={16} />
              </button>
            </div>
            <div className="note-box" dir="auto">
              💡 {asset.statusNote}
            </div>
          </AuraCard>
        ))}

        {/* Frequent locations */}
        <div style={{ marginTop: 8 }}>
          <span className="section-title">Frequent Locations</span>
        </div>

        {locations.map((loc) => (
          <AuraCard key={loc.id} background="var(--deep-charcoal)">
            <div className="row" style={{ alignItems: 'center' }}>
              <Icon name="location_on" size={18} />
              <div>
                <div style={{ fontSize: 13, fontWeight: 600 }}>{loc.label}</div>
                <div className="muted" style={{ fontSize: 11 }}>
                  {loc.address}
                </div>
              </div>
            </div>
          </AuraCard>
        ))}

        {/* Privacy & AI controls */}
        <div style={{ marginTop: 8 }}>
          <span className="section-title">Privacy &amp; Intelligence Shield</span>
        </div>

        <AuraCard background="var(--deep-charcoal)">
          <ToggleRow
            title="Context-Aware Orchestration"
            subtitle="Allows Aura to match registered devices, offices and vehicles with offers."
            checked={privacySettings.enableContextAwareness}
            onChange={(v) =>
              repository.updatePrivacySettings({ ...privacySettings, enableContextAwareness: v })
            }
          />
          <hr className="divider" style={{ margin: '10px 0' }} />
          <ToggleRow
            title="Context Attribution Tags"
            subtitle="Always displays which assets or addresses were used by AI for each decision."
            checked={privacySettings.showContextAttributionTags}
            onChange={(v) =>
              repository.updatePrivacySettings({
                ...privacySettings,
                showContextAttributionTags: v,
              })
            }
          />
          <hr className="divider" style={{ margin: '10px 0' }} />
          <ToggleRow
            title="Anonymize Seller Queries"
            subtitle="Merchants only receive delivery location and item specs without your personal profile."
            checked={privacySettings.shareAnonymizedDetailsWithSellers}
            onChange={(v) =>
              repository.updatePrivacySettings({
                ...privacySettings,
                shareAnonymizedDetailsWithSellers: v,
              })
            }
          />
        </AuraCard>
      </div>

      {showAddAsset && (
        <Modal
          title="Register New Asset"
          confirmLabel="Register Asset"
          confirmDisabled={!assetName.trim()}
          onConfirm={registerAsset}
          onDismiss={() => setShowAddAsset(false)}
        >
          <div className="field">
            <label htmlFor="asset-name">Asset Name (e.g. Bosch Dishwasher)</label>
            <input
              id="asset-name"
              value={assetName}
              onChange={(e) => setAssetName(e.target.value)}
              autoFocus
            />
          </div>
          <div className="field">
            <label htmlFor="asset-specs">Model or Specs</label>
            <input id="asset-specs" value={assetSpecs} onChange={(e) => setAssetSpecs(e.target.value)} />
          </div>
          <div className="field">
            <label htmlFor="asset-category">Category</label>
            <select
              id="asset-category"
              value={assetCategory}
              onChange={(e) => setAssetCategory(e.target.value as AssetCategory)}
            >
              <option value="VEHICLE">Vehicle</option>
              <option value="COMPUTING">Computing</option>
              <option value="HOME_APPLIANCE">Home Appliance</option>
              <option value="MOBILE_DEVICE">Mobile Device</option>
              <option value="OFFICE_EQUIPMENT">Office Equipment</option>
            </select>
          </div>
        </Modal>
      )}
    </div>
  )
}

function ToggleRow({
  title,
  subtitle,
  checked,
  onChange,
}: {
  title: string
  subtitle: string
  checked: boolean
  onChange: (v: boolean) => void
}) {
  return (
    <div className="row-between">
      <div style={{ flex: '1 1 auto' }}>
        <div style={{ fontSize: 13, fontWeight: 600 }}>{title}</div>
        <div className="muted" style={{ fontSize: 11 }}>
          {subtitle}
        </div>
      </div>
      <Toggle checked={checked} onChange={onChange} label={title} />
    </div>
  )
}
