// Direct port of app/src/main/java/com/example/model/*.kt

export type IntentType =
  | 'PRODUCT_ORDER'
  | 'SERVICE_BOOKING'
  | 'TAXI_RIDE'
  | 'COURIER_DELIVERY'
  | 'HARDWARE_MAINTENANCE'
  | 'GENERAL_QUERY'

export type OfferProviderType =
  | 'VERIFIED_SELLER'
  | 'SERVICE_SPECIALIST'
  | 'MOBILITY_API'
  | 'LOGISTICS_API'

export interface Offer {
  id: string
  title: string
  providerName: string
  providerType: OfferProviderType
  price: number
  currency: string
  rating: number
  reviewsCount: number
  deliveryOrServiceTime: string
  warrantyInfo: string
  distanceKm: number
  isBestMatch: boolean
  matchReason: string
  badges: string[]
}

export interface DetectedIntent {
  id: string
  rawQueryPart: string
  intentType: IntentType
  targetItemOrService: string
  quantity: number
  resolvedDestinationLabel: string
  resolvedDestinationAddress: string
  scheduledTime: string
  selectedOffer: Offer
  alternativeOffers: Offer[]
  statusText: string
}

export type PlanStatus = 'DRAFT_PROPOSED' | 'CONFIRMED_PAID' | 'IN_FULFILLMENT' | 'COMPLETED'

export interface FulfillmentPlan {
  id: string
  planCode: string
  createdAt: string
  intents: DetectedIntent[]
  totalCost: number
  currency: string
  status: PlanStatus
  estimatedCompletionTime: string
}

export interface ProactiveAlert {
  id: string
  title: string
  message: string
  category: string
  suggestedPrompt: string
  linkedAssetId?: string
  timestamp: string
  isDismissed: boolean
}

export interface ChatMessage {
  id: string
  isUser: boolean
  message: string
  timestamp: string
  detectedIntents: DetectedIntent[]
  fulfillmentPlan?: FulfillmentPlan
  usedContextTags: string[]
  proactiveAlert?: ProactiveAlert
}

export interface SellerProduct {
  id: string
  title: string
  category: string
  unitPrice: number
  stockCount: number
  warrantyMonths: number
  sellerRating: number
  dispatchWindowHours: number
}

export interface ServiceProviderListing {
  id: string
  serviceName: string
  basePrice: number
  durationMinutes: number
  availableSlots: string[]
  rating: number
  coverageRadiusKm: number
}

export type AssetCategory =
  | 'VEHICLE'
  | 'COMPUTING'
  | 'HOME_APPLIANCE'
  | 'MOBILE_DEVICE'
  | 'OFFICE_EQUIPMENT'

export interface UserAsset {
  id: string
  name: string
  category: AssetCategory
  specification: string
  registrationOrSerial: string
  statusNote: string
  lastServiceInfo: string
  mileageOrUsage: string
}

export interface SavedLocation {
  id: string
  label: string
  address: string
  latitude: number
  longitude: number
  isDefault: boolean
}

export interface UserPreference {
  favoriteBrands: string[]
  prioritizeFastDelivery: boolean
  requireOfficialWarranty: boolean
  autoApproveUnderBudget: boolean
  defaultPaymentMethod: string
}

export interface PrivacySettings {
  enableContextAwareness: boolean
  showContextAttributionTags: boolean
  shareAnonymizedDetailsWithSellers: boolean
  allowProactiveMaintenanceReminders: boolean
}

export type AppNavTab = 'CHAT' | 'ORDERS' | 'MAP' | 'CONTEXT' | 'PARTNER'
