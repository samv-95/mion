import type {
  ChatMessage,
  DetectedIntent,
  FulfillmentPlan,
  Offer,
  SavedLocation,
} from './models'

// ---------------------------------------------------------------------------
// AI Orchestrator Core Intent Processor
// Line-for-line port of EverydayAssistantRepository.orchestratePrompt()
// ---------------------------------------------------------------------------

const locate = (locations: SavedLocation[], ...needles: string[]): SavedLocation =>
  locations.find((l) => needles.some((n) => l.label.includes(n))) ?? locations[0]

export function orchestratePrompt(
  prompt: string,
  locations: SavedLocation[],
  now: () => string,
  id: () => string,
): ChatMessage {
  const lower = prompt.toLowerCase()
  const timeNow = now()

  // 1. Keyword detection over the natural-language request (Persian + English)
  const hasHardDrive =
    lower.includes('هارد') || lower.includes('hard drive') || lower.includes('ssd') || lower.includes('هارد دیسک')
  const hasFlower = lower.includes('گل') || lower.includes('flower') || lower.includes('bouquet')
  const hasACService =
    lower.includes('کولر') || lower.includes('ac') || lower.includes('سرویس کولر') || lower.includes('hvac')
  const hasTaxi =
    lower.includes('تاکسی') || lower.includes('taxi') || lower.includes('ماشین') || lower.includes('اسنپ') || lower.includes('ride')

  const detectedIntents: DetectedIntent[] = []
  const usedContext: string[] = []

  if (hasHardDrive) {
    const officeLoc = locate(locations, 'Office', 'شرکت')
    usedContext.push(`Context: Company Office (${officeLoc.address})`)
    usedContext.push('Asset: Workstations Storage Needs')

    const bestOffer: Offer = {
      id: 'offer-ssd-samsung',
      title: 'Samsung 870 EVO 1TB SATA SSD (x4 Pack)',
      providerName: 'TechDirect MegaStore',
      providerType: 'VERIFIED_SELLER',
      price: 380.0,
      currency: '$',
      rating: 4.9,
      reviewsCount: 428,
      deliveryOrServiceTime: '2-Hour Express Courier',
      warrantyInfo: '24 Months Official Guarantee',
      distanceKm: 4.2,
      isBestMatch: true,
      matchReason: 'Lowest price with official 24m warranty & 2h instant dispatch to Company Office',
      badges: ['Best Match', 'Fast Delivery', '24M Warranty'],
    }

    const altOffer: Offer = {
      id: 'offer-wd-blue',
      title: 'WD Blue 1TB SATA 7200RPM (x4 Pack)',
      providerName: 'Silicon Valley Supplies',
      providerType: 'VERIFIED_SELLER',
      price: 272.0,
      currency: '$',
      rating: 4.6,
      reviewsCount: 190,
      deliveryOrServiceTime: '4-Hour Courier',
      warrantyInfo: '12 Months Warranty',
      distanceKm: 7.1,
      isBestMatch: false,
      matchReason: 'Budget alternative with mechanical HDD storage',
      badges: [],
    }

    detectedIntents.push({
      id: 'intent-hard-drives',
      rawQueryPart: '۴ تا هارد برای شرکت',
      intentType: 'PRODUCT_ORDER',
      targetItemOrService: '4x Samsung 870 EVO 1TB SSD',
      quantity: 4,
      resolvedDestinationLabel: officeLoc.label,
      resolvedDestinationAddress: officeLoc.address,
      scheduledTime: 'Delivery by 14:00 (Today)',
      selectedOffer: bestOffer,
      alternativeOffers: [altOffer],
      statusText: 'Orchestrated',
    })
  }

  if (hasFlower) {
    const homeLoc = locate(locations, 'Home', 'خونه')
    usedContext.push(`Context: Home Address (${homeLoc.address})`)

    const bestFlower: Offer = {
      id: 'offer-flower-lily',
      title: 'White Lily & Peony Artisan Bouquet',
      providerName: 'Bloom Atelier Florist',
      providerType: 'VERIFIED_SELLER',
      price: 45.0,
      currency: '$',
      rating: 4.8,
      reviewsCount: 112,
      deliveryOrServiceTime: 'Arrives by 16:00',
      warrantyInfo: 'Freshness Guaranteed (48h)',
      distanceKm: 2.8,
      isBestMatch: true,
      matchReason: 'Top rated florist 2.8km from Home, temperature-controlled delivery',
      badges: ['Top Rated Florist', 'Fresh Delivery'],
    }

    detectedIntents.push({
      id: 'intent-flower-home',
      rawQueryPart: 'گل برای خونه',
      intentType: 'PRODUCT_ORDER',
      targetItemOrService: 'White Lily & Peony Bouquet',
      quantity: 1,
      resolvedDestinationLabel: homeLoc.label,
      resolvedDestinationAddress: homeLoc.address,
      scheduledTime: 'Delivery by 16:00 (Today)',
      selectedOffer: bestFlower,
      alternativeOffers: [],
      statusText: 'Orchestrated',
    })
  }

  if (hasACService) {
    const homeLoc = locate(locations, 'Home', 'خونه')
    usedContext.push('Asset: Daikin Inverter AC (24,000 BTU)')

    const bestHvac: Offer = {
      id: 'offer-hvac-daikin',
      title: 'Daikin Certified AC Maintenance & Deep Clean',
      providerName: 'HVAC Master Specialists',
      providerType: 'SERVICE_SPECIALIST',
      price: 65.0,
      currency: '$',
      rating: 4.9,
      reviewsCount: 310,
      deliveryOrServiceTime: 'Wednesday, 14:00 - 15:30',
      warrantyInfo: '90-day Service Guarantee',
      distanceKm: 3.5,
      isBestMatch: true,
      matchReason: 'Official Daikin certification, matched with registered living room split unit',
      badges: ['Daikin Certified', '90-Day Guarantee'],
    }

    detectedIntents.push({
      id: 'intent-ac-service',
      rawQueryPart: 'چهارشنبه کولر رو سرویس کن',
      intentType: 'SERVICE_BOOKING',
      targetItemOrService: 'Daikin AC Full Tune-up & Filter Sanitization',
      quantity: 1,
      resolvedDestinationLabel: homeLoc.label,
      resolvedDestinationAddress: homeLoc.address,
      scheduledTime: 'Wednesday, 14:00',
      selectedOffer: bestHvac,
      alternativeOffers: [],
      statusText: 'Orchestrated',
    })
  }

  if (hasTaxi) {
    const officeLoc = locate(locations, 'Office', 'شرکت')
    const homeLoc = locate(locations, 'Home', 'خونه')
    usedContext.push('Mobility API: Scheduled Comfort Taxi')

    const taxiOffer: Offer = {
      id: 'offer-taxi-comfort',
      title: 'Comfort Sedan (Electric VIP)',
      providerName: 'SwiftMobility API',
      providerType: 'MOBILITY_API',
      price: 18.0,
      currency: '$',
      rating: 4.95,
      reviewsCount: 1420,
      deliveryOrServiceTime: 'Pickup at 18:00 (Office -> Home)',
      warrantyInfo: 'Live GPS & Fixed Price Guarantee',
      distanceKm: 11.4,
      isBestMatch: true,
      matchReason: 'Direct pickup from TechCorp Tower at 18:00 to Home with priority lane routing',
      badges: ['Fixed Fare', 'VIP Comfort'],
    }

    detectedIntents.push({
      id: 'intent-taxi-ride',
      rawQueryPart: 'برای ساعت ۶ هم تاکسی بگیر',
      intentType: 'TAXI_RIDE',
      targetItemOrService: 'Executive Ride (TechCorp -> Home)',
      quantity: 1,
      resolvedDestinationLabel: 'Pickup: ' + officeLoc.label,
      resolvedDestinationAddress: 'Dropoff: ' + homeLoc.address,
      scheduledTime: 'Today, 18:00 sharp',
      selectedOffer: taxiOffer,
      alternativeOffers: [],
      statusText: 'Orchestrated',
    })
  }

  // Tailored handling for asset-status / personal-shopping / free-form requests
  if (detectedIntents.length === 0) {
    if (
      lower.includes('ماشین') ||
      lower.includes('خودرو') ||
      lower.includes('car') ||
      lower.includes('bmw')
    ) {
      usedContext.push('Asset: BMW 320i (14,850 km)')
      const serviceOffer: Offer = {
        id: 'offer-bmw-spec',
        title: 'BMW Fast-Lane Inspection & Oil Service',
        providerName: 'Bavaria Auto Center (Authorized)',
        providerType: 'SERVICE_SPECIALIST',
        price: 120.0,
        currency: '$',
        rating: 4.9,
        reviewsCount: 280,
        deliveryOrServiceTime: 'Tomorrow 10:00 AM (2.1 km from Office)',
        warrantyInfo: 'Genuine BMW Parts & Warranty',
        distanceKm: 2.1,
        isBestMatch: true,
        matchReason:
          'Closest certified BMW center to your office; fits your 15,000 km service schedule',
        badges: [],
      }
      detectedIntents.push({
        id: 'intent-car-maintenance',
        rawQueryPart: prompt,
        intentType: 'HARDWARE_MAINTENANCE',
        targetItemOrService: 'BMW 320i 15,000 km Service Check',
        quantity: 1,
        resolvedDestinationLabel: 'Bavaria Auto Center',
        resolvedDestinationAddress: '2.1 km from TechCorp Tower',
        scheduledTime: 'Tomorrow, 10:00 AM',
        selectedOffer: serviceOffer,
        alternativeOffers: [],
        statusText: 'Orchestrated',
      })
    } else if (
      lower.includes('گوشی') ||
      lower.includes('phone') ||
      lower.includes('mobile') ||
      lower.includes('پدر')
    ) {
      const parentsLoc = locate(locations, 'Parents', 'پدر')
      usedContext.push("Context: Parents' Home Address")
      usedContext.push('Preference: Fast Delivery & High Rating')

      const phoneOffer: Offer = {
        id: 'offer-phone-father',
        title: 'Samsung Galaxy A55 5G (128GB - Awesome Navy)',
        providerName: 'Arya Digital Center',
        providerType: 'VERIFIED_SELLER',
        price: 390.0,
        currency: '$',
        rating: 4.85,
        reviewsCount: 512,
        deliveryOrServiceTime: 'Tomorrow Morning Express',
        warrantyInfo: '18 Months Official Guarantee',
        distanceKm: 5.0,
        isBestMatch: true,
        matchReason:
          'Large legible AMOLED display, durable battery, within budget, with official 18m warranty and tomorrow delivery.',
        badges: ['Best Match for Parents', 'Official 18M Warranty'],
      }

      detectedIntents.push({
        id: 'intent-phone-father',
        rawQueryPart: prompt,
        intentType: 'PRODUCT_ORDER',
        targetItemOrService: 'Samsung Galaxy A55 5G (for Parents)',
        quantity: 1,
        resolvedDestinationLabel: parentsLoc.label,
        resolvedDestinationAddress: parentsLoc.address,
        scheduledTime: 'Tomorrow by 11:00 AM',
        selectedOffer: phoneOffer,
        alternativeOffers: [],
        statusText: 'Orchestrated',
      })
    } else {
      // Dynamic fallback for any general query
      usedContext.push('AI Orchestrator: Multi-Source Engine')
      const genericOffer: Offer = {
        id: 'offer-generic-dynamic',
        title: 'Express Service Fulfillment',
        providerName: 'Aura Verified Network',
        providerType: 'VERIFIED_SELLER',
        price: 45.0,
        currency: '$',
        rating: 4.9,
        reviewsCount: 88,
        deliveryOrServiceTime: 'Today, 17:00',
        warrantyInfo: 'Standard Satisfaction Guarantee',
        distanceKm: 3.0,
        isBestMatch: true,
        matchReason: 'Optimized based on real-time provider availability in your area',
        badges: [],
      }
      detectedIntents.push({
        id: 'intent-generic-' + Date.now(),
        rawQueryPart: prompt,
        intentType: 'GENERAL_QUERY',
        targetItemOrService: `Fulfillment of: ${prompt}`,
        quantity: 1,
        resolvedDestinationLabel: 'Primary Location',
        resolvedDestinationAddress: locations[0].address,
        scheduledTime: 'Today, 17:00',
        selectedOffer: genericOffer,
        alternativeOffers: [],
        statusText: 'Orchestrated',
      })
    }
  }

  // Create unified Fulfillment Plan
  const totalCost = detectedIntents.reduce((sum, i) => sum + i.selectedOffer.price, 0)
  const plan: FulfillmentPlan = {
    id: 'plan-' + id().slice(0, 8),
    planCode: 'AUR-' + (1000 + Math.floor(Math.random() * 9000)),
    createdAt: timeNow,
    intents: detectedIntents,
    totalCost,
    currency: '$',
    status: 'DRAFT_PROPOSED',
    estimatedCompletionTime: hasTaxi
      ? 'Today, 18:30 (Final Taxi Dropoff)'
      : 'Scheduled Fulfillment',
  }

  // Human-readable orchestration summary shown in the chat bubble
  const typeLabel: Record<DetectedIntent['intentType'], string> = {
    PRODUCT_ORDER: '📦 خرید کالا',
    SERVICE_BOOKING: '🔧 رزرو خدمات',
    TAXI_RIDE: '🚖 درخواست تاکسی',
    COURIER_DELIVERY: '🚚 پیک و باربری',
    HARDWARE_MAINTENANCE: '⚙️ نگهداری دارایی',
    GENERAL_QUERY: '⚡ هماهنگی روزمره',
  }

  let messageText = `درخواست شما تحلیل شد و ${detectedIntents.length} بخش اصلی استخراج گردید. تمام هماهنگی‌ها با فروشندگان، متخصصان خدمات و سرویس‌های خارجی انجام شده است:\n\n`
  detectedIntents.forEach((intent, index) => {
    messageText += `${index + 1}. ${typeLabel[intent.intentType]}: **${intent.targetItemOrService}**\n`
    messageText += `   • تأمین‌کننده: ${intent.selectedOffer.providerName} (${intent.selectedOffer.providerType.replace('_', ' ')})\n`
    messageText += `   • مقصد: ${intent.resolvedDestinationLabel}\n`
    messageText += `   • زمان/ارسال: ${intent.selectedOffer.deliveryOrServiceTime}\n`
    messageText += `   • هزینه: ${intent.selectedOffer.currency}${Math.round(intent.selectedOffer.price)}\n\n`
  })
  messageText += `برنامه تجمیعی (Fulfillment Plan) با مجموع هزینه $${Math.round(totalCost)} آماده تأیید است. نیازی به انتخاب جداگانه ندارید؛ برای بررسی مسیرها دکمه نقشه عملیاتی را بزنید یا مستقیماً تأیید کنید.`

  return {
    id: id(),
    isUser: false,
    message: messageText,
    timestamp: timeNow,
    detectedIntents,
    fulfillmentPlan: plan,
    usedContextTags: usedContext,
  }
}
