package com.example.tbcacademyfinal.presentation.ui.main.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check // For Added to collection state
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.presentation.model.ProductUi
import com.example.tbcacademyfinal.presentation.theme.TBCAcademyFinalTheme
import com.example.tbcacademyfinal.common.CollectSideEffect
import com.example.tbcacademyfinal.common.ImageLoader


@Composable
fun DetailsScreen(
    // Product ID is implicitly retrieved by the ViewModel via SavedStateHandle
    onNavigateBack: () -> Unit, // Needed for back navigation
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() } // For showing messages

    // Handle Side Effects (e.g., navigation, snackbars)
    CollectSideEffect(flow = viewModel.event) { effect ->
        when (effect) {
            is DetailsSideEffect.NavigateBack -> onNavigateBack()
            is DetailsSideEffect.ShowAddedToCollectionMessage -> {
                snackbarHostState.showSnackbar(
                    message = "Added to collection!", // TODO: Use string resource
                    duration = SnackbarDuration.Short
                )
            }

            is DetailsSideEffect.ShowError -> {
                snackbarHostState.showSnackbar(
                    message = "Error: ${effect.message}", // TODO: Use string resource
                    duration = SnackbarDuration.Long
                )
            }
        }
    }

    DetailsScreenContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::processIntent,
        onNavigateBack = { onNavigateBack() } // Direct navigation back
    )
}

@OptIn(ExperimentalMaterial3Api::class) // For TopAppBar, Scaffold
@Composable
fun DetailsScreenContent(
    state: DetailsState,
    snackbarHostState: SnackbarHostState,
    onIntent: (DetailsIntent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        state.product?.name ?: stringResource(R.string.details_title)
                    )
                }, // Show product name or default
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { // Use direct navigation
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface, // Or background
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                // Initial Loading State
                state.isLoading && state.product == null -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                // Error State
                state.error != null -> {
                    Text(
                        text = "Error: ${state.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                    // TODO: Add retry mechanism?
                }
                // Success State
                state.product != null -> {
                    ProductDetails(
                        product = state.product,
                        isAddedToCollection = state.isAddedToCollection,
                        onAddToCollection = { onIntent(DetailsIntent.AddToCollectionClicked) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductDetails(
    product: ProductUi,
    isAddedToCollection: Boolean,
    onAddToCollection: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Make content scrollable
            .padding(16.dp)
    ) {
        // Product Image
        ImageLoader.LoadImage(
            imageUrl = product.imageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // Adjust aspect ratio for details view
                .padding(bottom = 16.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    clip = false // we’ll clip separately
                )
                .clip(MaterialTheme.shapes.large),
            localContext = LocalContext.current
        )

        Text(
            text = product.name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = product.formattedPrice,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Category: ${product.category}", // TODO: Use string resource
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Spacer(modifier = Modifier.weight(1f)) // Push button towards bottom

        Button(
            onClick = onAddToCollection,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = !isAddedToCollection // Disable if already added
        ) {
            val icon =
                if (isAddedToCollection) Icons.Filled.Check else Icons.Filled.ShoppingCart // Use appropriate icons
            val textResId =
                if (isAddedToCollection) R.string.details_added_to_collection else R.string.details_add_to_collection // Add strings

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(stringResource(id = textResId))
        }
    }
}

@Preview(showBackground = true, name = "Details Screen Content")
@Composable
fun DetailsScreenContentPreview() {
    TBCAcademyFinalTheme() {
        val sampleProduct = ProductUi(
            "p1",
            "Modern Sofa",
            "A very comfortable sofa for your living room.",
            "$1299.99",
            "",
            "Sofas",
            "m1"
        )
        DetailsScreenContent(
            state = DetailsState(isLoading = false, product = sampleProduct),
            snackbarHostState = SnackbarHostState(),
            onIntent = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Details Screen Loading")
@Composable
fun DetailsScreenLoadingPreview() {
    TBCAcademyFinalTheme {
        DetailsScreenContent(
            state = DetailsState(isLoading = true),
            snackbarHostState = SnackbarHostState(),
            onIntent = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Details Screen Added")
@Composable
fun DetailsScreenAddedPreview() {
    TBCAcademyFinalTheme {
        val sampleProduct =
            ProductUi("p1", "Modern Sofa", "Desc", "$1299.99", "", "Sofas", "m1")
        DetailsScreenContent(
            state = DetailsState(
                isLoading = false,
                product = sampleProduct,
                isAddedToCollection = true
            ),
            snackbarHostState = SnackbarHostState(),
            onIntent = {},
            onNavigateBack = {}
        )
    }
}