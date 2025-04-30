package com.example.tbcacademyfinal.presentation.navigation

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
        startDestination = Routes.SplashRoute,
    ) {
        composable<Routes.SplashRoute> {
            SplashScreen(
                onNavigateToMain = {
                    navController.navigate(Routes.MainGraphRoute) {
                        popUpTo(Routes.SplashRoute) { inclusive = true }
                    }
                },
                onNavigateToAuth = {
                    navController.navigate(Routes.AuthGraphRoute) {
                        popUpTo(Routes.SplashRoute) { inclusive = true }
                    }
                },
                onNavigateToLanding = {
                    navController.navigate(Routes.LandingRoute) {
                        popUpTo(Routes.SplashRoute) { inclusive = true }
                    }
                }
            )
        }

        // 2. Landing Screen (Top Level)
        composable<Routes.LandingRoute> {
            LandingScreen(
                onNavigateToTutorial = {
                    navController.navigate(Routes.TutorialGraphRoute) {
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


fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<Routes.AuthGraphRoute>(
        startDestination = Routes.LoginRoute
    ) {
        composable<Routes.LoginRoute> {
            LoginScreen(
                onNavigateToMain = {
                    navController.navigate(Routes.MainGraphRoute) {
                        popUpTo(Routes.AuthGraphRoute) { inclusive = true }
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