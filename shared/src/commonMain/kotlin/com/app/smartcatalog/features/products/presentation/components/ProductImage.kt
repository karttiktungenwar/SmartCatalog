package com.app.smartcatalog.features.products.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun ProductImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun ProductThumbnail(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    ProductImage(
        imageUrl = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier.size(72.dp),
    )
}
