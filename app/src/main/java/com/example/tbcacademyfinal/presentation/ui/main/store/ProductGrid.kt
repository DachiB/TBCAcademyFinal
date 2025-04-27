package com.example.tbcacademyfinal.presentation.ui.main.store

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductGrid(
    state: StoreState,
    onProductClick: (productId: String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp), // Adjust minSize as needed
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(state.products, key = { product -> product.id }) { product ->
            ProductItem(
                product = product,
                onClick = { onProductClick(product.id) },
                modifier = Modifier.animateItemPlacement()
            )
        }


//        if (state.isLoading && state.products.isNotEmpty()) {
//            item {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    LinearProgressIndicator(
//                        modifier = Modifier
//                            .width(72.dp)
//                            .align(Alignment.Center),
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                }
//            }
//        }
    }
}