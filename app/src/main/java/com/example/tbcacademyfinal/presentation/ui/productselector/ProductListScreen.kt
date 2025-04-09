package com.example.tbcacademyfinal.presentation.ui.productselector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tbcacademyfinal.domain.model.ProductDomain


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductListScreen(
    onProductSelected: (ProductDomain) -> Unit,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val products = viewModel.productList

    LazyColumn {
        items(products) { product ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProductSelected(product) },
                text = { Text(product.name) },
                secondaryText = { Text("Tap to preview in AR") }
            )
            Divider()
        }
    }
}
