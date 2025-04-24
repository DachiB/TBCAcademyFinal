package com.example.tbcacademyfinal.presentation.ui.main.bottom_nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.presentation.navigation.Routes

sealed class BottomNavItem(
    val route: Routes,
    val titleResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Store : BottomNavItem(
        route = Routes.StoreRoute,
        titleResId = R.string.bottom_nav_store,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.TwoTone.Home
    )

    data object ArCollection : BottomNavItem(
        route = Routes.CollectionRoute,
        titleResId = R.string.bottom_nav_ar_collection,
        selectedIcon = Icons.Filled.PlayArrow,
        unselectedIcon = Icons.TwoTone.PlayArrow
    )

    data object Profile : BottomNavItem(
        route = Routes.ProfileRoute,
        titleResId = R.string.bottom_nav_profile,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.TwoTone.AccountCircle
    )
}