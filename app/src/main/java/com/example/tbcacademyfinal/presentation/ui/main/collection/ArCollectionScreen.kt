package com.example.tbcacademyfinal.presentation.ui.main.collection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.tbcacademyfinal.R

@Composable
fun ArCollectionScreen(
    // If needed for external navigation add controller
    // Inject ViewModel later when needed
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = stringResource(id = R.string.bottom_nav_ar_collection)) // Use string resource
    }
    // TODO: Implement Collection List UI, Delete/Clear buttons, Start Decorating button
    // Start Decorating button would likely navigate to a new AR Activity/Screen
}