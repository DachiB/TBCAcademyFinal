package com.example.tbcacademyfinal.presentation.ui.main.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.common.ImageLoader
import com.example.tbcacademyfinal.common.safecalls.CollectSideEffect
import com.example.tbcacademyfinal.presentation.model.ProductUi
import com.example.tbcacademyfinal.presentation.theme.TBCAcademyFinalTheme


@Composable
fun DetailsScreen(

    viewModel: DetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToModelScene: (productId: String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    CollectSideEffect(flow = viewModel.event) { effect ->
        when (effect) {
            is DetailsSideEffect.NavigateBack -> onNavigateBack()
            is DetailsSideEffect.ShowAddedToCollectionMessage -> {
                snackbarHostState.showSnackbar(
                    message = "Added to collection!",
                    duration = SnackbarDuration.Short,
                    actionLabel = "Dismiss",
                    withDismissAction = true
                )
            }

            is DetailsSideEffect.ShowError -> {
                snackbarHostState.showSnackbar(
                    message = "Error: ${effect.message}",
                    duration = SnackbarDuration.Long,
                    actionLabel = "Dismiss",
                    withDismissAction = true
                )
            }

            is DetailsSideEffect.NavigateToModel -> onNavigateToModelScene(effect.productId)
        }
    }

    DetailsScreenContent(
        state = viewModel.state,
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
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading && state.product == null -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.error != null -> {
                    Text(
                        text = "Error: ${state.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                state.product != null -> {
                    ProductDetails(
                        product = state.product,
                        isAddedToCollection = state.isAddedToCollection,
                        onAddToCollection = { onIntent(DetailsIntent.AddToCollectionClicked) },
                        onViewModelClicked = { onIntent(DetailsIntent.ClickedModel(state.product.id)) }
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
    onViewModelClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        ImageLoader.LoadImage(
            imageUrl = product.imageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(bottom = 16.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    clip = false
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
            text = "Category: ${product.category}",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onViewModelClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(stringResource(id = R.string.view_model_in_3d))
        }
        Button(
            onClick = onAddToCollection,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = !isAddedToCollection
        ) {
            val icon =
                if (isAddedToCollection) Icons.Filled.Check else Icons.Filled.ShoppingCart
            val textResId =
                if (isAddedToCollection) R.string.details_added_to_collection else R.string.details_add_to_collection

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
            1299.99,
            "$1299.99",
            "Sofas",
            "", ""
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
            ProductUi("p1", "Modern Sofa", "Desc"
                , 1299.99, "$1299.99", "", "", "")
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