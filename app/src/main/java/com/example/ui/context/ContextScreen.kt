package com.example.ui.context

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.model.AssetCategory
import com.example.model.SavedLocation
import com.example.model.UserAsset
import com.example.ui.EverydayAssistantViewModel
import com.example.ui.components.AuraBadge
import com.example.ui.components.AuraCard
import com.example.ui.theme.*

@Composable
fun ContextScreen(
    viewModel: EverydayAssistantViewModel,
    modifier: Modifier = Modifier
) {
    val assets by viewModel.assets.collectAsState()
    val locations by viewModel.locations.collectAsState()
    val privacySettings by viewModel.privacySettings.collectAsState()
    val userPreference by viewModel.userPreference.collectAsState()

    var showAddAssetDialog by remember { mutableStateOf(false) }

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
                    text = "CONTEXT & PERSONALIZATION",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Explicit Knowledge Verified by You",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                )
            }
            AuraBadge(text = "Privacy Shield", isHighlight = true)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = BorderSubtle, thickness = 0.8.dp)
        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Section 1: Registered Assets (Section 6)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Registered Assets (${assets.size})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    TextButton(
                        onClick = { showAddAssetDialog = true },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = ElectricBlueLight)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Asset", fontSize = 12.sp, color = ElectricBlueLight)
                    }
                }
            }

            items(assets, key = { it.id }) { asset ->
                AuraCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = DeepCharcoal
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (asset.category) {
                                        AssetCategory.VEHICLE -> Icons.Outlined.DirectionsCar
                                        AssetCategory.COMPUTING -> Icons.Outlined.Laptop
                                        AssetCategory.HOME_APPLIANCE -> Icons.Outlined.AcUnit
                                        AssetCategory.MOBILE_DEVICE -> Icons.Outlined.Smartphone
                                        AssetCategory.OFFICE_EQUIPMENT -> Icons.Outlined.Dns
                                    },
                                    contentDescription = null,
                                    tint = ElectricBlueLight,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = asset.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = asset.specification,
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        IconButton(
                            onClick = { viewModel.removeAsset(asset.id) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete",
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceDark)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "💡 ${asset.statusNote}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Section 2: Saved Frequent Locations (Section 6)
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Frequent Locations",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            items(locations, key = { it.id }) { loc ->
                AuraCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = DeepCharcoal
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = ElectricBlueLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = loc.label,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = loc.address,
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            // Section 3: Privacy & AI Controls (Section 16)
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Privacy & Intelligence Shield",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            item {
                AuraCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = DeepCharcoal
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Context-Aware Orchestration",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Allows Aura to match registered devices, offices and vehicles with offers.",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                        Switch(
                            checked = privacySettings.enableContextAwareness,
                            onCheckedChange = {
                                viewModel.updatePrivacySettings(privacySettings.copy(enableContextAwareness = it))
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ElectricBlue,
                                uncheckedTrackColor = SurfaceDark
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = BorderSubtle, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Context Attribution Tags",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Always displays which assets or addresses were used by AI for each decision.",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                        Switch(
                            checked = privacySettings.showContextAttributionTags,
                            onCheckedChange = {
                                viewModel.updatePrivacySettings(privacySettings.copy(showContextAttributionTags = it))
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ElectricBlue,
                                uncheckedTrackColor = SurfaceDark
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = BorderSubtle, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Anonymize Seller Queries",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Merchants only receive delivery location and item specs without your personal profile.",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                        Switch(
                            checked = privacySettings.shareAnonymizedDetailsWithSellers,
                            onCheckedChange = {
                                viewModel.updatePrivacySettings(privacySettings.copy(shareAnonymizedDetailsWithSellers = it))
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ElectricBlue,
                                uncheckedTrackColor = SurfaceDark
                            )
                        )
                    }
                }
            }
        }
    }

    if (showAddAssetDialog) {
        var assetName by remember { mutableStateOf("") }
        var assetSpecs by remember { mutableStateOf("") }
        var assetReg by remember { mutableStateOf("") }
        var assetCategory by remember { mutableStateOf(AssetCategory.HOME_APPLIANCE) }

        AlertDialog(
            onDismissRequest = { showAddAssetDialog = false },
            containerColor = DeepCharcoal,
            title = {
                Text("Register New Asset", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = assetName,
                        onValueChange = { assetName = it },
                        label = { Text("Asset Name (e.g. Bosch Dishwasher)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlueLight,
                            unfocusedBorderColor = BorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    OutlinedTextField(
                        value = assetSpecs,
                        onValueChange = { assetSpecs = it },
                        label = { Text("Model or Specs") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlueLight,
                            unfocusedBorderColor = BorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (assetName.isNotBlank()) {
                            viewModel.addAsset(
                                name = assetName,
                                category = assetCategory,
                                specs = assetSpecs.ifBlank { "Registered Model" },
                                reg = assetReg.ifBlank { "AURA-REG" },
                                status = "Healthy / Monitored by AI"
                            )
                            showAddAssetDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Text("Register Asset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddAssetDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }
}
