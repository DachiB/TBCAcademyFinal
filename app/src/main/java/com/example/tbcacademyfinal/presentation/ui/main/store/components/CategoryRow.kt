package com.example.tbcacademyfinal.presentation.ui.main.store.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.presentation.theme.WalnutBrown
import com.example.tbcacademyfinal.presentation.ui.main.store.StoreIntent
import com.example.tbcacademyfinal.presentation.ui.main.store.StoreState


@Composable
fun CategoryRow(state: StoreState, onIntent: (StoreIntent) -> Unit) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = state.selectedCategory == null,
                onClick = { onIntent(StoreIntent.ClearCategoryFilter) },
                label = {
                    Text(text = "All")
                },
                leadingIcon = if (state.selectedCategory == null) {
                    {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = stringResource(R.string.selected_option)
                        )
                    }
                } else {
                    null
                }
            )
        }
        items(state.availableCategories) { category ->
            val isSelected = state.selectedCategory == category
            FilterChip(
                border = BorderStroke(
                    width = 1.dp,
                    color = WalnutBrown
                ),
                selected = isSelected,
                onClick = { onIntent(StoreIntent.CategorySelected(category)) },
                label = {
                    Text(text = category)
                }, leadingIcon = if (isSelected) {
                    {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = stringResource(R.string.selected_option)
                        )
                    }
                } else {
                    null
                })
        }
    }
}
