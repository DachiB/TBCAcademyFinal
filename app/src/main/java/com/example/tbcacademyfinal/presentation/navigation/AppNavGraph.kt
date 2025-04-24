package com.example.tbcacademyfinal.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.tbcacademyfinal.presentation.ui.auth.login.LoginScreen
import com.example.tbcacademyfinal.presentation.ui.auth.register.RegisterScreen
import com.example.tbcacademyfinal.presentation.ui.landing.LandingScreen
import com.example.tbcacademyfinal.presentation.ui.main.MainScreen
import com.example.tbcacademyfinal.presentation.ui.main.details.DetailsScreen
import com.example.tbcacademyfinal.presentation.ui.splash.SplashScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SplashRoute, // Use the specific object passed (SplashRoute)
    ) {
        // 1. Splash Screen (Top Level)
        composable<Routes.SplashRoute> {
            SplashScreen(
                // ViewModel handled internally by hiltViewModel() in SplashScreen
                onNavigateToMain = {
                    navController.navigate(Routes.MainGraphRoute) { // Go to main graph
                        popUpTo(Routes.SplashRoute) { inclusive = true } // Remove splash from stack
                    }
                },
                onNavigateToAuth = {
                    navController.navigate(Routes.AuthGraphRoute) { // Go to auth graph
                        popUpTo(Routes.SplashRoute) { inclusive = true } // Remove splash from stack
                    }
                },
                onNavigateToLanding = {
                    navController.navigate(Routes.LandingRoute) { // Go to landing
                        popUpTo(Routes.SplashRoute) { inclusive = true } // Remove splash from stack
                    }
                }
            )
        }

        // 2. Landing Screen (Top Level)
        composable<Routes.LandingRoute> {
            LandingScreen(
                // ViewModel handled internally
                onNavigateToAuth = {
                    navController.navigate(Routes.AuthGraphRoute) { // Go to auth graph
                        // Pop Landing off stack, user shouldn't go back to it after proceeding
                        popUpTo(Routes.LandingRoute) { inclusive = true }
                    }
                }
            )
        }

        // 3. Authentication Nested Graph (Top Level)
        authGraph(navController)

        // 4. Main Application Nested Graph (Top Level)
        mainGraph(navController)
    }
}

// --- Nested Graph Extension Functions ---

/**
 * Defines the navigation graph for the Authentication flow (Login, Register).
 */
fun NavGraphBuilder.authGraph(navController: NavHostController) {
    // Use the AuthGraphRoute object to define the nested graph itself
    navigation<Routes.AuthGraphRoute>(
        startDestination = Routes.LoginRoute // Starting screen within this graph is Login
    ) {
        // Login Screen
        composable<Routes.LoginRoute> {
            LoginScreen(
                // ViewModel handled internally
                onNavigateToMain = {
                    // Navigate to the entire Main graph after successful login
                    navController.navigate(Routes.MainGraphRoute) {
                        // Pop the entire Auth graph off the back stack
                        popUpTo(Routes.AuthGraphRoute) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    // Navigate to the Register screen within the same graph
                    navController.navigate(Routes.RegisterRoute)
                    // Don't pop Login, allow back navigation to it
                }
            )
        }

        // Register Screen
        composable<Routes.RegisterRoute> {
            RegisterScreen(
                // ViewModel handled internally
                onRegisterSuccess = {
                    // Navigate to the Main graph after successful registration
                    navController.navigate(Routes.MainGraphRoute) {
                        // Pop the entire Auth graph off the back stack
                        popUpTo(Routes.AuthGraphRoute) { inclusive = true }
                    }
                },
                onNavigateBackToLogin = {
                    // Simply pop the current screen (Register) to go back to Login
                    navController.popBackStack()
                }
            )
        }
    }
}

/**
 * Defines the navigation graph for the Main Application flow (post-authentication),
 * including the screen hosting the bottom navigation and other destinations like Details.
 */
fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    // Use the MainGraphRoute object to define the nested graph
    navigation<Routes.MainGraphRoute>(
        startDestination = Routes.MainContainerRoute // Start with the screen that has the Bottom Nav
    ) {
        // Main Container Screen (Hosts Bottom Navigation)
        composable<Routes.MainContainerRoute> {
            // MainScreen internally manages navigation between Store/AR/Profile tabs
            // using its own NavController (bottomBarNavController).
            // We pass the main `navController` here for actions *leaving* the
            // bottom nav flow (e.g., navigating to Details, or Logging Out from Profile).
            MainScreen(mainNavController = navController)
        }

        // Details Screen (Navigated to FROM StoreScreen, hence part of Main graph)
        composable<Routes.DetailsRoute> {
            // Use .toRoute() to get the type-safe argument object
            DetailsScreen(
                // Assuming DetailsScreen exists
                onNavigateBack = { navController.popBackStack() }, // Handle back navigation
                // Pass main navController for navigating back etc.
            )
        }

        // --- Note on Bottom Bar Destinations ---
        // StoreRoute, ArCollectionRoute, and ProfileRoute composables are defined *inside*
        // the NavHost within MainScreen.kt, controlled by the `bottomBarNavController`.
        // They don't need separate `composable` entries here in the mainGraph definition,
        // unless you wanted to allow deep-linking directly to them via the `mainNavController`
        // (which adds complexity). The current setup keeps them encapsulated within MainScreen.
    }
}