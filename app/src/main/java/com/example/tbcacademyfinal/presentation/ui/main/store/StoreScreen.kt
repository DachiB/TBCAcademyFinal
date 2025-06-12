package com.example.tbcacademyfinal.presentation.ui.main.store

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.common.safecalls.CollectSideEffect
import com.example.tbcacademyfinal.presentation.model.ProductUi
import com.example.tbcacademyfinal.presentation.theme.GreenLinearGradient
import com.example.tbcacademyfinal.presentation.theme.PlainWhite
import com.example.tbcacademyfinal.presentation.theme.TBCAcademyFinalTheme
import com.example.tbcacademyfinal.presentation.ui.main.store.components.CategoryRow
import com.example.tbcacademyfinal.presentation.ui.main.store.components.FilterRow
import com.example.tbcacademyfinal.presentation.ui.main.store.components.HighlightsSection
import com.example.tbcacademyfinal.presentation.ui.main.store.components.ProductGrid
import com.example.tbcacademyfinal.presentation.ui.main.store.components.ProductItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    viewModel: StoreViewModel = hiltViewModel(),
    onNavigateToDetails: (productId: String) -> Unit,
) {
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
        }, topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = PlainWhite
                ),
                modifier = Modifier.background(GreenLinearGradient)
            )
        }
    ) { paddingValues ->
        StoreScreenContent(
            modifier = Modifier.padding(paddingValues),
            state = viewModel.state,
            onIntent = viewModel::processIntent
        )
    }
}

@Composable
fun StoreScreenContent(
    modifier: Modifier = Modifier,
    state: StoreState,
    onIntent: (StoreIntent) -> Unit
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
                modifier = modifier.padding(
                    start = 16.dp, end = 16.dp, top = 16.dp
                ),
            ) {
                Spacer(modifier = Modifier.height(0.dp))
                AnimatedVisibility(
                    visible = !state.isFiltering && !state.isSearching,
                    enter =
                    fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    HighlightsSection(
                        state = state,
                        onIntent = onIntent,
                    )
                }


                Spacer(modifier = Modifier.height(12.dp))
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
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = R.string.search_string.toString(),
                            modifier = Modifier.clickable {
                                onIntent(
                                    StoreIntent.FilterButtonClicked(
                                        state.isFiltering.not()
                                    )
                                )
                            }
                        )

                    }
                )
                if (state.isFiltering) {
                    FilterRow(
                        state = state,
                        onIntent = onIntent
                    )
                } else {
                    CategoryRow(
                        state = state,
                        onIntent = onIntent
                    )
                }

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
                } else if (state.currentProducts.isNotEmpty()) {
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

@Preview(showBackground = true)
@Composable
fun StoreScreenContentPreview() {
    TBCAcademyFinalTheme {
        val sampleProducts = listOf(
            ProductUi("p1", "Modern Sofa", "Desc", 1299.99, "$1299.99", "", "", ""),
            ProductUi("p2", "Minimalist Lamp", "Desc", 149.50, "$149.50", "", "", ""),
            ProductUi("p3", "Oak Coffee Table", "Desc", 399.00, "$399.00", "", "", "")
        )
        StoreScreenContent(
            state = StoreState(
                isLoading = false,
                currentProducts = sampleProducts,
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
            product = ProductUi("p1", "Modern Sofa", "Desc", 1299.99, "$1299.99", "", "", ""),
            onClick = {}
        )
    }
}
