package com.example.tbcacademyfinal.presentation.ui.main.collection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.common.safecalls.CollectSideEffect
import com.example.tbcacademyfinal.presentation.model.CollectionItemUi
import com.example.tbcacademyfinal.presentation.theme.TBCAcademyFinalTheme

@Composable
fun CollectionScreen(
    // Needed to navigate away to AR screen
    viewModel: CollectionViewModel = hiltViewModel(),
    onNavigateToArScreen: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    CollectSideEffect(flow = viewModel.event) { effect ->
        when (effect) {
            is CollectionSideEffect.NavigateToArScreen -> {
                // TODO: Navigate to the actual AR Activity/Screen
                onNavigateToArScreen()
                println("Navigate to AR Screen (Not Implemented)")
                snackbarHostState.showSnackbar("AR Screen navigation not implemented.")
            }

            is CollectionSideEffect.ShowError -> {
                snackbarHostState.showSnackbar(
                    message = effect.message,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        ArCollectionScreenContent(
            state = viewModel.state,
            snackbarHostState = snackbarHostState,
            onIntent = viewModel::processIntent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArCollectionScreenContent(
    state: CollectionState,
    snackbarHostState: SnackbarHostState,
    onIntent: (CollectionIntent) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.bottom_nav_ar_collection)) },
                actions = { // Add Clear All button to TopAppBar
                    if (state.items.isNotEmpty() && !state.isLoading) {
                        IconButton(onClick = { onIntent(CollectionIntent.ClearCollection) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.collection_clear_all) // Add string
                            )
                        }
                    }
                }
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.error != null) {
                Text(
                    text = "Error: ${state.error}",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
                Button(
                    onClick = { onIntent(CollectionIntent.LoadCollection) },
                    modifier = Modifier.align(Alignment.Center)
                ) {

                }
            } else if (state.items.isEmpty()) {
                Text(
                    text = stringResource(R.string.collection_empty_message), // Add string
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp) // Padding for FAB
                ) {
                    items(state.items, key = { item -> item.productId }) { item ->
                        CollectionListItem(
                            item = item,
                            onRemoveClick = { onIntent(CollectionIntent.RemoveItem(item.productId)) }
                        )
                        HorizontalDivider()
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)

                ) {
//                    ExtendedFloatingActionButton(
//                        onClick = { onIntent(CollectionIntent.StartDecorating) },
//                        icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = null) },
//                        text = { Text(stringResource(R.string.buy_collection)) } // Add string
//                    )
//                    Spacer(modifier = Modifier.width(16.dp))
                    ExtendedFloatingActionButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onIntent(CollectionIntent.StartDecorating) },
                        icon = { Icon(Icons.Filled.Star, contentDescription = null) },
                        text = { Text(stringResource(R.string.collection_start_decorating)) } // Add string
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArCollectionScreenContentPreview() {
    TBCAcademyFinalTheme {
        val sampleItems = listOf(
            CollectionItemUi("p1", "Modern Sofa", "", "m1"),
            CollectionItemUi("p2", "Minimalist Lamp", "", "m2")
        )
        ArCollectionScreenContent(
            state = CollectionState(isLoading = false, items = sampleItems),
            snackbarHostState = SnackbarHostState(),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArCollectionScreenEmptyPreview() {
    TBCAcademyFinalTheme {
        ArCollectionScreenContent(
            state = CollectionState(isLoading = false, items = emptyList()),
            snackbarHostState = SnackbarHostState(),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CollectionListItemPreview() {
    TBCAcademyFinalTheme {
        CollectionListItem(
            item = CollectionItemUi(
                "p1",
                "Modern Sofa",
                "",
                "m1"
            ),
            onRemoveClick = {}
        )
    }
}