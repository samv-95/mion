package com.example.model

enum class AssetCategory {
    VEHICLE,
    COMPUTING,
    HOME_APPLIANCE,
    MOBILE_DEVICE,
    OFFICE_EQUIPMENT
}

data class UserAsset(
    val id: String,
    val name: String,
    val category: AssetCategory,
    val specification: String,
    val registrationOrSerial: String,
    val statusNote: String,
    val lastServiceInfo: String = "",
    val mileageOrUsage: String = ""
)

data class SavedLocation(
    val id: String,
    val label: String, // e.g., "Company / Office", "Home", "Parents' Home"
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val isDefault: Boolean = false
)

data class UserPreference(
    val favoriteBrands: List<String> = listOf("Samsung", "Apple", "Daikin", "Bosch"),
    val prioritizeFastDelivery: Boolean = true,
    val requireOfficialWarranty: Boolean = true,
    val autoApproveUnderBudget: Boolean = false,
    val defaultPaymentMethod: String = "Corporate Wallet (Active)"
)

data class PrivacySettings(
    val enableContextAwareness: Boolean = true,
    val showContextAttributionTags: Boolean = true,
    val shareAnonymizedDetailsWithSellers: Boolean = true,
    val allowProactiveMaintenanceReminders: Boolean = true
)
