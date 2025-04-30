package com.example.tbcacademyfinal.presentation.ui.main.bottom_nav

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

@Composable
fun BottomNavBar(bottomBarNavController: NavHostController, bottomNavItems: List<BottomNavItem>) {
    NavigationBar {
        val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        bottomNavItems.forEach { item ->

            val isSelected = currentDestination?.route == item.route::class.qualifiedName

            NavigationBarItem(
                selected = isSelected,
                onClick = {
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