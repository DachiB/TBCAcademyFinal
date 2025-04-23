package com.example.tbcacademyfinal.presentation.ui.main.store

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.tbcacademyfinal.R

@Composable
fun StoreScreen(
//    viewModel: StoreViewModel = hiltViewModel(),
    onNavigateToDetails: (String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = stringResource(id = R.string.bottom_nav_store)) // Use string resource
    }
    // TODO: Implement Store UI (product list, filters, etc.)
    // Example navigation to details:
    // Button(onClick = { mainNavController.navigate(DetailsRoute(productId = "some_id")) }) { ... }
}