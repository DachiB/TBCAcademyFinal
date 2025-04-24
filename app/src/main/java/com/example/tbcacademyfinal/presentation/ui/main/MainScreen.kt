package com.example.tbcacademyfinal.presentation.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tbcacademyfinal.presentation.navigation.Routes
import com.example.tbcacademyfinal.presentation.ui.main.collection.ArCollectionScreen
import com.example.tbcacademyfinal.presentation.ui.main.profile.ProfileScreen
import com.example.tbcacademyfinal.presentation.ui.main.store.StoreScreen

@Composable
fun MainScreen(
    // This is the NavController for the overall app navigation (Auth -> Main, Main -> Details)
    mainNavController: NavHostController
) {
    // Create a separate NavController for the bottom bar navigation
    val bottomBarNavController = rememberNavController()

    // List of bottom navigation items
    val bottomNavItems = listOf(
        BottomNavItem.Store,
        BottomNavItem.ArCollection,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
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
    ) { innerPadding ->
        // NavHost for the content area, controlled by bottomBarNavController
        NavHost(
            navController = bottomBarNavController,
            // Start destination is the first item in our list (Store)
            startDestination = Routes.StoreRoute, // Use the actual @Serializable object/class
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Routes.StoreRoute> {
                // Pass the mainNavController for navigating *outside* the bottom bar flow (e.g., to Details)
                StoreScreen(onNavigateToDetails = { productId ->
                    mainNavController.navigate(Routes.DetailsRoute(productId))
                })
            }
            composable<Routes.ArCollectionRoute> {
                // Pass the mainNavController if AR screen needs to navigate elsewhere (unlikely?)
                ArCollectionScreen(
                    onNavigateToArScreen = {}
                )
            }
            composable<Routes.ProfileRoute> {
                // Pass the mainNavController for actions like logout (which navigates back to AuthGraph)
                ProfileScreen(
                    onLogout = {
                        mainNavController.navigate(Routes.AuthGraphRoute) {
                            popUpTo(Routes.MainContainerRoute) { inclusive = true }
                        }
                    }
                )
            }
            // Add other potential composables reachable *within* the main flow if needed
            // Note: DetailsScreen is defined in the outer mainGraph (AppNavigation.kt)
            // because it's navigated to *from* StoreScreen using the mainNavController.
        }
    }
}

// --- Placeholder Screens (Create these files in respective packages later) ---
// ProfileScreen will be implemented next
