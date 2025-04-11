package com.example.tbcacademyfinal.presentation.ui.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ARPreviewScreen(productId: String) {
    Column {
        Text(text = "Product ID: $productId")
    }
}

@Preview(showBackground = true)
@Composable
fun ARPreviewScreenPreview() {
    ARPreviewScreen(productId = "")
}