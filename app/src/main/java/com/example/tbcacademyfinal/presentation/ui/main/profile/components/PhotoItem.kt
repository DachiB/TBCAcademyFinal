package com.example.tbcacademyfinal.presentation.ui.main.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.tbcacademyfinal.R
import com.example.tbcacademyfinal.common.ImageLoader
import com.example.tbcacademyfinal.presentation.theme.PlainWhite

@Composable
fun PhotoItem(
    imageUrl: String,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {},
) {
    Box(modifier = modifier.aspectRatio(1f)) {
        Card(
            modifier = modifier.aspectRatio(1f), // Make photo items square
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            ImageLoader.AltLoadImage(
                imageUrl = imageUrl,
                modifier = Modifier.fillMaxSize(),
                localContext = LocalContext.current,
                description = stringResource(R.string.profile_photo_item_desc)
            )
        }
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier
                .align(Alignment.TopEnd) // Position top-right
                .padding(4.dp) // Add some padding
                .size(24.dp) // Explicit size for the button
                .background(
                    Color.Black.copy(alpha = 0.5f),
                    CircleShape
                ), // Semi-transparent background
            colors = IconButtonDefaults.iconButtonColors(contentColor = PlainWhite)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.profile_delete_photo_desc)
            )
        }
    }
}