package com.example.tbcacademyfinal.presentation.ui.main.store

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tbcacademyfinal.common.CollectSideEffect
import com.example.tbcacademyfinal.presentation.model.ProductUi
import com.example.tbcacademyfinal.presentation.theme.TBCAcademyFinalTheme

@Composable
fun StoreScreen(
    viewModel: StoreViewModel = hiltViewModel(),
    onNavigateToDetails: (productId: String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    // Handle side effects like navigation
    CollectSideEffect(flow = viewModel.event) { effect ->
        when (effect) {
            is StoreSideEffect.NavigateToDetails -> onNavigateToDetails(effect.productId)
            is StoreSideEffect.ShowErrorSnackbar -> {
                println("StoreScreen Error: ${effect.message}")
            }
        }
    }

    StoreScreenContent(
        state = state,
        searchQuery = searchQuery,
        onProductClick = { productId ->
            viewModel.processIntent(StoreIntent.ProductClicked(productId))
        },
        onSearchQueryChange = { query ->
            viewModel.processIntent(StoreIntent.SearchQueryChanged(query))
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StoreScreenContent(
    state: StoreState,
    searchQuery: String, // Receive current query
    onProductClick: (productId: String) -> Unit,
    onSearchQueryChange: (query: String) -> Unit // Receive lambda for query change
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange, // Call lambda on change
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            label = { Text("Search Furniture") }, // TODO: String resource
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search"
                )
            }, // TODO: String resource
            singleLine = true,
            colors = TextFieldDefaults.colors() // Use default M3 colors
        )

        // Content Area (Loading/Error/Grid)
        Box(modifier = Modifier.fillMaxSize()) { // Change to fill remaining space
            if (state.isLoading && state.products.isEmpty()) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .width(72.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary // Use primary color for indicator
                )
            } else if (state.error != null && state.products.isEmpty()) {
                Text(text = "Error: ${state.error}")
            } else if (!state.isLoading && state.products.isEmpty() && searchQuery.isNotEmpty()) {
                // Show "No results" message when searching and empty
                Text(
                    text = "No results found for \"$searchQuery\"", // TODO: String resource
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            } else if (!state.isLoading && state.products.isEmpty() && searchQuery.isBlank()) {
                // Show "Empty store" only if not loading, not searching, and empty
                Text(
                    text = "Store is empty.", // TODO: String resource
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            } else {
                // Display Product Grid
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp), // Adjust minSize as needed
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp,start = 16.dp, end = 16.dp),
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

                    // Optional: Add loading indicator at the bottom if loading more pages
                    if (state.isLoading && state.products.isNotEmpty()) {
                        item { // GridItemSpan Scope not needed for single item here
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                LinearProgressIndicator(
                                    modifier = Modifier
                                        .width(72.dp)
                                        .align(Alignment.Center),
                                    color = MaterialTheme.colorScheme.primary // Use primary color for indicator
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


// Add placeholder drawable `ic_placeholder_image.xml` to `res/drawable`
// Example vector placeholder:
/*
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24.0"
    android:viewportHeight="24.0"
    android:tint="?attr/colorControlNormal">
  <path
      android:fillColor="@android:color/darker_gray"
      android:pathData="M21,19V5c0,-1.1 -0.9,-2 -2,-2H5c-1.1,0 -2,0.9 -2,2v14c0,1.1 0.9,2 2,2h14c1.1,0 2,-0.9 2,-2zM8.5,13.5l2.5,3.01L14.5,12l4.5,6H5l3.5,-4.5z"/>
</vector>
*/


// --- Previews ---
@Preview(showBackground = true)
@Composable
fun StoreScreenContentPreview() {
    TBCAcademyFinalTheme {
        val sampleProducts = listOf(
            ProductUi("p1", "Modern Sofa", "Desc", "$1299.99", "", "Sofas", "m1"),
            ProductUi("p2", "Minimalist Lamp", "Desc", "$149.50", "", "Lighting", "m2"),
            ProductUi("p3", "Oak Coffee Table", "Desc", "$399.00", "", "Tables", "m3")
        )
        StoreScreenContent(
            state = StoreState(isLoading = false, products = sampleProducts),
            onProductClick = {},
            searchQuery = "",
            onSearchQueryChange = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ProductItemPreview() {
    TBCAcademyFinalTheme {
        ProductItem(
            product = ProductUi("p1", "Modern Sofa", "Desc", "$1299.99", "", "Sofas", "m1"),
            onClick = {}
        )
    }
}
