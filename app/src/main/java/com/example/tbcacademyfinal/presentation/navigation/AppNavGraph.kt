package com.example.tbcacademyfinal.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tbcacademyfinal.presentation.ui.auth.login.LoginScreen
import com.example.tbcacademyfinal.presentation.ui.auth.register.RegisterScreen
import com.example.tbcacademyfinal.presentation.ui.landing.LandingScreen
import com.example.tbcacademyfinal.presentation.ui.splash.SplashScreen

@Composable
fun AppNavGraph(
    navController: NavHostController // Accept NavController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SplashRoute, // Use the passed start destination (SplashRoute)
    ) {
        composable<Routes.SplashRoute> {
            SplashScreen(
                onNavigateToMain = {
                    navController.navigate(Routes.MainGraphRoute) {
                        popUpTo(Routes.SplashRoute) { inclusive = true }
                    }
                },
                onNavigateToAuth = {
                    navController.navigate(Routes.LoginRoute) {
                        popUpTo(Routes.SplashRoute) { inclusive = true }
                    }
                },
                onNavigateToLanding = {
                    navController.navigate(Routes.LandingRoute) {
                        popUpTo(Routes.SplashRoute) { inclusive = true }
                    }
                })
        }

        composable<Routes.LandingRoute> {
            // Pass the correct navigation lambda
            LandingScreen(
                onNavigateToAuth = {
                    navController.navigate(Routes.LoginRoute) {
                        popUpTo(Routes.LandingRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<Routes.LoginRoute> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.MainGraphRoute) {
                        popUpTo(Routes.LoginRoute) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.RegisterRoute)
                }
            )
        }
        composable<Routes.RegisterRoute> {
             RegisterScreen(
                 onRegisterSuccess = {
                     navController.navigate(Routes.MainGraphRoute) {
                         popUpTo(Routes.RegisterRoute) { inclusive = true }
                     }
                 },
                 onNavigateBackToLogin = {
                     navController.popBackStack()
                 }
             )
        }

        // Authentication Graph
//        authGraph(navController)

        // Main Application Graph (Post-Login)
//        mainGraph(navController)
    }
}

// --- Nested Graph Extension Functions HERE!---

