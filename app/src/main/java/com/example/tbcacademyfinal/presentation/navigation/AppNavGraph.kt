package com.example.tbcacademyfinal.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tbcacademyfinal.presentation.ui.preview.ARPreviewScreen
import com.example.tbcacademyfinal.presentation.ui.productselector.ProductListScreen
import kotlinx.serialization.Serializable

@Serializable
data object ProductListScreenDestination

@Serializable
data class ARPreviewScreenDestination(val productId: String)


@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.ProductList.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Product list screen
            composable(Screen.ProductList.route) {
                ProductListScreen(
                    onProductSelected = { product ->
                        // Navigate to AR Preview, passing the product id as parameter.
                        navController.navigate("${Screen.ARPreview.route}/${product.id}") {
                            popUpTo(Screen.ProductList.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // AR Preview screen expecting a productId parameter.
            composable(
                route = "${Screen.ARPreview.route}/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                ARPreviewScreen(
                    productId = backStackEntry.arguments?.getString("productId") ?: ""
                )
            }
        }
    }
}

