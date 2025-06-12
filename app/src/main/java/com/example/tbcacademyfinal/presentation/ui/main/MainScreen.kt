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
    mainNavController: NavHostController
) {
    val bottomBarNavController = rememberNavController()

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
        NavHost(
            navController = bottomBarNavController,
            startDestination = Routes.StoreRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Routes.StoreRoute> {
                StoreScreen(onNavigateToDetails = { productId ->
                    mainNavController.navigate(Routes.DetailsRoute(productId))
                })
            }
            composable<Routes.CollectionRoute> {
                CollectionScreen(
                    onNavigateToArScreen = {
                        mainNavController.navigate(Routes.ArSceneRoute)
                    }
                )
            }
            composable<Routes.ProfileRoute> {
                ProfileScreen(
                    onLogout = {
                        mainNavController.navigate(Routes.AuthGraphRoute) {
                            popUpTo(Routes.MainContainerRoute) { inclusive = true }
                        }
                    }
                )
            }

        }
    }
}


