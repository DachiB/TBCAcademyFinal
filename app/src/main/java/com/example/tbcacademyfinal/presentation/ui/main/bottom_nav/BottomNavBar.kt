package com.example.tbcacademyfinal.presentation.ui.main.bottom_nav

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import java.lang.reflect.Modifier

@Composable
fun BottomNavBar(bottomBarNavController: NavHostController, bottomNavItems: List<BottomNavItem>) {
    NavigationBar {
        val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        bottomNavItems.forEach { item ->
            // --- Refined isSelected Logic ---
            // Check if the current destination's route string matches the
            // qualified name of the item's route object class.
            // This relies on the internal behavior of compose-navigation with Serializable objects.
            val isSelected = currentDestination?.route == item.route::class.qualifiedName
            // --- End Refined Logic ---

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    // Ensure we are not navigating to the same destination if already selected
                    if (currentDestination?.route != item.route::class.qualifiedName) {
                        bottomBarNavController.navigate(item.route) {
                            popUpTo(bottomBarNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = stringResource(id = item.titleResId)
                    )
                },
                label = { Text(stringResource(id = item.titleResId)) },
                alwaysShowLabel = true
            )
        }
    }
}