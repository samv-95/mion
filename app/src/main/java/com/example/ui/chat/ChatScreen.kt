package com.example.ui.chat

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.AppNavTab
import com.example.ui.EverydayAssistantViewModel
import com.example.ui.components.AuraBadge
import com.example.ui.components.AuraCard
import com.example.ui.components.ContextTag
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    viewModel: EverydayAssistantViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.messages.collectAsState()
    val isAiTyping by viewModel.isAiTyping.collectAsState()
    val proactiveAlerts by viewModel.proactiveAlerts.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var inputText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Auto-scroll when messages change or typing
    LaunchedEffect(messages.size, isAiTyping) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val samplePrompts = listOf(
        "برای شرکت ۴ تا هارد میخوام، برای خونه گل، چهارشنبه کولر رو سرویس کن و ساعت ۶ تاکسی بگیر",
        "یه گوشی خوب برای پدرم میخوام، بودجهم ۳۰ میلیون بیشتر نباشه و فردا برسه",
        "وضعیت سرویس ماشینم چطوره و نزدیک‌ترین تعمیرگاه کجاست؟",
        "این بسته رو امروز برسون شرکت",
        "یه ماشین برای ساعت ۷ صبح به سمت شرکت میخوام"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PureBlack)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AURA",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(ElectricBlueLight)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    AuraBadge(text = "Everyday Life AI", isHighlight = true)
                }
                Text(
                    text = "Natural Language Orchestrator",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                )
            }

            // Quick Status / Privacy Shield
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceElevated)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Shield,
                    contentDescription = "Privacy Shield",
                    tint = ElectricBlueLight,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Context Active",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Divider(color = BorderSubtle, thickness = 0.8.dp)

        // Proactive Suggestion Banner (Section 7)
        if (proactiveAlerts.isNotEmpty()) {
            val alert = proactiveAlerts.first()
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceElevated)
                        .border(1.dp, ElectricBlue.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.AutoAwesome,
                                    contentDescription = null,
                                    tint = ElectricBlueLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = alert.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                            IconButton(
                                onClick = { viewModel.dismissAlert(alert.id) },
                                modifier = Modifier.size(22.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = TextMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = alert.message,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = {
                                    viewModel.sendPrompt(alert.suggestedPrompt)
                                    viewModel.dismissAlert(alert.id)
                                },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                colors = ButtonDefaults.textButtonColors(contentColor = ElectricBlueLight)
                            ) {
                                Text(
                                    text = "Ask Aura to Coordinate →",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 16.dp)
        ) {
            items(messages, key = { it.id }) { message ->
                ChatMessageItem(
                    message = message,
                    onConfirmPlan = { planId -> viewModel.confirmPlan(planId) },
                    onViewMap = { viewModel.selectTab(AppNavTab.MAP) },
                    onSwitchOffer = { planId, intentId, offer ->
                        viewModel.switchOffer(planId, intentId, offer)
                    }
                )
            }

            if (isAiTyping) {
                item {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceElevated)
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            color = ElectricBlueLight,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Orchestrating intents across Sellers, Services & APIs...",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Quick Suggestion Chips (Easy 1-tap testing of iconic prompts)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            samplePrompts.forEach { prompt ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceDark)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                        .clickable {
                            inputText = prompt
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = prompt.take(38) + if (prompt.length > 38) "..." else "",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Chat Input Bar
        Surface(
            color = DeepCharcoal,
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        // Quick voice simulated interaction
                        inputText = "برای شرکت ۴ تا هارد میخوام، برای خونه هم گل سفارش بده، چهارشنبه کولر رو سرویس کن و برای ساعت ۶ هم تاکسی بگیر."
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(SurfaceElevated)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Mic,
                        contentDescription = "Voice Input",
                        tint = ElectricBlueLight,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = "هر کاری دارید به زبان طبیعی بگویید...",
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (inputText.isNotBlank()) {
                                viewModel.sendPrompt(inputText)
                                inputText = ""
                                focusManager.clearFocus()
                            }
                        }
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.sendPrompt(inputText)
                            inputText = ""
                            focusManager.clearFocus()
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (inputText.isNotBlank()) ElectricBlue else SurfaceElevated)
                        .testTag("send_prompt_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (inputText.isNotBlank()) Color.White else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    onConfirmPlan: (String) -> Unit,
    onViewMap: () -> Unit,
    onSwitchOffer: (String, String, Offer) -> Unit
) {
    if (message.isUser) {
        // User Message
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 310.dp)
                    .clip(RoundedCornerShape(16.dp, 4.dp, 16.dp, 16.dp))
                    .background(ElectricBlue)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Column {
                    Text(
                        text = message.message,
                        color = Color.White,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = message.timestamp,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    } else {
        // AI Orchestrator Response
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(ElectricBlueMuted),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AutoAwesome,
                        contentDescription = null,
                        tint = ElectricBlueLight,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Aura AI Orchestrator",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = message.timestamp,
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Text Content Card
            AuraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = DeepCharcoal
            ) {
                Text(
                    text = message.message,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 20.sp
                )

                // Used Context Transparency Tags (Section 16)
                if (message.usedContextTags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = BorderSubtle, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Context Applied:",
                            fontSize = 10.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                        message.usedContextTags.take(3).forEach { tag ->
                            ContextTag(label = tag)
                        }
                    }
                }
            }

            // If this message has a Fulfillment Plan, render the interactive Multi-Order card
            message.fulfillmentPlan?.let { plan ->
                Spacer(modifier = Modifier.height(12.dp))
                FulfillmentPlanCard(
                    plan = plan,
                    onConfirm = { onConfirmPlan(plan.id) },
                    onViewMap = onViewMap,
                    onSwitchOffer = { intentId, offer ->
                        onSwitchOffer(plan.id, intentId, offer)
                    }
                )
            }
        }
    }
}

@Composable
fun FulfillmentPlanCard(
    plan: FulfillmentPlan,
    onConfirm: () -> Unit,
    onViewMap: () -> Unit,
    onSwitchOffer: (String, Offer) -> Unit
) {
    AuraCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceDark,
        borderColor = if (plan.status == PlanStatus.CONFIRMED_PAID || plan.status == PlanStatus.IN_FULFILLMENT)
            StatusGreen.copy(alpha = 0.5f)
        else
            ElectricBlue.copy(alpha = 0.5f)
    ) {
        // Plan Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Layers,
                    contentDescription = null,
                    tint = ElectricBlueLight,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Fulfillment Plan ${plan.planCode}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            val (statusText, statusColor) = when (plan.status) {
                PlanStatus.DRAFT_PROPOSED -> "Ready for Review" to ElectricBlueLight
                PlanStatus.CONFIRMED_PAID -> "Paid & Assigned" to StatusGreen
                PlanStatus.IN_FULFILLMENT -> "En Route / Active" to StatusGreen
                PlanStatus.COMPLETED -> "Completed" to TextMuted
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(statusColor.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = statusText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = statusColor
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = BorderSubtle, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(10.dp))

        // Multi-Intents list inside this plan
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            plan.intents.forEachIndexed { index, intent ->
                IntentItemRow(
                    index = index + 1,
                    intent = intent,
                    onSwitchOffer = { newOffer -> onSwitchOffer(intent.id, newOffer) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Divider(color = BorderSubtle, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(10.dp))

        // Total & Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Total Unified Cost",
                    fontSize = 11.sp,
                    color = TextMuted
                )
                Text(
                    text = "${plan.currency}${plan.totalCost.toInt()}",
                    fontSize = 18.sp,
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
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Map,
                        contentDescription = "Map",
                        modifier = Modifier.size(16.dp),
                        tint = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Map Layer", fontSize = 12.sp)
                }

                if (plan.status == PlanStatus.DRAFT_PROPOSED) {
                    Button(
                        onClick = onConfirm,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricBlue,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Approve & Pay All",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(StatusGreenMuted)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Tracking Dispatched",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = StatusGreen
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IntentItemRow(
    index: Int,
    intent: DetectedIntent,
    onSwitchOffer: (Offer) -> Unit
) {
    var showAlternatives by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceElevated)
            .padding(10.dp)
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
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(PureBlack),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$index",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricBlueLight
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = intent.targetItemOrService,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "To: ${intent.resolvedDestinationLabel}",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            Text(
                text = "${intent.selectedOffer.currency}${intent.selectedOffer.price.toInt()}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Selected Offer Details
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Storefront,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${intent.selectedOffer.providerName} • ⭐ ${intent.selectedOffer.rating}",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            Text(
                text = intent.selectedOffer.deliveryOrServiceTime,
                fontSize = 11.sp,
                color = ElectricBlueLight,
                fontWeight = FontWeight.Medium
            )
        }

        // Best Match Reason
        if (intent.selectedOffer.matchReason.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "⚡ AI Decision: ${intent.selectedOffer.matchReason}",
                fontSize = 10.sp,
                color = TextMuted,
                lineHeight = 14.sp
            )
        }

        // If alternatives exist, let user inspect or toggle
        if (intent.alternativeOffers.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showAlternatives = !showAlternatives },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = if (showAlternatives) "Hide Alternatives" else "Compare with alternative (${intent.alternativeOffers.first().providerName})",
                    fontSize = 10.sp,
                    color = ElectricBlueLight,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = if (showAlternatives) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = ElectricBlueLight,
                    modifier = Modifier.size(14.dp)
                )
            }

            if (showAlternatives) {
                Spacer(modifier = Modifier.height(6.dp))
                intent.alternativeOffers.forEach { alt ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(PureBlack)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${alt.providerName} (${alt.currency}${alt.price.toInt()})",
                                fontSize = 11.sp,
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = alt.deliveryOrServiceTime,
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }

                        TextButton(
                            onClick = {
                                onSwitchOffer(alt)
                                showAlternatives = false
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Select", fontSize = 11.sp, color = ElectricBlueLight)
                        }
                    }
                }
            }
        }
    }
}
