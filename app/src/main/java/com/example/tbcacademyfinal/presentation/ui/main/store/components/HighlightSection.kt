package com.example.tbcacademyfinal.presentation.ui.main.store.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tbcacademyfinal.presentation.ui.main.store.StoreIntent
import com.example.tbcacademyfinal.presentation.ui.main.store.StoreState

@Composable
fun HighlightsSection(
    state: StoreState,
    onIntent: (StoreIntent) -> Unit,
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CollectionOfDayCard(
                dailyCollection = state.dailyCollection,
                onAddToCollection = { onIntent(StoreIntent.ClickedAddDailyCollection) }
            )
        }
        item {
            state.dailyItem?.let { product ->
                ItemOfDayCard(
                    state = state,
                    onClickGoToDetails = { onIntent(StoreIntent.ProductClicked(product.id)) },
                    onClickAddToCollection = { onIntent(StoreIntent.ClickedAddItemOfDay) },
                    modifier = Modifier
                        .width(300.dp)
                        .height(256.dp)
                )
            }
        }
    }
}
