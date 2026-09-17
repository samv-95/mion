package com.example.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppNavTab
import com.example.ui.theme.*

@Composable
fun BottomNavigationDock(
    currentTab: AppNavTab,
    onTabSelected: (AppNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = DeepCharcoal,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DockItem(
                label = "Chat",
                icon = Icons.Outlined.ChatBubbleOutline,
                activeIcon = Icons.Filled.ChatBubble,
                isSelected = currentTab == AppNavTab.CHAT,
                testTag = "nav_chat_tab",
                onClick = { onTabSelected(AppNavTab.CHAT) }
            )

            DockItem(
                label = "Map",
                icon = Icons.Outlined.Map,
                activeIcon = Icons.Filled.Map,
                isSelected = currentTab == AppNavTab.MAP,
                testTag = "nav_map_tab",
                onClick = { onTabSelected(AppNavTab.MAP) }
            )

            DockItem(
                label = "Orders",
                icon = Icons.Outlined.Assignment,
                activeIcon = Icons.Filled.Assignment,
                isSelected = currentTab == AppNavTab.ORDERS,
                testTag = "nav_orders_tab",
                onClick = { onTabSelected(AppNavTab.ORDERS) }
            )

            DockItem(
                label = "Context",
                icon = Icons.Outlined.Shield,
                activeIcon = Icons.Filled.Shield,
                isSelected = currentTab == AppNavTab.CONTEXT,
                testTag = "nav_context_tab",
                onClick = { onTabSelected(AppNavTab.CONTEXT) }
            )

            DockItem(
                label = "Partner",
                icon = Icons.Outlined.Storefront,
                activeIcon = Icons.Filled.Storefront,
                isSelected = currentTab == AppNavTab.PARTNER,
                testTag = "nav_partner_tab",
                onClick = { onTabSelected(AppNavTab.PARTNER) }
            )
        }
    }
}

@Composable
private fun DockItem(
    label: String,
    icon: ImageVector,
    activeIcon: ImageVector,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) ElectricBlueLight else TextSecondary

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isSelected) ElectricBlueMuted else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSelected) activeIcon else icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = contentColor
        )
    }
}
