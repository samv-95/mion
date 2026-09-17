package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class EverydayAssistantRepository {

    // 1. Context: User Registered Assets
    private val _assets = MutableStateFlow<List<UserAsset>>(
        listOf(
            UserAsset(
                id = "asset-car-1",
                name = "BMW 320i (2022)",
                category = AssetCategory.VEHICLE,
                specification = "2.0L Turbo / Vin: WBA33AY098",
                registrationOrSerial = "77B-4921",
                statusNote = "Routine Service due at 15,000 km (Current: 14,850 km)",
                lastServiceInfo = "6 months ago (Oil & Brake Pads)",
                mileageOrUsage = "14,850 km"
            ),
            UserAsset(
                id = "asset-laptop-2",
                name = "MacBook Pro 16\" (M3 Max)",
                category = AssetCategory.COMPUTING,
                specification = "36GB RAM / 1TB SSD / Thunderbolt 4",
                registrationOrSerial = "C02G99XZMD",
                statusNote = "Storage 88% full; backup recommended",
                lastServiceInfo = "Purchased Nov 2023",
                mileageOrUsage = "Battery Cycle: 42"
            ),
            UserAsset(
                id = "asset-ac-3",
                name = "Daikin Inverter AC (24,000 BTU)",
                category = AssetCategory.HOME_APPLIANCE,
                specification = "Dual-cool Split Inverter / Living Room",
                registrationOrSerial = "DK-INV-88210",
                statusNote = "Summer pre-season filter & refrigerant check recommended",
                lastServiceInfo = "May 2025",
                mileageOrUsage = "Active 1400 hrs"
            ),
            UserAsset(
                id = "asset-office-4",
                name = "TechCorp Studio Workstations (x4)",
                category = AssetCategory.OFFICE_EQUIPMENT,
                specification = "Dual Display Workstations / Server rack expansion ready",
                registrationOrSerial = "TC-ST-2024",
                statusNote = "Needs 4x high-speed NVMe/SATA storage drives",
                lastServiceInfo = "Deployed Jan 2024",
                mileageOrUsage = "24/7 Operations"
            )
        )
    )
    val assets: StateFlow<List<UserAsset>> = _assets.asStateFlow()

    // 2. Context: Saved Locations
    private val _locations = MutableStateFlow<List<SavedLocation>>(
        listOf(
            SavedLocation(
                id = "loc-office",
                label = "Company / Office",
                address = "TechCorp Tower, 7th Floor, Innovation Blvd",
                latitude = 35.7219,
                longitude = 51.3347,
                isDefault = false
            ),
            SavedLocation(
                id = "loc-home",
                label = "Home",
                address = "Apartment 4B, 18 Maple Ave, North Heights",
                latitude = 35.7650,
                longitude = 51.4120,
                isDefault = true
            ),
            SavedLocation(
                id = "loc-parents",
                label = "Parents' Home",
                address = "No. 12, Cedar Grove Court, Central District",
                latitude = 35.6980,
                longitude = 51.3850,
                isDefault = false
            )
        )
    )
    val locations: StateFlow<List<SavedLocation>> = _locations.asStateFlow()

    // 3. Context: Preferences & Privacy
    private val _userPreference = MutableStateFlow(UserPreference())
    val userPreference: StateFlow<UserPreference> = _userPreference.asStateFlow()

    private val _privacySettings = MutableStateFlow(PrivacySettings())
    val privacySettings: StateFlow<PrivacySettings> = _privacySettings.asStateFlow()

    // 4. Proactive Alerts Engine
    private val _proactiveAlerts = MutableStateFlow<List<ProactiveAlert>>(
        listOf(
            ProactiveAlert(
                id = "alert-bmw-service",
                title = "Vehicle Maintenance Nearing",
                message = "Your BMW 320i has reached 14,850 km (interval: 15,000 km). 2 certified specialists near your office have available slots today at 16:30 and tomorrow morning.",
                category = "Vehicle Health",
                suggestedPrompt = "Book car service near my office for tomorrow morning",
                linkedAssetId = "asset-car-1"
            ),
            ProactiveAlert(
                id = "alert-ac-check",
                title = "Pre-Summer HVAC Optimization",
                message = "Seasonal tune-up recommended for your Daikin Inverter AC to prevent peak summer efficiency loss.",
                category = "Home Maintenance",
                suggestedPrompt = "چهارشنبه کولر رو سرویس کن",
                linkedAssetId = "asset-ac-3"
            )
        )
    )
    val proactiveAlerts: StateFlow<List<ProactiveAlert>> = _proactiveAlerts.asStateFlow()

    // 5. Active Fulfillment Plans
    private val _fulfillmentPlans = MutableStateFlow<List<FulfillmentPlan>>(emptyList())
    val fulfillmentPlans: StateFlow<List<FulfillmentPlan>> = _fulfillmentPlans.asStateFlow()

    // 6. Chat History
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    // 7. Seller Products (Ecosystem)
    private val _sellerProducts = MutableStateFlow<List<SellerProduct>>(
        listOf(
            SellerProduct("sp-1", "Samsung 870 EVO 1TB SATA SSD", "Computer Hardware", 95.0, 34, 24, 4.9, 2),
            SellerProduct("sp-2", "WD Blue 1TB SATA Hard Drive", "Computer Hardware", 68.0, 50, 12, 4.6, 4),
            SellerProduct("sp-3", "White Lily & Peony Floral Arrangement", "Florist & Gift", 45.0, 18, 0, 4.8, 2),
            SellerProduct("sp-4", "Artisan Rose & Hydrangea Box", "Florist & Gift", 60.0, 12, 0, 4.9, 3),
            SellerProduct("sp-5", "Thunderbolt 4 Dual Display Dock", "Computer Accessories", 185.0, 8, 24, 4.7, 2),
            SellerProduct("sp-6", "Ergonomic Mechanical Keyboard (Silent)", "Office Tech", 110.0, 15, 12, 4.8, 3)
        )
    )
    val sellerProducts: StateFlow<List<SellerProduct>> = _sellerProducts.asStateFlow()

    // 8. Service Provider Listings
    private val _serviceListings = MutableStateFlow<List<ServiceProviderListing>>(
        listOf(
            ServiceProviderListing("srv-1", "Daikin & Split AC Deep Clean & Refrigerant Check", 65.0, 75, listOf("Wed 14:00", "Wed 16:30", "Thu 11:00"), 4.9, 15.0),
            ServiceProviderListing("srv-2", "General HVAC Inspection & Filter Replacement", 50.0, 60, listOf("Wed 10:00", "Wed 13:00"), 4.5, 10.0),
            ServiceProviderListing("srv-3", "BMW Certified Brake & Fluid Inspection", 120.0, 90, listOf("Tomorrow 09:30", "Tomorrow 14:00"), 4.9, 20.0),
            ServiceProviderListing("srv-4", "Office Network & Cable Infrastructure Tuning", 80.0, 120, listOf("Fri 10:00"), 4.8, 25.0)
        )
    )
    val serviceListings: StateFlow<List<ServiceProviderListing>> = _serviceListings.asStateFlow()

    init {
        // Seed initial friendly greeting from Aura AI
        _messages.value = listOf(
            ChatMessage(
                id = "init-welcome",
                isUser = false,
                message = "سلام! من دستیار هوشمند شما هستم. نیازی نیست بین اپلیکیشن‌های مختلف بگردید؛ هر کاری دارید را به زبان طبیعی بگویید — از خرید سخت‌افزار تا سفارش گل، رزرو خدمات یا درخواست خودرو.",
                timestamp = getCurrentTimeString(),
                usedContextTags = listOf("Assets: 4 items", "Locations: 3 saved", "Privacy Shield Active")
            )
        )
    }

    private fun getCurrentTimeString(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

    fun addAsset(asset: UserAsset) {
        _assets.value = _assets.value + asset
    }

    fun removeAsset(assetId: String) {
        _assets.value = _assets.value.filter { it.id != assetId }
    }

    fun addLocation(location: SavedLocation) {
        _locations.value = _locations.value + location
    }

    fun updatePrivacySettings(settings: PrivacySettings) {
        _privacySettings.value = settings
    }

    fun dismissProactiveAlert(alertId: String) {
        _proactiveAlerts.value = _proactiveAlerts.value.filter { it.id != alertId }
    }

    // AI Orchestrator Core Intent Processor
    fun processUserPrompt(prompt: String) {
        val userMsgId = UUID.randomUUID().toString()
        val userMsg = ChatMessage(
            id = userMsgId,
            isUser = true,
            message = prompt,
            timestamp = getCurrentTimeString()
        )
        _messages.value = _messages.value + userMsg

        // Orchestrate multi-intents based on prompt analysis
        val response = orchestratePrompt(prompt)
        _messages.value = _messages.value + response

        // If a plan was created, record it
        response.fulfillmentPlan?.let { plan ->
            _fulfillmentPlans.value = listOf(plan) + _fulfillmentPlans.value
        }
    }

    private fun orchestratePrompt(prompt: String): ChatMessage {
        val lower = prompt.lowercase()
        val timeNow = getCurrentTimeString()

        // 1. Check for the iconic multi-intent prompt:
        // "برای شرکت ۴ تا هارد میخوام، برای خونه هم گل سفارش بده، چهارشنبه کولر رو سرویس کن و برای ساعت ۶ هم تاکسی بگیر"
        // Or English variant: "4 hard drives for office, flower for home, AC service wednesday, taxi at 6"
        val hasHardDrive = lower.contains("هارد") || lower.contains("hard drive") || lower.contains("ssd") || lower.contains("هارد دیسک")
        val hasFlower = lower.contains("گل") || lower.contains("flower") || lower.contains("bouquet")
        val hasACService = lower.contains("کولر") || lower.contains("ac") || lower.contains("سرویس کولر") || lower.contains("hvac")
        val hasTaxi = lower.contains("تاکسی") || lower.contains("taxi") || lower.contains("ماشین") || lower.contains("اسنپ") || lower.contains("ride")

        val detectedIntents = mutableListOf<DetectedIntent>()
        val usedContext = mutableListOf<String>()

        if (hasHardDrive) {
            val officeLoc = _locations.value.find { it.label.contains("Office") || it.label.contains("شرکت") }
                ?: _locations.value.first()
            usedContext.add("Context: Company Office (${officeLoc.address})")
            usedContext.add("Asset: Workstations Storage Needs")

            val bestOffer = Offer(
                id = "offer-ssd-samsung",
                title = "Samsung 870 EVO 1TB SATA SSD (x4 Pack)",
                providerName = "TechDirect MegaStore",
                providerType = OfferProviderType.VERIFIED_SELLER,
                price = 380.0,
                rating = 4.9,
                reviewsCount = 428,
                deliveryOrServiceTime = "2-Hour Express Courier",
                warrantyInfo = "24 Months Official Guarantee",
                distanceKm = 4.2,
                isBestMatch = true,
                matchReason = "Lowest price with official 24m warranty & 2h instant dispatch to Company Office",
                badges = listOf("Best Match", "Fast Delivery", "24M Warranty")
            )

            val altOffer = Offer(
                id = "offer-wd-blue",
                title = "WD Blue 1TB SATA 7200RPM (x4 Pack)",
                providerName = "Silicon Valley Supplies",
                providerType = OfferProviderType.VERIFIED_SELLER,
                price = 272.0,
                rating = 4.6,
                reviewsCount = 190,
                deliveryOrServiceTime = "4-Hour Courier",
                warrantyInfo = "12 Months Warranty",
                distanceKm = 7.1,
                isBestMatch = false,
                matchReason = "Budget alternative with mechanical HDD storage"
            )

            detectedIntents.add(
                DetectedIntent(
                    id = "intent-hard-drives",
                    rawQueryPart = "۴ تا هارد برای شرکت",
                    intentType = IntentType.PRODUCT_ORDER,
                    targetItemOrService = "4x Samsung 870 EVO 1TB SSD",
                    quantity = 4,
                    resolvedDestinationLabel = officeLoc.label,
                    resolvedDestinationAddress = officeLoc.address,
                    scheduledTime = "Delivery by 14:00 (Today)",
                    selectedOffer = bestOffer,
                    alternativeOffers = listOf(altOffer)
                )
            )
        }

        if (hasFlower) {
            val homeLoc = _locations.value.find { it.label.contains("Home") || it.label.contains("خونه") }
                ?: _locations.value.first()
            usedContext.add("Context: Home Address (${homeLoc.address})")

            val bestFlower = Offer(
                id = "offer-flower-lily",
                title = "White Lily & Peony Artisan Bouquet",
                providerName = "Bloom Atelier Florist",
                providerType = OfferProviderType.VERIFIED_SELLER,
                price = 45.0,
                rating = 4.8,
                reviewsCount = 112,
                deliveryOrServiceTime = "Arrives by 16:00",
                warrantyInfo = "Freshness Guaranteed (48h)",
                distanceKm = 2.8,
                isBestMatch = true,
                matchReason = "Top rated florist 2.8km from Home, temperature-controlled delivery",
                badges = listOf("Top Rated Florist", "Fresh Delivery")
            )

            detectedIntents.add(
                DetectedIntent(
                    id = "intent-flower-home",
                    rawQueryPart = "گل برای خونه",
                    intentType = IntentType.PRODUCT_ORDER,
                    targetItemOrService = "White Lily & Peony Bouquet",
                    quantity = 1,
                    resolvedDestinationLabel = homeLoc.label,
                    resolvedDestinationAddress = homeLoc.address,
                    scheduledTime = "Delivery by 16:00 (Today)",
                    selectedOffer = bestFlower
                )
            )
        }

        if (hasACService) {
            val homeLoc = _locations.value.find { it.label.contains("Home") || it.label.contains("خونه") }
                ?: _locations.value.first()
            usedContext.add("Asset: Daikin Inverter AC (24,000 BTU)")

            val bestHvac = Offer(
                id = "offer-hvac-daikin",
                title = "Daikin Certified AC Maintenance & Deep Clean",
                providerName = "HVAC Master Specialists",
                providerType = OfferProviderType.SERVICE_SPECIALIST,
                price = 65.0,
                rating = 4.9,
                reviewsCount = 310,
                deliveryOrServiceTime = "Wednesday, 14:00 - 15:30",
                warrantyInfo = "90-day Service Guarantee",
                distanceKm = 3.5,
                isBestMatch = true,
                matchReason = "Official Daikin certification, matched with registered living room split unit",
                badges = listOf("Daikin Certified", "90-Day Guarantee")
            )

            detectedIntents.add(
                DetectedIntent(
                    id = "intent-ac-service",
                    rawQueryPart = "چهارشنبه کولر رو سرویس کن",
                    intentType = IntentType.SERVICE_BOOKING,
                    targetItemOrService = "Daikin AC Full Tune-up & Filter Sanitization",
                    quantity = 1,
                    resolvedDestinationLabel = homeLoc.label,
                    resolvedDestinationAddress = homeLoc.address,
                    scheduledTime = "Wednesday, 14:00",
                    selectedOffer = bestHvac
                )
            )
        }

        if (hasTaxi) {
            val officeLoc = _locations.value.find { it.label.contains("Office") || it.label.contains("شرکت") }
                ?: _locations.value.first()
            val homeLoc = _locations.value.find { it.label.contains("Home") || it.label.contains("خونه") }
                ?: _locations.value.first()
            usedContext.add("Mobility API: Scheduled Comfort Taxi")

            val taxiOffer = Offer(
                id = "offer-taxi-comfort",
                title = "Comfort Sedan (Electric VIP)",
                providerName = "SwiftMobility API",
                providerType = OfferProviderType.MOBILITY_API,
                price = 18.0,
                rating = 4.95,
                reviewsCount = 1420,
                deliveryOrServiceTime = "Pickup at 18:00 (Office -> Home)",
                warrantyInfo = "Live GPS & Fixed Price Guarantee",
                distanceKm = 11.4,
                isBestMatch = true,
                matchReason = "Direct pickup from TechCorp Tower at 18:00 to Home with priority lane routing",
                badges = listOf("Fixed Fare", "VIP Comfort")
            )

            detectedIntents.add(
                DetectedIntent(
                    id = "intent-taxi-ride",
                    rawQueryPart = "برای ساعت ۶ هم تاکسی بگیر",
                    intentType = IntentType.TAXI_RIDE,
                    targetItemOrService = "Executive Ride (TechCorp -> Home)",
                    quantity = 1,
                    resolvedDestinationLabel = "Pickup: " + officeLoc.label,
                    resolvedDestinationAddress = "Dropoff: " + homeLoc.address,
                    scheduledTime = "Today, 18:00 sharp",
                    selectedOffer = taxiOffer
                )
            )
        }

        // If it was a generic query or didn't match the 4 keywords, build a tailored response:
        if (detectedIntents.isEmpty()) {
            // Check if user is asking about their assets (e.g. BMW, phone, laptop)
            if (lower.contains("ماشین") || lower.contains("خودرو") || lower.contains("car") || lower.contains("bmw")) {
                usedContext.add("Asset: BMW 320i (14,850 km)")
                val serviceOffer = Offer(
                    id = "offer-bmw-spec",
                    title = "BMW Fast-Lane Inspection & Oil Service",
                    providerName = "Bavaria Auto Center (Authorized)",
                    providerType = OfferProviderType.SERVICE_SPECIALIST,
                    price = 120.0,
                    rating = 4.9,
                    reviewsCount = 280,
                    deliveryOrServiceTime = "Tomorrow 10:00 AM (2.1 km from Office)",
                    warrantyInfo = "Genuine BMW Parts & Warranty",
                    distanceKm = 2.1,
                    isBestMatch = true,
                    matchReason = "Closest certified BMW center to your office; fits your 15,000 km service schedule"
                )
                detectedIntents.add(
                    DetectedIntent(
                        id = "intent-car-maintenance",
                        rawQueryPart = prompt,
                        intentType = IntentType.HARDWARE_MAINTENANCE,
                        targetItemOrService = "BMW 320i 15,000 km Service Check",
                        quantity = 1,
                        resolvedDestinationLabel = "Bavaria Auto Center",
                        resolvedDestinationAddress = "2.1 km from TechCorp Tower",
                        scheduledTime = "Tomorrow, 10:00 AM",
                        selectedOffer = serviceOffer
                    )
                )
            } else if (lower.contains("گوشی") || lower.contains("phone") || lower.contains("mobile") || lower.contains("پدر")) {
                // Example 14 from user prompt: "یه گوشی خوب برای پدرم میخوام، بودجهم ۳۰ میلیون بیشتر نباشه و فردا به دستم برسه"
                val parentsLoc = _locations.value.find { it.label.contains("Parents") || it.label.contains("پدر") }
                    ?: _locations.value.first()
                usedContext.add("Context: Parents' Home Address")
                usedContext.add("Preference: Fast Delivery & High Rating")

                val phoneOffer = Offer(
                    id = "offer-phone-father",
                    title = "Samsung Galaxy A55 5G (128GB - Awesome Navy)",
                    providerName = "Arya Digital Center",
                    providerType = OfferProviderType.VERIFIED_SELLER,
                    price = 390.0, // approx 28-30 million tomans in USD scale
                    rating = 4.85,
                    reviewsCount = 512,
                    deliveryOrServiceTime = "Tomorrow Morning Express",
                    warrantyInfo = "18 Months Official Guarantee",
                    distanceKm = 5.0,
                    isBestMatch = true,
                    matchReason = "Large legible AMOLED display, durable battery, within budget, with official 18m warranty and tomorrow delivery.",
                    badges = listOf("Best Match for Parents", "Official 18M Warranty")
                )

                detectedIntents.add(
                    DetectedIntent(
                        id = "intent-phone-father",
                        rawQueryPart = prompt,
                        intentType = IntentType.PRODUCT_ORDER,
                        targetItemOrService = "Samsung Galaxy A55 5G (for Parents)",
                        quantity = 1,
                        resolvedDestinationLabel = parentsLoc.label,
                        resolvedDestinationAddress = parentsLoc.address,
                        scheduledTime = "Tomorrow by 11:00 AM",
                        selectedOffer = phoneOffer
                    )
                )
            } else {
                // Dynamic fallback for any general query
                usedContext.add("AI Orchestrator: Multi-Source Engine")
                val genericOffer = Offer(
                    id = "offer-generic-dynamic",
                    title = "Express Service Fulfillment",
                    providerName = "Aura Verified Network",
                    providerType = OfferProviderType.VERIFIED_SELLER,
                    price = 45.0,
                    rating = 4.9,
                    reviewsCount = 88,
                    deliveryOrServiceTime = "Today, 17:00",
                    warrantyInfo = "Standard Satisfaction Guarantee",
                    distanceKm = 3.0,
                    isBestMatch = true,
                    matchReason = "Optimized based on real-time provider availability in your area"
                )
                detectedIntents.add(
                    DetectedIntent(
                        id = "intent-generic-" + System.currentTimeMillis(),
                        rawQueryPart = prompt,
                        intentType = IntentType.GENERAL_QUERY,
                        targetItemOrService = "Fulfillment of: $prompt",
                        quantity = 1,
                        resolvedDestinationLabel = "Primary Location",
                        resolvedDestinationAddress = _locations.value.first().address,
                        scheduledTime = "Today, 17:00",
                        selectedOffer = genericOffer
                    )
                )
            }
        }

        // Create unified Fulfillment Plan
        val totalCost = detectedIntents.sumOf { it.selectedOffer.price }
        val plan = FulfillmentPlan(
            id = "plan-" + UUID.randomUUID().toString().take(8),
            planCode = "AUR-" + (1000..9999).random(),
            createdAt = timeNow,
            intents = detectedIntents,
            totalCost = totalCost,
            currency = "$",
            status = PlanStatus.DRAFT_PROPOSED,
            estimatedCompletionTime = if (hasTaxi) "Today, 18:30 (Final Taxi Dropoff)" else "Scheduled Fulfillment"
        )

        val messageText = buildString {
            append("درخواست شما تحلیل شد و ${detectedIntents.size} بخش اصلی استخراج گردید. تمام هماهنگی‌ها با فروشندگان، متخصصان خدمات و سرویس‌های خارجی انجام شده است:\n\n")
            detectedIntents.forEachIndexed { index, intent ->
                val typeLabel = when (intent.intentType) {
                    IntentType.PRODUCT_ORDER -> "📦 خرید کالا"
                    IntentType.SERVICE_BOOKING -> "🔧 رزرو خدمات"
                    IntentType.TAXI_RIDE -> "🚖 درخواست تاکسی"
                    IntentType.COURIER_DELIVERY -> "🚚 پیک و باربری"
                    IntentType.HARDWARE_MAINTENANCE -> "⚙️ نگهداری دارایی"
                    IntentType.GENERAL_QUERY -> "⚡ هماهنگی روزمره"
                }
                append("${index + 1}. $typeLabel: **${intent.targetItemOrService}**\n")
                append("   • تأمین‌کننده: ${intent.selectedOffer.providerName} (${intent.selectedOffer.providerType.name.replace("_", " ")})\n")
                append("   • مقصد: ${intent.resolvedDestinationLabel}\n")
                append("   • زمان/ارسال: ${intent.selectedOffer.deliveryOrServiceTime}\n")
                append("   • هزینه: ${intent.selectedOffer.currency}${intent.selectedOffer.price.toInt()}\n\n")
            }
            append("برنامه تجمیعی (Fulfillment Plan) با مجموع هزینه $${totalCost.toInt()} آماده تأیید است. نیازی به انتخاب جداگانه ندارید؛ برای بررسی مسیرها دکمه نقشه عملیاتی را بزنید یا مستقیماً تأیید کنید.")
        }

        return ChatMessage(
            id = UUID.randomUUID().toString(),
            isUser = false,
            message = messageText,
            timestamp = timeNow,
            detectedIntents = detectedIntents,
            fulfillmentPlan = plan,
            usedContextTags = usedContext
        )
    }

    fun confirmFulfillmentPlan(planId: String) {
        val currentPlans = _fulfillmentPlans.value.toMutableList()
        val index = currentPlans.indexOfFirst { it.id == planId }
        if (index != -1) {
            val updated = currentPlans[index].copy(status = PlanStatus.IN_FULFILLMENT)
            currentPlans[index] = updated
            _fulfillmentPlans.value = currentPlans

            // Update in messages as well
            val currentMsgs = _messages.value.toMutableList()
            val msgIndex = currentMsgs.indexOfFirst { it.fulfillmentPlan?.id == planId }
            if (msgIndex != -1) {
                val oldMsg = currentMsgs[msgIndex]
                currentMsgs[msgIndex] = oldMsg.copy(
                    fulfillmentPlan = updated
                )
                _messages.value = currentMsgs
            }

            // Post system confirmation message
            val sysMsg = ChatMessage(
                id = UUID.randomUUID().toString(),
                isUser = false,
                message = "✅ برنامه با موفقیت تأیید شد و پرداخت امن انجام گرفت! کلیه سفارش‌ها وارد فاز عملیاتی شدند. می‌توانید وضعیت لحظه‌ای پیک‌ها، مسیرها و رزرو را در نقشه عملیاتی دنبال کنید.",
                timestamp = getCurrentTimeString(),
                usedContextTags = listOf("Automated Payment: Corporate Wallet", "Courier Live Tracking Assigned")
            )
            _messages.value = _messages.value + sysMsg
        }
    }

    // Switch an offer within an intent (e.g. user toggles between Best Match and Budget Alternative)
    fun switchOffer(planId: String, intentId: String, newOffer: Offer) {
        val currentPlans = _fulfillmentPlans.value.toMutableList()
        val planIndex = currentPlans.indexOfFirst { it.id == planId }
        if (planIndex != -1) {
            val plan = currentPlans[planIndex]
            val updatedIntents = plan.intents.map { intent ->
                if (intent.id == intentId) {
                    val newAlternatives = (intent.alternativeOffers + listOf(intent.selectedOffer)).filter { it.id != newOffer.id }
                    intent.copy(selectedOffer = newOffer, alternativeOffers = newAlternatives)
                } else intent
            }
            val newTotal = updatedIntents.sumOf { it.selectedOffer.price }
            val updatedPlan = plan.copy(intents = updatedIntents, totalCost = newTotal)
            currentPlans[planIndex] = updatedPlan
            _fulfillmentPlans.value = currentPlans

            // Update message
            val msgList = _messages.value.map { msg ->
                if (msg.fulfillmentPlan?.id == planId) {
                    msg.copy(fulfillmentPlan = updatedPlan)
                } else msg
            }
            _messages.value = msgList
        }
    }

    // Partner Portal: update inventory or add item
    fun addSellerProduct(product: SellerProduct) {
        _sellerProducts.value = listOf(product) + _sellerProducts.value
    }

    fun addServiceListing(listing: ServiceProviderListing) {
        _serviceListings.value = listOf(listing) + _serviceListings.value
    }
}
