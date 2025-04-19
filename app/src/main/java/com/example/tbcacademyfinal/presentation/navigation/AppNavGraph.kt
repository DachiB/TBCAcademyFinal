package com.example.tbcacademyfinal.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    // We need a way to determine the starting point based on auth state
    // Let's assume SplashViewModel handles this check
) {
    NavHost(
        navController = navController,
        startDestination = SplashRoute, // Always start with Splash to check auth
        modifier = modifier
    ) {
        composable<SplashRoute> {
            // Ideally, inject the ViewModel
            val viewModel: SplashScreenViewModel = hiltViewModel()
            // Observe some state from the ViewModel to decide where to go next
            val authState by viewModel.authState.collectAsState() // Example state flow

            SplashScreen(
                authState = authState, // Pass state down if needed
                onAuthCheckComplete = { isLoggedIn ->
                    val destination: Any = if (isLoggedIn) MainGraph else AuthGraph
                    navController.navigate(destination) {
                        popUpTo(SplashRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Authentication Flow Nested Graph
        authGraph(navController)

        // Main Application Flow Nested Graph
        mainGraph(navController)
    }
}

// Defines the nested graph for authentication
private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<AuthGraph>(
        startDestination = LoginRoute // Starting point of the auth flow
    ) {
        composable<LoginRoute> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(MainGraph) { // Navigate to main app graph
                        popUpTo(AuthGraph) { inclusive = true } // Clear the entire auth graph
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(RegisterRoute) // Navigate within the auth graph
                }
            )
        }
        composable<RegisterRoute> {
            RegisterScreen(
                onRegisterSuccess = {
                    // Navigate back to Login after successful registration
                    navController.popBackStack() // Pops RegisterRoute, goes back to LoginRoute
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

// Defines the nested graph for the main application features (post-login)
private fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation<MainGraph>(
        startDestination = MainContainerRoute // Start with the screen holding the Bottom Nav Bar
    ) {
        // The destination that hosts the Bottom Navigation Bar and the NavHost for its tabs
        composable<MainContainerRoute> {
            MainContainerScreen(
                // This screen will internally manage navigation between Store, Collection, Profile
                // It might need the mainNavController for navigating outside the tabs (e.g., to Details)
                onNavigateToDetails = { productId ->
                    navController.navigate(DetailsRoute(productId = productId))
                },
                onLogout = {
                    // Perform logout logic (clear session, etc.)
                    navController.navigate(AuthGraph) { // Go back to login screen
                        popUpTo(MainGraph) { inclusive = true } // Clear the entire main graph
                        launchSingleTop = true
                    }
                }
            )
        }

        // Details Screen - Part of the main graph, but not a tab in the bottom nav
        composable<DetailsRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<DetailsRoute>()
            DetailsScreen(
                productId = args.productId,
                onNavigateBack = { navController.popBackStack() }
                // Potentially need navigation to AR from here too? Or only from collection?
            )
        }
        // Add other destinations reachable from within the main flow but outside the bottom nav tabs
    }
}
