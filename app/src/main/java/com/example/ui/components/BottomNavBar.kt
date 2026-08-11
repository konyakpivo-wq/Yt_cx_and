package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.AppShortcut
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material.icons.outlined.AppShortcut
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.YTRed
import com.example.ui.viewmodel.NavigationTab

data class BottomNavItem(
    val tab: NavigationTab,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun BottomNavBar(
    selectedTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit
) {
    val items = listOf(
        BottomNavItem(NavigationTab.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem(NavigationTab.SHORTS, "Shorts", Icons.Filled.AppShortcut, Icons.Outlined.AppShortcut),
        BottomNavItem(NavigationTab.SUBSCRIPTIONS, "Subscriptions", Icons.Filled.Subscriptions, Icons.Outlined.Subscriptions),
        BottomNavItem(NavigationTab.LIBRARY, "Library", Icons.Filled.VideoLibrary, Icons.Outlined.VideoLibrary),
        BottomNavItem(NavigationTab.GMS_SETTINGS, "MicroG CX", Icons.Filled.Settings, Icons.Outlined.Settings)
    )

    NavigationBar(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = selectedTab == item.tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item.tab) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = YTRed,
                    selectedTextColor = YTRed,
                    indicatorColor = YTRed.copy(alpha = 0.15f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_${item.title.lowercase().replace(" ", "_")}")
            )
        }
    }
}
