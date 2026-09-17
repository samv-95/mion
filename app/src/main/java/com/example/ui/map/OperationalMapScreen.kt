package com.example.ui.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FulfillmentPlan
import com.example.model.PlanStatus
import com.example.ui.EverydayAssistantViewModel
import com.example.ui.components.AuraBadge
import com.example.ui.components.AuraCard
import com.example.ui.theme.*

data class MapNode(
    val id: String,
    val label: String,
    val type: String, // "DESTINATION", "STORE", "SERVICE_HQ", "TAXI"
    val xRatio: Float,
    val yRatio: Float,
    val detail: String,
    val status: String,
    val eta: String
)

@Composable
fun OperationalMapScreen(
    viewModel: EverydayAssistantViewModel,
    modifier: Modifier = Modifier
) {
    val plans by viewModel.fulfillmentPlans.collectAsState()
    val activePlan = plans.firstOrNull()

    // Map Nodes
    val nodes = remember {
        listOf(
            MapNode("node-office", "TechCorp Tower", "DESTINATION", 0.72f, 0.35f, "4x SSD Package Delivery", "Destination A", "ETA: 14:00"),
            MapNode("node-home", "Home (Maple Ave)", "DESTINATION", 0.32f, 0.68f, "Fresh Flowers & HVAC Service", "Destination B", "ETA: 16:00"),
            MapNode("node-techdirect", "TechDirect MegaStore", "STORE", 0.85f, 0.20f, "Dispatched via Express Courier", "Pickup", "Departed"),
            MapNode("node-florist", "Bloom Atelier", "STORE", 0.20f, 0.85f, "Packaged in Climate Carrier", "Pickup", "Departed"),
            MapNode("node-hvac", "HVAC Master Service Van", "SERVICE_HQ", 0.15f, 0.45f, "Technician En Route", "Dispatch", "Wed 14:00"),
            MapNode("node-taxi", "SwiftMobility Sedan", "TAXI", 0.60f, 0.50f, "Scheduled for 18:00 Commute", "Active Ride", "18:00 Sharp")
        )
    }

    var selectedNode by remember { mutableStateOf(nodes.first()) }
    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var panOffsetY by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PureBlack)
    ) {
        // Operational Road Network Canvas
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        panOffsetX += dragAmount.x * 0.4f
                        panOffsetY += dragAmount.y * 0.4f
                    }
                }
        ) {
            val width = size.width
            val height = size.height

            // 1. Draw subtle grid of minimalist road network (dark grayscale)
            val roadColor = Color(0xFF1B202A)
            val primaryRoadColor = Color(0xFF262C3A)

            // Horizontal & Vertical major grid corridors
            for (i in 1..8) {
                val y = height * (i / 9f) + panOffsetY * 0.1f
                drawLine(
                    color = roadColor,
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 2.dp.toPx()
                )
            }
            for (j in 1..6) {
                val x = width * (j / 7f) + panOffsetX * 0.1f
                drawLine(
                    color = roadColor,
                    start = Offset(x, 0f),
                    end = Offset(x, height),
                    strokeWidth = 2.dp.toPx()
                )
            }

            // Diagonal Expressway corridor
            drawLine(
                color = primaryRoadColor,
                start = Offset(0f, height * 0.8f + panOffsetY * 0.1f),
                end = Offset(width, height * 0.2f + panOffsetY * 0.1f),
                strokeWidth = 4.dp.toPx()
            )

            // 2. Multi-stop Route Polylines (Surgical Electric Blue)
            // TechDirect -> Office
            val techDirectPos = Offset(width * 0.85f + panOffsetX, height * 0.20f + panOffsetY)
            val officePos = Offset(width * 0.72f + panOffsetX, height * 0.35f + panOffsetY)
            val floristPos = Offset(width * 0.20f + panOffsetX, height * 0.85f + panOffsetY)
            val homePos = Offset(width * 0.32f + panOffsetX, height * 0.68f + panOffsetY)
            val taxiPos = Offset(width * 0.60f + panOffsetX, height * 0.50f + panOffsetY)

            // Route 1: Courier to Office
            drawPath(
                path = Path().apply {
                    moveTo(techDirectPos.x, techDirectPos.y)
                    lineTo(techDirectPos.x - 30f, officePos.y)
                    lineTo(officePos.x, officePos.y)
                },
                color = ElectricBlueLight,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )

            // Route 2: Florist to Home
            drawPath(
                path = Path().apply {
                    moveTo(floristPos.x, floristPos.y)
                    lineTo(homePos.x, floristPos.y - 40f)
                    lineTo(homePos.x, homePos.y)
                },
                color = ElectricBlue,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )

            // Route 3: Taxi Office to Home route
            drawPath(
                path = Path().apply {
                    moveTo(officePos.x, officePos.y)
                    lineTo(taxiPos.x, taxiPos.y)
                    lineTo(homePos.x, homePos.y)
                },
                color = ElectricBlue.copy(alpha = 0.4f),
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Overlay Interactive Nodes
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val totalWidth = maxWidth
            val totalHeight = maxHeight

            nodes.forEach { node ->
                val posX = totalWidth * node.xRatio + panOffsetX.dp
                val posY = totalHeight * node.yRatio + panOffsetY.dp

                val isSelected = selectedNode.id == node.id

                Box(
                    modifier = Modifier
                        .offset(x = posX - 22.dp, y = posY - 22.dp)
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) ElectricBlue else DeepCharcoal)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) Color.White else BorderSubtle,
                            shape = CircleShape
                        )
                        .clickable { selectedNode = node },
                    contentAlignment = Alignment.Center
                ) {
                    val icon = when (node.type) {
                        "DESTINATION" -> Icons.Default.LocationOn
                        "STORE" -> Icons.Default.Storefront
                        "SERVICE_HQ" -> Icons.Default.Build
                        "TAXI" -> Icons.Default.DirectionsCar
                        else -> Icons.Default.Place
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = node.label,
                        tint = if (isSelected) Color.White else ElectricBlueLight,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Top Control Overlay
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "OPERATIONAL MAP",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                AuraBadge(text = "Live Multi-Route", isHighlight = true)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(DeepCharcoal)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                    .clickable {
                        panOffsetX = 0f
                        panOffsetY = 0f
                    }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.MyLocation,
                        contentDescription = "Recenter",
                        tint = ElectricBlueLight,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Recenter",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Bottom Inspection Sheet for Selected Node / Active Multi-Orders
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            AuraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = DeepCharcoal,
                borderColor = BorderMedium
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when (selectedNode.type) {
                                    "DESTINATION" -> Icons.Default.LocationOn
                                    "STORE" -> Icons.Default.Storefront
                                    "SERVICE_HQ" -> Icons.Default.Build
                                    "TAXI" -> Icons.Default.DirectionsCar
                                    else -> Icons.Default.Place
                                },
                                contentDescription = null,
                                tint = ElectricBlueLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = selectedNode.label,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = selectedNode.detail,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ElectricBlueMuted)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = selectedNode.eta,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ElectricBlueLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Divider(color = BorderSubtle, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(8.dp))

                // Stops preview pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    nodes.take(4).forEach { n ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selectedNode.id == n.id) ElectricBlueLight.copy(alpha = 0.2f) else SurfaceDark)
                                .border(
                                    width = 1.dp,
                                    color = if (selectedNode.id == n.id) ElectricBlueLight else BorderSubtle,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable { selectedNode = n }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = n.label.take(10) + "..",
                                fontSize = 10.sp,
                                color = if (selectedNode.id == n.id) Color.White else TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}
