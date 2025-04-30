package com.example.tbcacademyfinal.presentation.ui.main.store.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tbcacademyfinal.presentation.ui.main.store.StoreState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductGrid(
    state: StoreState,
    onProductClick: (productId: String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(state.currentProducts, key = { product -> product.id }) { product ->
            ProductItem(
                product = product,
                onClick = { onProductClick(product.id) },
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}