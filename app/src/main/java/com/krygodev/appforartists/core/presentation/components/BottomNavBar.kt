package com.krygodev.appforartists.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.krygodev.appforartists.core.presentation.util.BottomNavItem

@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    Row(
        modifier = Modifier.padding(start = 30.dp, end = 30.dp)
    ) {
        BottomNavigation(
            modifier = modifier
                .clip(RoundedCornerShape(50.dp))
                .height(70.dp),
            backgroundColor = Color.Black,
            elevation = 5.dp
        ) {
            items.forEach { item ->
                val selected =
                    item.route == backStackEntry.value?.destination?.route ||
                            item.route + "?uid={uid}" == backStackEntry.value?.destination?.route

                BottomNavigationItem(
                    selected = selected,
                    onClick = { onItemClick(item) },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.LightGray,
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val color = if (selected) Color.DarkGray else Color.Transparent
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(27.dp))
                                    .background(color = color),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (selected) item.iconSelected else item.iconDeselected,
                                    contentDescription = null,
                                    modifier = Modifier.size(27.dp)
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}