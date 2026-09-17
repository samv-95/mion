import type {
  PrivacySettings,
  ProactiveAlert,
  SavedLocation,
  SellerProduct,
  ServiceProviderListing,
  UserAsset,
  UserPreference,
} from './models'

// ---------------------------------------------------------------------------
// Port of the seed data of EverydayAssistantRepository.kt (sections 1 - 8)
// ---------------------------------------------------------------------------

export const SEED_ASSETS: UserAsset[] = [
  {
    id: 'asset-car-1',
    name: 'BMW 320i (2022)',
    category: 'VEHICLE',
    specification: '2.0L Turbo / Vin: WBA33AY098',
    registrationOrSerial: '77B-4921',
    statusNote: 'Routine Service due at 15,000 km (Current: 14,850 km)',
    lastServiceInfo: '6 months ago (Oil & Brake Pads)',
    mileageOrUsage: '14,850 km',
  },
  {
    id: 'asset-laptop-2',
    name: 'MacBook Pro 16" (M3 Max)',
    category: 'COMPUTING',
    specification: '36GB RAM / 1TB SSD / Thunderbolt 4',
    registrationOrSerial: 'C02G99XZMD',
    statusNote: 'Storage 88% full; backup recommended',
    lastServiceInfo: 'Purchased Nov 2023',
    mileageOrUsage: 'Battery Cycle: 42',
  },
  {
    id: 'asset-ac-3',
    name: 'Daikin Inverter AC (24,000 BTU)',
    category: 'HOME_APPLIANCE',
    specification: 'Dual-cool Split Inverter / Living Room',
    registrationOrSerial: 'DK-INV-88210',
    statusNote: 'Summer pre-season filter & refrigerant check recommended',
    lastServiceInfo: 'May 2025',
    mileageOrUsage: 'Active 1400 hrs',
  },
  {
    id: 'asset-office-4',
    name: 'TechCorp Studio Workstations (x4)',
    category: 'OFFICE_EQUIPMENT',
    specification: 'Dual Display Workstations / Server rack expansion ready',
    registrationOrSerial: 'TC-ST-2024',
    statusNote: 'Needs 4x high-speed NVMe/SATA storage drives',
    lastServiceInfo: 'Deployed Jan 2024',
    mileageOrUsage: '24/7 Operations',
  },
]

export const SEED_LOCATIONS: SavedLocation[] = [
  {
    id: 'loc-office',
    label: 'Company / Office',
    address: 'TechCorp Tower, 7th Floor, Innovation Blvd',
    latitude: 35.7219,
    longitude: 51.3347,
    isDefault: false,
  },
  {
    id: 'loc-home',
    label: 'Home',
    address: 'Apartment 4B, 18 Maple Ave, North Heights',
    latitude: 35.765,
    longitude: 51.412,
    isDefault: true,
  },
  {
    id: 'loc-parents',
    label: "Parents' Home",
    address: 'No. 12, Cedar Grove Court, Central District',
    latitude: 35.698,
    longitude: 51.385,
    isDefault: false,
  },
]

export const SEED_ALERTS: ProactiveAlert[] = [
  {
    id: 'alert-bmw-service',
    title: 'Vehicle Maintenance Nearing',
    message:
      'Your BMW 320i has reached 14,850 km (interval: 15,000 km). 2 certified specialists near your office have available slots today at 16:30 and tomorrow morning.',
    category: 'Vehicle Health',
    suggestedPrompt: 'Book car service near my office for tomorrow morning',
    linkedAssetId: 'asset-car-1',
    timestamp: 'Just now',
    isDismissed: false,
  },
  {
    id: 'alert-ac-check',
    title: 'Pre-Summer HVAC Optimization',
    message:
      'Seasonal tune-up recommended for your Daikin Inverter AC to prevent peak summer efficiency loss.',
    category: 'Home Maintenance',
    suggestedPrompt: 'چهارشنبه کولر رو سرویس کن',
    linkedAssetId: 'asset-ac-3',
    timestamp: 'Just now',
    isDismissed: false,
  },
]

export const SEED_SELLER_PRODUCTS: SellerProduct[] = [
  { id: 'sp-1', title: 'Samsung 870 EVO 1TB SATA SSD', category: 'Computer Hardware', unitPrice: 95.0, stockCount: 34, warrantyMonths: 24, sellerRating: 4.9, dispatchWindowHours: 2 },
  { id: 'sp-2', title: 'WD Blue 1TB SATA Hard Drive', category: 'Computer Hardware', unitPrice: 68.0, stockCount: 50, warrantyMonths: 12, sellerRating: 4.6, dispatchWindowHours: 4 },
  { id: 'sp-3', title: 'White Lily & Peony Floral Arrangement', category: 'Florist & Gift', unitPrice: 45.0, stockCount: 18, warrantyMonths: 0, sellerRating: 4.8, dispatchWindowHours: 2 },
  { id: 'sp-4', title: 'Artisan Rose & Hydrangea Box', category: 'Florist & Gift', unitPrice: 60.0, stockCount: 12, warrantyMonths: 0, sellerRating: 4.9, dispatchWindowHours: 3 },
  { id: 'sp-5', title: 'Thunderbolt 4 Dual Display Dock', category: 'Computer Accessories', unitPrice: 185.0, stockCount: 8, warrantyMonths: 24, sellerRating: 4.7, dispatchWindowHours: 2 },
  { id: 'sp-6', title: 'Ergonomic Mechanical Keyboard (Silent)', category: 'Office Tech', unitPrice: 110.0, stockCount: 15, warrantyMonths: 12, sellerRating: 4.8, dispatchWindowHours: 3 },
]

export const SEED_SERVICE_LISTINGS: ServiceProviderListing[] = [
  { id: 'srv-1', serviceName: 'Daikin & Split AC Deep Clean & Refrigerant Check', basePrice: 65.0, durationMinutes: 75, availableSlots: ['Wed 14:00', 'Wed 16:30', 'Thu 11:00'], rating: 4.9, coverageRadiusKm: 15.0 },
  { id: 'srv-2', serviceName: 'General HVAC Inspection & Filter Replacement', basePrice: 50.0, durationMinutes: 60, availableSlots: ['Wed 10:00', 'Wed 13:00'], rating: 4.5, coverageRadiusKm: 10.0 },
  { id: 'srv-3', serviceName: 'BMW Certified Brake & Fluid Inspection', basePrice: 120.0, durationMinutes: 90, availableSlots: ['Tomorrow 09:30', 'Tomorrow 14:00'], rating: 4.9, coverageRadiusKm: 20.0 },
  { id: 'srv-4', serviceName: 'Office Network & Cable Infrastructure Tuning', basePrice: 80.0, durationMinutes: 120, availableSlots: ['Fri 10:00'], rating: 4.8, coverageRadiusKm: 25.0 },
]

export const DEFAULT_USER_PREFERENCE: UserPreference = {
  favoriteBrands: ['Samsung', 'Apple', 'Daikin', 'Bosch'],
  prioritizeFastDelivery: true,
  requireOfficialWarranty: true,
  autoApproveUnderBudget: false,
  defaultPaymentMethod: 'Corporate Wallet (Active)',
}

export const DEFAULT_PRIVACY_SETTINGS: PrivacySettings = {
  enableContextAwareness: true,
  showContextAttributionTags: true,
  shareAnonymizedDetailsWithSellers: true,
  allowProactiveMaintenanceReminders: true,
}

export const SAMPLE_PROMPTS: string[] = [
  'برای شرکت ۴ تا هارد میخوام، برای خونه گل، چهارشنبه کولر رو سرویس کن و ساعت ۶ تاکسی بگیر',
  'یه گوشی خوب برای پدرم میخوام، بودجهم ۳۰ میلیون بیشتر نباشه و فردا برسه',
  'وضعیت سرویس ماشینم چطوره و نزدیک‌ترین تعمیرگاه کجاست؟',
  'این بسته رو امروز برسون شرکت',
  'یه ماشین برای ساعت ۷ صبح به سمت شرکت میخوام',
]

export const ICONIC_PROMPT =
  'برای شرکت ۴ تا هارد میخوام، برای خونه هم گل سفارش بده، چهارشنبه کولر رو سرویس کن و برای ساعت ۶ هم تاکسی بگیر.'
