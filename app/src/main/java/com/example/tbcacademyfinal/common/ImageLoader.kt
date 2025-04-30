package com.example.tbcacademyfinal.common

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.example.tbcacademyfinal.R

object ImageLoader {
    @Composable
    fun LoadImage(
        modifier: Modifier = Modifier,
        imageUrl: String,
        description: String? = null,
        localContext: Context
    ) {
        AsyncImage(
            model = ImageRequest.Builder(localContext)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = description,
            placeholder = painterResource(R.drawable.ic_launcher_background),
            error = painterResource(R.drawable.ic_launcher_background),
            modifier = modifier,
            contentScale = ContentScale.Crop,
            clipToBounds = true
        )
    }

    @Composable
    fun AltLoadImage(
        modifier: Modifier = Modifier,
        imageUrl: String,
        description: String? = null,
        localContext: Context
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(localContext)
                .data(imageUrl)
                .placeholder(
                    R.drawable.ic_launcher_background
                )
                .error(
                    R.drawable.ic_launcher_background
                )
                .crossfade(500)
                .build(),
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                        .padding(72.dp)
                )
            },
            contentDescription = description,
            modifier = modifier,
            contentScale = ContentScale.Crop,
        )
    }
}