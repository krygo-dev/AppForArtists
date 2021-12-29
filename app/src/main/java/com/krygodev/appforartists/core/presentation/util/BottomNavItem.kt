package com.krygodev.appforartists.core.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.krygodev.appforartists.core.domain.util.Screen

sealed class BottomNavItem(
    val route: String,
    val iconSelected: ImageVector,
    val iconDeselected: ImageVector
) {
    object Home : BottomNavItem(Screen.HomeScreen.route, Icons.Filled.Home, Icons.Outlined.Home)
    object Search : BottomNavItem(Screen.SearchScreen.route, Icons.Filled.Search, Icons.Outlined.Search)
    object Messages : BottomNavItem(Screen.MessagesScreen.route, Icons.Filled.Chat, Icons.Outlined.Chat)
    object Profile : BottomNavItem(Screen.ProfileScreen.route, Icons.Filled.Person, Icons.Outlined.Person)
}