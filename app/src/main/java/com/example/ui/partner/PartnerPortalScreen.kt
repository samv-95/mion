package com.example.ui.partner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SellerProduct
import com.example.model.ServiceProviderListing
import com.example.ui.EverydayAssistantViewModel
import com.example.ui.components.AuraBadge
import com.example.ui.components.AuraCard
import com.example.ui.theme.*

@Composable
fun PartnerPortalScreen(
    viewModel: EverydayAssistantViewModel,
    modifier: Modifier = Modifier
) {
    val sellerProducts by viewModel.sellerProducts.collectAsState()
    val serviceListings by viewModel.serviceListings.collectAsState()

    var partnerRole by remember { mutableStateOf("SELLER") } // "SELLER" or "SERVICE_PROVIDER"
    var showAddProductDialog by remember { mutableStateOf(false) }
    var showAddServiceDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PureBlack)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "PARTNER CONSOLE",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Merchant & Service Ecosystem Hub",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                )
            }
            AuraBadge(text = "Verified Merchant", isHighlight = true)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Role Segment Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(DeepCharcoal)
                .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (partnerRole == "SELLER") ElectricBlue else Color.Transparent)
                    .clickable { partnerRole = "SELLER" }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Seller / Retail Hub",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (partnerRole == "SELLER") Color.White else TextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (partnerRole == "SERVICE_PROVIDER") ElectricBlue else Color.Transparent)
                    .clickable { partnerRole = "SERVICE_PROVIDER" }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Service Specialists",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (partnerRole == "SERVICE_PROVIDER") Color.White else TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = BorderSubtle, thickness = 0.8.dp)
        Spacer(modifier = Modifier.height(14.dp))

        if (partnerRole == "SELLER") {
            // Seller Inventory & Incoming AI Orders
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live Catalog (${sellerProducts.size} Items)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                TextButton(
                    onClick = { showAddProductDialog = true },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = ElectricBlueLight)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Product", fontSize = 12.sp, color = ElectricBlueLight)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(sellerProducts, key = { it.id }) { product ->
                    AuraCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = DeepCharcoal
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = product.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${product.category} • Dispatch window: ${product.dispatchWindowHours}h",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                            Text(
                                text = "$${product.unitPrice.toInt()}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "In Stock: ${product.stockCount} units",
                                fontSize = 11.sp,
                                color = if (product.stockCount > 5) StatusGreen else StatusAmber,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Warranty: ${product.warrantyMonths} Months",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        } else {
            // Service Provider View
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Active Services (${serviceListings.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                TextButton(
                    onClick = { showAddServiceDialog = true },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = ElectricBlueLight)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Service", fontSize = 12.sp, color = ElectricBlueLight)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(serviceListings, key = { it.id }) { service ->
                    AuraCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = DeepCharcoal
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = service.serviceName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Estimated Duration: ${service.durationMinutes} mins • Radius: ${service.coverageRadiusKm}km",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                            Text(
                                text = "$${service.basePrice.toInt()}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Next Available: ${service.availableSlots.joinToString(", ")}",
                            fontSize = 11.sp,
                            color = ElectricBlueLight,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

    if (showAddProductDialog) {
        var pTitle by remember { mutableStateOf("") }
        var pPrice by remember { mutableStateOf("") }
        var pStock by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddProductDialog = false },
            containerColor = DeepCharcoal,
            title = { Text("List New Product", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = pTitle,
                        onValueChange = { pTitle = it },
                        label = { Text("Product Name") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlueLight, unfocusedBorderColor = BorderSubtle, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                    OutlinedTextField(
                        value = pPrice,
                        onValueChange = { pPrice = it },
                        label = { Text("Price ($)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlueLight, unfocusedBorderColor = BorderSubtle, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                    OutlinedTextField(
                        value = pStock,
                        onValueChange = { pStock = it },
                        label = { Text("Stock Quantity") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlueLight, unfocusedBorderColor = BorderSubtle, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (pTitle.isNotBlank()) {
                            viewModel.addSellerProduct(
                                title = pTitle,
                                category = "Electronics & Supplies",
                                price = pPrice.toDoubleOrNull() ?: 50.0,
                                stock = pStock.toIntOrNull() ?: 10,
                                warranty = 12
                            )
                            showAddProductDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Text("Publish to AI Engine")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddProductDialog = false }) { Text("Cancel", color = TextMuted) }
            }
        )
    }

    if (showAddServiceDialog) {
        var sName by remember { mutableStateOf("") }
        var sPrice by remember { mutableStateOf("") }
        var sSlot by remember { mutableStateOf("Tomorrow 14:00") }

        AlertDialog(
            onDismissRequest = { showAddServiceDialog = false },
            containerColor = DeepCharcoal,
            title = { Text("Register Service", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = sName,
                        onValueChange = { sName = it },
                        label = { Text("Service Title") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlueLight, unfocusedBorderColor = BorderSubtle, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                    OutlinedTextField(
                        value = sPrice,
                        onValueChange = { sPrice = it },
                        label = { Text("Base Price ($)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlueLight, unfocusedBorderColor = BorderSubtle, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (sName.isNotBlank()) {
                            viewModel.addServiceListing(
                                name = sName,
                                price = sPrice.toDoubleOrNull() ?: 60.0,
                                duration = 60,
                                slot = sSlot
                            )
                            showAddServiceDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Text("Register Service")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddServiceDialog = false }) { Text("Cancel", color = TextMuted) }
            }
        )
    }
}
