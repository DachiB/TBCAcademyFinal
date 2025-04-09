package com.example.tbcacademyfinal.presentation.ui.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun ARPreviewScreen(productId: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("AR Preview for product: $productId", fontSize = 24.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun ARPreviewScreenPreview() {
    ARPreviewScreen(productId = "")
}