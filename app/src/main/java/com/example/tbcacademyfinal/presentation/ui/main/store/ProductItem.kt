package com.example.tbcacademyfinal.presentation.ui.main.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.presentation.model.ProductUi
import com.example.tbcacademyfinal.util.ImageLoader

@Composable
fun ProductItem(
    product: ProductUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            ImageLoader.LoadImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                imageUrl = product.imageUrl,
                description = product.name,
                localContext = LocalContext
            )
//            SubcomposeAsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(product.imageUrl)
//                    .crossfade(true)
//                    .build(),
//                loading = {
//                    CircularProgressIndicator(
//                        modifier = Modifier
//                            .size(24.dp)
//                            .padding(64.dp)
//                            .align(Alignment.Center)
//                    )
//                },
//                contentDescription = product.name,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(1f),
//                contentScale = ContentScale.Crop,
//                error = {
//                    Image(
//                        painter = painterResource(R.drawable.ic_launcher_background),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .aspectRatio(1f)
//                    )
//                }
//            )
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(product.imageUrl)
//                    .crossfade(true)
//                    .build(),
//                placeholder = painterResource(R.drawable.ic_launcher_background), // Add a placeholder drawable
//                error = painterResource(R.drawable.ic_launcher_background), // Use same or error specific drawable
//                contentDescription = product.name,
//                onLoading = {
//
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(1f),
//                contentScale = ContentScale.Crop, // Crop image to fit aspect ratio
//            )
            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.formattedPrice,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary // Highlight price maybe
                )
            }
        }
    }
}