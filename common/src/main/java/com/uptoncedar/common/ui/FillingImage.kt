package com.uptoncedar.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.SubcomposeAsyncImage
import com.uptoncedar.common.R


/**
 * The Filling Image is used to fill the container with an image, that will be scaled/cropped accordingly.
 */
@Composable
fun FillingImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    imageDescription: String? = ""
) {
    SubcomposeAsyncImage(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        model = imageUrl,
        contentDescription = imageDescription,
        loading = {
            CircularProgressIndicator()
        },
        error = {
            Image(
                painter = painterResource(id = R.drawable.image_placeholder_icon),
                contentDescription = "Image not available",
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        },
        contentScale = ContentScale.Crop
    )
}
