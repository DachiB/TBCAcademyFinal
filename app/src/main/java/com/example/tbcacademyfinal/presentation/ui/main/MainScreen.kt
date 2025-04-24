package com.example.tbcacademyfinal.presentation.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tbcacademyfinal.presentation.navigation.Routes
import com.example.tbcacademyfinal.presentation.ui.main.bottom_nav.BottomNavBar
import com.example.tbcacademyfinal.presentation.ui.main.bottom_nav.BottomNavItem
import com.example.tbcacademyfinal.presentation.ui.main.collection.CollectionScreen
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
            BottomNavBar(bottomBarNavController, bottomNavItems)
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
            composable<Routes.CollectionRoute> {
                // Pass the mainNavController if AR screen needs to navigate elsewhere (unlikely?)
                CollectionScreen(
                    onNavigateToArScreen = {
                        mainNavController.navigate(Routes.ArSceneRoute)
                    }
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
