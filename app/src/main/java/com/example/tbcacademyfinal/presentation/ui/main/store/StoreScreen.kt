package com.example.tbcacademyfinal.presentation.ui.main.store

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.presentation.model.ProductUi
import com.example.tbcacademyfinal.presentation.theme.TBCAcademyFinalTheme
import com.example.tbcacademyfinal.util.CollectSideEffect

@Composable
fun StoreScreen(
    viewModel: StoreViewModel = hiltViewModel(),
    onNavigateToDetails: (productId: String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
        onProductClick = { productId ->
            viewModel.processIntent(StoreIntent.ProductClicked(productId))
        },
        // Pass other lambdas for retry, filter, etc. if added later
    )
}

@Composable
fun StoreScreenContent(
    state: StoreState,
    onProductClick: (productId: String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading && state.products.isEmpty()) { // Show full screen loader only on initial load
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (state.error != null && state.products.isEmpty()) { // Show error only if no products loaded
            Text(
                text = "Error: ${state.error}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center).padding(16.dp)
            )
            // TODO: Add a retry button here
        } else {
            // Display Product Grid
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp), // Adjust minSize as needed
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.products, key = { product -> product.id }) { product ->
                    ProductItem(
                        product = product,
                        onClick = { onProductClick(product.id) }
                    )
                }

                // Optional: Add loading indicator at the bottom if loading more pages
                if (state.isLoading && state.products.isNotEmpty()) {
                    item { // GridItemSpan Scope not needed for single item here
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    product: ProductUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .crossfade(true) // Enable crossfade animation
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_background), // Add a placeholder drawable
                error = painterResource(R.drawable.ic_launcher_background), // Use same or error specific drawable
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f), // Make image square-ish
                contentScale = ContentScale.Crop // Crop image to fit aspect ratio
            )
            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.formattedPrice,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary // Highlight price maybe
                )
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
            onProductClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StoreScreenLoadingPreview() {
    TBCAcademyFinalTheme {
        StoreScreenContent(
            state = StoreState(isLoading = true),
            onProductClick = {}
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