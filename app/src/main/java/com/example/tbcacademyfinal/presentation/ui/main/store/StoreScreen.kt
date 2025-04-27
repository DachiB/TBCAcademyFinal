package com.example.tbcacademyfinal.presentation.ui.main.store

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.common.CollectSideEffect
import com.example.tbcacademyfinal.presentation.model.ProductUi
import com.example.tbcacademyfinal.presentation.theme.TBCAcademyFinalTheme

@Composable
fun StoreScreen(
    viewModel: StoreViewModel = hiltViewModel(),
    onNavigateToDetails: (productId: String) -> Unit,
) {
    // Handle side effects like navigation
    val snackbarHostState = remember { SnackbarHostState() }

    CollectSideEffect(flow = viewModel.event) { effect ->
        when (effect) {
            is StoreSideEffect.NavigateToDetails -> onNavigateToDetails(effect.productId)
            is StoreSideEffect.ShowErrorSnackbar -> {
                snackbarHostState.showSnackbar(
                    message = effect.message,
                    duration = SnackbarDuration.Short,
                    actionLabel = "Dismiss",
                    withDismissAction = true
                )
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        StoreScreenContent(
            state = viewModel.state,
            onIntent = viewModel::processIntent
        )
    }
}

@Composable
fun StoreScreenContent(
    state: StoreState,
    onIntent: (StoreIntent) -> Unit // Receive lambda for query change
) {

    if (state.isNetworkAvailable) {
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .width(72.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 8.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
            ) {
                OutlinedTextField(
                    value = state.searchQuery,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { query ->
                        onIntent(StoreIntent.SearchQueryChanged(query))
                    },
                    label = {
                        Text(
                            stringResource(R.string.search_string)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = R.string.search_string.toString()
                        )

                    }
                )
                if (state.isSearching) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .width(72.dp)
                                .align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else if (state.products.isNotEmpty()) {
                    ProductGrid(
                        state = state,
                        onProductClick = { productId ->
                            onIntent(StoreIntent.ProductClicked(productId))
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = stringResource(R.string.no_such_product_s),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(
                                Alignment.Center
                            )
                        )
                    }
                }
            }

        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "No internet connection",
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(48.dp),
            )
            Text(
                text = "No internet connection",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = { onIntent(StoreIntent.RetryButtonClicked) },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(
                        width = 150.dp,
                        height = 40.dp
                    )

            ) {
                Text(
                    text = "Retry",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
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
            state = StoreState(
                isLoading = false,
                products = sampleProducts,
                isNetworkAvailable = true
            ),
            onIntent = {}
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
