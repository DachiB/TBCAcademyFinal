package com.example.tbcacademyfinal.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.tbcacademyfinal.R

object ImageLoader {
    @Composable
    fun LoadImage(
        modifier: Modifier = Modifier,
        imageUrl: String,
        description: String? = null,
        localContext: ProvidableCompositionLocal<Context>
    ) {
        AsyncImage(
            model = ImageRequest.Builder(localContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = description,
            placeholder = painterResource(R.drawable.ic_launcher_background),
            error = painterResource(R.drawable.ic_launcher_background),
            modifier = modifier,
            contentScale = ContentScale.Crop,
            clipToBounds = true// Fit the whole image
        )
    }

    @Composable
    fun AltLoadImage() {}
}