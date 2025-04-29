package com.example.tbcacademyfinal.presentation.ui.main.store.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.common.ImageLoader
import com.example.tbcacademyfinal.presentation.theme.PlainWhite
import com.example.tbcacademyfinal.presentation.ui.main.store.StoreState

@Composable
fun ItemOfDayCard(
    state: StoreState,
    onClickAddToCollection: () -> Unit,
    onClickGoToDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = "Item of the Day",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            state.dailyItem?.let {
                ImageLoader.AltLoadImage(
                    imageUrl = it.imageUrl,
                    description = it.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    localContext = LocalContext.current
                )
            }

            Spacer(Modifier.height(8.dp))
            state.dailyItem?.let {
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.height(4.dp))
            state.dailyItem?.let {
                Text(
                    text = it.formattedPrice,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.weight(1f))
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .align(
                        alignment = Alignment.CenterHorizontally
                    )
            ) {
                Button(
                    onClick = onClickGoToDetails,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = stringResource(R.string.go_to_details),
                        style = MaterialTheme.typography.labelSmall,
                        color = PlainWhite,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                    )
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = onClickAddToCollection,
                    modifier = Modifier.weight(1f),
                    enabled = !state.isDailyItemInCollection,
                ) {
                    Text(
                        if (state.isDailyItemInCollection) stringResource(R.string.in_collection) else stringResource(
                            R.string.details_add_to_collection
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = PlainWhite,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

        }
    }
}