package com.example.tbcacademyfinal.presentation.ui.main.store.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.presentation.ui.main.store.StoreIntent
import com.example.tbcacademyfinal.presentation.ui.main.store.StoreState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterRow(
    state: StoreState,
    onIntent: (StoreIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        CategoryRow(state = state, onIntent = onIntent)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Price Range: ${state.minPrice.toInt()} – ${state.maxPrice.toInt()}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
        RangeSlider(
            value = state.minPrice..state.maxPrice,
            onValueChange = { range ->
                onIntent(
                    StoreIntent.PriceRangeChanged(
                        min = range.start,
                        max = range.endInclusive
                    )
                )
            },
            valueRange = 0f..10000f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = state.hasModelOnly,
                onCheckedChange = { onIntent(StoreIntent.HasModelOnlyChanged(it)) }
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.has_3d_model_only),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}