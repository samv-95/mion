package com.example.model

enum class IntentType {
    PRODUCT_ORDER,
    SERVICE_BOOKING,
    TAXI_RIDE,
    COURIER_DELIVERY,
    HARDWARE_MAINTENANCE,
    GENERAL_QUERY
}

enum class OfferProviderType {
    VERIFIED_SELLER,
    SERVICE_SPECIALIST,
    MOBILITY_API,
    LOGISTICS_API
}

data class Offer(
    val id: String,
    val title: String,
    val providerName: String,
    val providerType: OfferProviderType,
    val price: Double,
    val currency: String = "$",
    val rating: Double,
    val reviewsCount: Int,
    val deliveryOrServiceTime: String,
    val warrantyInfo: String,
    val distanceKm: Double,
    val isBestMatch: Boolean = false,
    val matchReason: String = "",
    val badges: List<String> = emptyList()
)

data class DetectedIntent(
    val id: String,
    val rawQueryPart: String,
    val intentType: IntentType,
    val targetItemOrService: String,
    val quantity: Int = 1,
    val resolvedDestinationLabel: String,
    val resolvedDestinationAddress: String,
    val scheduledTime: String,
    val selectedOffer: Offer,
    val alternativeOffers: List<Offer> = emptyList(),
    val statusText: String = "Orchestrated"
)

enum class PlanStatus {
    DRAFT_PROPOSED,
    CONFIRMED_PAID,
    IN_FULFILLMENT,
    COMPLETED
}

data class FulfillmentPlan(
    val id: String,
    val planCode: String,
    val createdAt: String,
    val intents: List<DetectedIntent>,
    val totalCost: Double,
    val currency: String = "$",
    val status: PlanStatus = PlanStatus.DRAFT_PROPOSED,
    val estimatedCompletionTime: String = "Today, 18:30"
)

data class ProactiveAlert(
    val id: String,
    val title: String,
    val message: String,
    val category: String,
    val suggestedPrompt: String,
    val linkedAssetId: String? = null,
    val timestamp: String = "Just now",
    val isDismissed: Boolean = false
)

data class ChatMessage(
    val id: String,
    val isUser: Boolean,
    val message: String,
    val timestamp: String,
    val detectedIntents: List<DetectedIntent> = emptyList(),
    val fulfillmentPlan: FulfillmentPlan? = null,
    val usedContextTags: List<String> = emptyList(),
    val proactiveAlert: ProactiveAlert? = null
)

// Data models for the Seller / Service Provider perspective (Section 10)
data class SellerProduct(
    val id: String,
    val title: String,
    val category: String,
    val unitPrice: Double,
    val stockCount: Int,
    val warrantyMonths: Int,
    val sellerRating: Double,
    val dispatchWindowHours: Int
)

data class ServiceProviderListing(
    val id: String,
    val serviceName: String,
    val basePrice: Double,
    val durationMinutes: Int,
    val availableSlots: List<String>,
    val rating: Double,
    val coverageRadiusKm: Double
)
