package com.example.tbcacademyfinal.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.tbcacademyfinal.presentation.ui.auth.login.LoginScreen
import com.example.tbcacademyfinal.presentation.ui.auth.register.RegisterScreen
import com.example.tbcacademyfinal.presentation.ui.landing.LandingScreen
import com.example.tbcacademyfinal.presentation.ui.main.MainScreen
import com.example.tbcacademyfinal.presentation.ui.main.ar_scene.ArSceneScreen
import com.example.tbcacademyfinal.presentation.ui.main.details.DetailsScreen
import com.example.tbcacademyfinal.presentation.ui.main.model_scene.ModelScreen
import com.example.tbcacademyfinal.presentation.ui.splash.SplashScreen
import com.example.tbcacademyfinal.presentation.ui.tutorial.TutorialScreen

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
                onNavigateToTutorial = {
                    navController.navigate(Routes.TutorialGraphRoute) { // Go to auth graph
                        // Pop Landing off stack, user shouldn't go back to it after proceeding
                        popUpTo(Routes.LandingRoute) { inclusive = true }
                    }
                }
            )
        }

        tutorialGraph(navController)

        authGraph(navController)

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

        composable<Routes.RegisterRoute> {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.MainGraphRoute) {
                        popUpTo(Routes.AuthGraphRoute) { inclusive = true }
                    }
                },
                onNavigateBackToLogin = {
                    navController.popBackStack()
                }
            )
        }
    }
}

fun NavGraphBuilder.tutorialGraph(navController: NavHostController) {
    navigation<Routes.TutorialGraphRoute>(
        startDestination = Routes.TutorialRoute
    ) {
        composable<Routes.TutorialRoute> {
            TutorialScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.AuthGraphRoute) {
                        popUpTo(Routes.TutorialGraphRoute) { inclusive = true }
                    }
                },
            )
        }
    }
}

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation<Routes.MainGraphRoute>(
        startDestination = Routes.MainContainerRoute
    ) {
        composable<Routes.MainContainerRoute> {
            MainScreen(mainNavController = navController)
        }

        composable<Routes.DetailsRoute> {
            DetailsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToModelScene = { productId ->
                    navController.navigate(Routes.ModelRoute(productId))
                }
            )
        }
        composable<Routes.ArSceneRoute> {
            ArSceneScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable<Routes.ModelRoute> {
            ModelScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}