package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppNavTab
import com.example.ui.EverydayAssistantViewModel
import com.example.ui.chat.ChatScreen
import com.example.ui.context.ContextScreen
import com.example.ui.map.OperationalMapScreen
import com.example.ui.navigation.BottomNavigationDock
import com.example.ui.orders.OrdersScreen
import com.example.ui.partner.PartnerPortalScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PureBlack

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true) {
                MainAppScreen()
            }
        }
    }
}

@Composable
fun MainAppScreen(
    viewModel: EverydayAssistantViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PureBlack,
        bottomBar = {
            BottomNavigationDock(
                currentTab = currentTab,
                onTabSelected = { tab -> viewModel.selectTab(tab) }
            )
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentTab,
            animationSpec = tween(220),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            label = "tab_crossfade"
        ) { tab ->
            when (tab) {
                AppNavTab.CHAT -> ChatScreen(viewModel = viewModel)
                AppNavTab.MAP -> OperationalMapScreen(viewModel = viewModel)
                AppNavTab.ORDERS -> OrdersScreen(viewModel = viewModel)
                AppNavTab.CONTEXT -> ContextScreen(viewModel = viewModel)
                AppNavTab.PARTNER -> PartnerPortalScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    androidx.compose.material3.Text(text = "Hello $name!", modifier = modifier)
}
