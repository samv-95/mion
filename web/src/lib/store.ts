import { useSyncExternalStore } from 'react'
import { orchestratePrompt } from './orchestrator'
import {
  DEFAULT_PRIVACY_SETTINGS,
  DEFAULT_USER_PREFERENCE,
  SEED_ALERTS,
  SEED_ASSETS,
  SEED_LOCATIONS,
  SEED_SELLER_PRODUCTS,
  SEED_SERVICE_LISTINGS,
} from './seed'
import type {
  AppNavTab,
  AssetCategory,
  ChatMessage,
  FulfillmentPlan,
  Offer,
  PrivacySettings,
  ProactiveAlert,
  SavedLocation,
  SellerProduct,
  ServiceProviderListing,
  UserAsset,
  UserPreference,
} from './models'

export interface AppState {
  messages: ChatMessage[]
  fulfillmentPlans: FulfillmentPlan[]
  assets: UserAsset[]
  locations: SavedLocation[]
  userPreference: UserPreference
  privacySettings: PrivacySettings
  proactiveAlerts: ProactiveAlert[]
  sellerProducts: SellerProduct[]
  serviceListings: ServiceProviderListing[]
  currentTab: AppNavTab
  isAiTyping: boolean
}

type Listener = () => void

const uuid = () =>
  typeof crypto !== 'undefined' && 'randomUUID' in crypto
    ? crypto.randomUUID()
    : Math.random().toString(36).slice(2) + Date.now().toString(36)

const nowHHmm = () => {
  const d = new Date()
  return `${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}


function initialState(): AppState {
  return {
    messages: [
      {
        id: 'init-welcome',
        isUser: false,
        message:
          'سلام! من دستیار هوشمند شما هستم. نیازی نیست بین اپلیکیشن‌های مختلف بگردید؛ هر کاری دارید را به زبان طبیعی بگویید — از خرید سخت‌افزار تا سفارش گل، رزرو خدمات یا درخواست خودرو.',
        timestamp: nowHHmm(),
        detectedIntents: [],
        usedContextTags: ['Assets: 4 items', 'Locations: 3 saved', 'Privacy Shield Active'],
      },
    ],
    fulfillmentPlans: [],
    assets: [...SEED_ASSETS],
    locations: [...SEED_LOCATIONS],
    userPreference: { ...DEFAULT_USER_PREFERENCE },
    privacySettings: { ...DEFAULT_PRIVACY_SETTINGS },
    proactiveAlerts: SEED_ALERTS.map((a) => ({ ...a })),
    sellerProducts: [...SEED_SELLER_PRODUCTS],
    serviceListings: [...SEED_SERVICE_LISTINGS],
    currentTab: 'CHAT',
    isAiTyping: false,
  }
}

export class EverydayAssistantRepository {
  private state: AppState
  private listeners = new Set<Listener>()

  constructor() {
    this.state = initialState()
  }

  reset() {
    this.set(initialState())
  }

  getSnapshot = (): AppState => this.state

  subscribe = (listener: Listener): (() => void) => {
    this.listeners.add(listener)
    return () => this.listeners.delete(listener)
  }

  private set(patch: Partial<AppState>) {
    this.state = { ...this.state, ...patch }
    this.listeners.forEach((l) => l())
  }

  // --- Navigation ----------------------------------------------------------
  selectTab(tab: AppNavTab) {
    this.set({ currentTab: tab })
  }

  // --- Chat / Orchestration ------------------------------------------------
  sendPrompt(prompt: string) {
    if (!prompt.trim()) return
    this.set({ isAiTyping: true })
    // Realistic orchestrator thinking delay (matches the ViewModel's 500 ms)
    window.setTimeout(() => {
      const userMsg: ChatMessage = {
        id: uuid(),
        isUser: true,
        message: prompt,
        timestamp: nowHHmm(),
        detectedIntents: [],
        usedContextTags: [],
      }
      const response = orchestratePrompt(prompt, this.state.locations, nowHHmm, uuid)
      const plans = response.fulfillmentPlan
        ? [response.fulfillmentPlan, ...this.state.fulfillmentPlans]
        : this.state.fulfillmentPlans
      this.set({
        messages: [...this.state.messages, userMsg, response],
        fulfillmentPlans: plans,
        isAiTyping: false,
      })
    }, 500)
  }

  confirmFulfillmentPlan(planId: string) {
    const plans = [...this.state.fulfillmentPlans]
    const index = plans.findIndex((p) => p.id === planId)
    if (index === -1) return
    const updated = { ...plans[index], status: 'IN_FULFILLMENT' as const }
    plans[index] = updated

    const messages = this.state.messages.map((m) =>
      m.fulfillmentPlan?.id === planId ? { ...m, fulfillmentPlan: updated } : m,
    )

    messages.push({
      id: uuid(),
      isUser: false,
      message:
        '✅ برنامه با موفقیت تأیید شد و پرداخت امن انجام گرفت! کلیه سفارش‌ها وارد فاز عملیاتی شدند. می‌توانید وضعیت لحظه‌ای پیک‌ها، مسیرها و رزرو را در نقشه عملیاتی دنبال کنید.',
      timestamp: nowHHmm(),
      detectedIntents: [],
      usedContextTags: ['Automated Payment: Corporate Wallet', 'Courier Live Tracking Assigned'],
    })

    this.set({ fulfillmentPlans: plans, messages })
  }

  switchOffer(planId: string, intentId: string, newOffer: Offer) {
    const plans = [...this.state.fulfillmentPlans]
    const planIndex = plans.findIndex((p) => p.id === planId)
    if (planIndex === -1) return
    const plan = plans[planIndex]
    const intents = plan.intents.map((intent) => {
      if (intent.id !== intentId) return intent
      const alternatives = [...intent.alternativeOffers, intent.selectedOffer].filter(
        (o) => o.id !== newOffer.id,
      )
      return { ...intent, selectedOffer: newOffer, alternativeOffers: alternatives }
    })
    const updatedPlan: FulfillmentPlan = {
      ...plan,
      intents,
      totalCost: intents.reduce((sum, i) => sum + i.selectedOffer.price, 0),
    }
    plans[planIndex] = updatedPlan
    this.set({
      fulfillmentPlans: plans,
      messages: this.state.messages.map((m) =>
        m.fulfillmentPlan?.id === planId ? { ...m, fulfillmentPlan: updatedPlan } : m,
      ),
    })
  }

  dismissProactiveAlert(alertId: string) {
    this.set({ proactiveAlerts: this.state.proactiveAlerts.filter((a) => a.id !== alertId) })
  }

  // --- Context -------------------------------------------------------------
  addAsset(name: string, category: AssetCategory, specs: string, reg: string, status: string) {
    const asset: UserAsset = {
      id: 'asset-' + Date.now(),
      name,
      category,
      specification: specs,
      registrationOrSerial: reg,
      statusNote: status,
      lastServiceInfo: '',
      mileageOrUsage: '',
    }
    this.set({ assets: [...this.state.assets, asset] })
  }

  removeAsset(assetId: string) {
    this.set({ assets: this.state.assets.filter((a) => a.id !== assetId) })
  }

  addLocation(label: string, address: string) {
    const loc: SavedLocation = {
      id: 'loc-' + Date.now(),
      label,
      address,
      latitude: 35.73,
      longitude: 51.35,
      isDefault: false,
    }
    this.set({ locations: [...this.state.locations, loc] })
  }

  updatePrivacySettings(settings: PrivacySettings) {
    this.set({ privacySettings: settings })
  }

  updateUserPreference(preference: UserPreference) {
    this.set({ userPreference: preference })
  }

  // --- Partner portal ------------------------------------------------------
  addSellerProduct(title: string, category: string, price: number, stock: number, warranty: number) {
    const item: SellerProduct = {
      id: 'sp-' + Date.now(),
      title,
      category,
      unitPrice: price,
      stockCount: stock,
      warrantyMonths: warranty,
      sellerRating: 4.9,
      dispatchWindowHours: 2,
    }
    this.set({ sellerProducts: [item, ...this.state.sellerProducts] })
  }

  addServiceListing(name: string, price: number, duration: number, slot: string) {
    const item: ServiceProviderListing = {
      id: 'srv-' + Date.now(),
      serviceName: name,
      basePrice: price,
      durationMinutes: duration,
      availableSlots: [slot],
      rating: 4.9,
      coverageRadiusKm: 20.0,
    }
    this.set({ serviceListings: [item, ...this.state.serviceListings] })
  }
}

export const repository = new EverydayAssistantRepository()

/** Single global store subscription — mirrors the shared ViewModel of the app. */
export function useAppState(): AppState {
  return useSyncExternalStore(repository.subscribe, repository.getSnapshot)
}

/** Restores the seeded demo state (used by tests / the "reset demo" control). */
export function resetStore() {
  repository.reset()
}
