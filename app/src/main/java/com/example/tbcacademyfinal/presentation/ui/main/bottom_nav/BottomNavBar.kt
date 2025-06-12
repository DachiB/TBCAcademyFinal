package com.example.tbcacademyfinal.presentation.ui.main.bottom_nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tbcacademyfinal.presentation.theme.GreenLinearGradient

@Composable
fun BottomNavBar(
    bottomBarNavController: NavHostController,
    bottomNavItems: List<BottomNavItem>
) {
    val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
    val currentDest = navBackStackEntry?.destination

    NavigationBar(
        modifier = Modifier
            .height(96.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(GreenLinearGradient),
        containerColor = Color.Transparent,
        tonalElevation = 8.dp
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = currentDest?.route == item.route::class.qualifiedName

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
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
                        contentDescription = stringResource(item.titleResId),
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.titleResId),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White.copy(alpha = 0.7f),
                    indicatorColor = Color.White.copy(alpha = 0.2f)
                )
            )
        }
    }
}