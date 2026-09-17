package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.EverydayAssistantRepository
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppNavTab {
    CHAT,
    ORDERS,
    MAP,
    CONTEXT,
    PARTNER
}

class EverydayAssistantViewModel(
    private val repository: EverydayAssistantRepository = EverydayAssistantRepository()
) : ViewModel() {

    val messages: StateFlow<List<ChatMessage>> = repository.messages
    val fulfillmentPlans: StateFlow<List<FulfillmentPlan>> = repository.fulfillmentPlans
    val assets: StateFlow<List<UserAsset>> = repository.assets
    val locations: StateFlow<List<SavedLocation>> = repository.locations
    val userPreference: StateFlow<UserPreference> = repository.userPreference
    val privacySettings: StateFlow<PrivacySettings> = repository.privacySettings
    val proactiveAlerts: StateFlow<List<ProactiveAlert>> = repository.proactiveAlerts
    val sellerProducts: StateFlow<List<SellerProduct>> = repository.sellerProducts
    val serviceListings: StateFlow<List<ServiceProviderListing>> = repository.serviceListings

    private val _currentTab = MutableStateFlow(AppNavTab.CHAT)
    val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

    private val _isAiTyping = MutableStateFlow(false)
    val isAiTyping: StateFlow<Boolean> = _isAiTyping.asStateFlow()

    fun selectTab(tab: AppNavTab) {
        _currentTab.value = tab
    }

    fun sendPrompt(prompt: String) {
        if (prompt.isBlank()) return
        viewModelScope.launch {
            _isAiTyping.value = true
            delay(500) // Realistic orchestrator thinking delay
            repository.processUserPrompt(prompt)
            _isAiTyping.value = false
        }
    }

    fun confirmPlan(planId: String) {
        viewModelScope.launch {
            repository.confirmFulfillmentPlan(planId)
        }
    }

    fun switchOffer(planId: String, intentId: String, newOffer: Offer) {
        repository.switchOffer(planId, intentId, newOffer)
    }

    fun dismissAlert(alertId: String) {
        repository.dismissProactiveAlert(alertId)
    }

    fun addAsset(name: String, category: AssetCategory, specs: String, reg: String, status: String) {
        val newAsset = UserAsset(
            id = "asset-" + System.currentTimeMillis(),
            name = name,
            category = category,
            specification = specs,
            registrationOrSerial = reg,
            statusNote = status
        )
        repository.addAsset(newAsset)
    }

    fun removeAsset(assetId: String) {
        repository.removeAsset(assetId)
    }

    fun addLocation(label: String, address: String) {
        val newLoc = SavedLocation(
            id = "loc-" + System.currentTimeMillis(),
            label = label,
            address = address,
            latitude = 35.7300,
            longitude = 51.3500
        )
        repository.addLocation(newLoc)
    }

    fun updatePrivacySettings(settings: PrivacySettings) {
        repository.updatePrivacySettings(settings)
    }

    fun addSellerProduct(title: String, category: String, price: Double, stock: Int, warranty: Int) {
        val item = SellerProduct(
            id = "sp-" + System.currentTimeMillis(),
            title = title,
            category = category,
            unitPrice = price,
            stockCount = stock,
            warrantyMonths = warranty,
            sellerRating = 4.9,
            dispatchWindowHours = 2
        )
        repository.addSellerProduct(item)
    }

    fun addServiceListing(name: String, price: Double, duration: Int, slot: String) {
        val item = ServiceProviderListing(
            id = "srv-" + System.currentTimeMillis(),
            serviceName = name,
            basePrice = price,
            durationMinutes = duration,
            availableSlots = listOf(slot),
            rating = 4.9,
            coverageRadiusKm = 20.0
        )
        repository.addServiceListing(item)
    }
}
