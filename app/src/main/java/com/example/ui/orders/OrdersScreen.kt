package com.example.ui.orders

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FulfillmentPlan
import com.example.model.PlanStatus
import com.example.ui.AppNavTab
import com.example.ui.EverydayAssistantViewModel
import com.example.ui.components.AuraBadge
import com.example.ui.components.AuraCard
import com.example.ui.theme.*

@Composable
fun OrdersScreen(
    viewModel: EverydayAssistantViewModel,
    modifier: Modifier = Modifier
) {
    val plans by viewModel.fulfillmentPlans.collectAsState()

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
                    text = "ORDERS & FULFILLMENT",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Multi-Destination & Multi-Provider Plans",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                )
            }
            AuraBadge(text = "${plans.size} Active", isHighlight = true)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = BorderSubtle, thickness = 0.8.dp)
        Spacer(modifier = Modifier.height(16.dp))

        if (plans.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.Assignment,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Orders Yet",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ask Aura AI in the chat to order products or book services.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.selectTab(AppNavTab.CHAT) },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Go to Chat")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(plans, key = { it.id }) { plan ->
                    OrderPlanItem(
                        plan = plan,
                        onViewMap = { viewModel.selectTab(AppNavTab.MAP) },
                        onConfirm = { viewModel.confirmPlan(plan.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderPlanItem(
    plan: FulfillmentPlan,
    onViewMap: () -> Unit,
    onConfirm: () -> Unit
) {
    AuraCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = DeepCharcoal
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Plan ${plan.planCode}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Created ${plan.createdAt} • ${plan.intents.size} Sub-Orders",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            val statusColor = when (plan.status) {
                PlanStatus.DRAFT_PROPOSED -> ElectricBlueLight
                PlanStatus.CONFIRMED_PAID -> StatusGreen
                PlanStatus.IN_FULFILLMENT -> StatusGreen
                PlanStatus.COMPLETED -> TextMuted
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(statusColor.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = plan.status.name.replace("_", " "),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Divider(color = BorderSubtle, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(10.dp))

        // Breakdown of intents
        plan.intents.forEachIndexed { idx, intent ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(ElectricBlueLight)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = intent.targetItemOrService,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                        Text(
                            text = "${intent.selectedOffer.providerName} → ${intent.resolvedDestinationLabel}",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }
                Text(
                    text = "${intent.selectedOffer.currency}${intent.selectedOffer.price.toInt()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Divider(color = BorderSubtle, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Total Unified", fontSize = 10.sp, color = TextMuted)
                Text(
                    text = "${plan.currency}${plan.totalCost.toInt()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onViewMap,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    border = BorderStroke(1.dp, BorderSubtle),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Map Tracking", fontSize = 11.sp)
                }

                if (plan.status == PlanStatus.DRAFT_PROPOSED) {
                    Button(
                        onClick = onConfirm,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("Approve All", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
