package com.krygodev.appforartists.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.krygodev.appforartists.core.presentation.util.BottomNavItem

@Composable
fun SetupBottomNavBar(
    navController: NavController
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Messages,
        BottomNavItem.Profile
    )

    BottomNavBar(
        items = items,
        navController = navController,
        onItemClick = {
            navController.navigate(it.route) {
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}