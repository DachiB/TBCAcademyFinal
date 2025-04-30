package com.example.tbcacademyfinal.presentation.ui.main.store.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.common.ImageLoader
import com.example.tbcacademyfinal.presentation.model.ProductUi
import com.example.tbcacademyfinal.presentation.theme.PlainWhite

@Composable
fun CollectionOfDayCard(
    dailyCollection: List<ProductUi>,
    onAddToCollection: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(300.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(stringResource(R.string.collection_of_day), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            if (dailyCollection.isNotEmpty()) {
                ImageLoader.AltLoadImage(
                    imageUrl = dailyCollection[0].imageUrl,
                    description = dailyCollection[0].name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    localContext = LocalContext.current
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    dailyCollection.drop(1).take(3).forEach { product ->
                        ImageLoader.AltLoadImage(
                            imageUrl = product.imageUrl,
                            description = product.name,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(6.dp)),
                            localContext = LocalContext.current
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onAddToCollection,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            ) {
                Text(
                    stringResource(R.string.details_add_to_collection),
                    style = MaterialTheme.typography.labelMedium,
                    color = PlainWhite
                )
            }
        }
    }
}