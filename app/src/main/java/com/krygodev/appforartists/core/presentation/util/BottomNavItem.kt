package com.krygodev.appforartists.core.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.krygodev.appforartists.core.domain.util.Screen

sealed class BottomNavItem(
    val route: String,
    val name: String,
    val iconSelected: ImageVector
) {
    object Home : BottomNavItem(Screen.HomeScreen.route, "Home", Icons.Filled.Home)
    object Search : BottomNavItem(Screen.SearchScreen.route, "Search", Icons.Filled.Search)
    object Messages : BottomNavItem(Screen.MessagesScreen.route, "Messages", Icons.Filled.Chat)
    object Profile : BottomNavItem(Screen.ProfileScreen.route, "Profile", Icons.Filled.Person)
}