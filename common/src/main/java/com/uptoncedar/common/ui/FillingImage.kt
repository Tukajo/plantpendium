package com.uptoncedar.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.SubcomposeAsyncImage
import com.uptoncedar.common.R

/**
 * The Filling Image is used to fill the container with an image, that will be scaled/cropped accordingly.
 */
@Composable
fun FillingImage(
    modifier: Modifier = Modifier,
    imageUrl: String? = "",
    imageDescription: String?
) {
    SubcomposeAsyncImage(
        modifier = modifier
            .fillMaxSize(),
        model = imageUrl,
        contentDescription = imageDescription
            ?: stringResource(R.string.fallback_filling_image_description),
        loading = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        },
        error = {
            Image(
                painter = painterResource(id = R.drawable.image_placeholder_icon),
                contentDescription = "Image not available",
                modifier = Modifier.fillMaxWidth(0.5f), // Adjust size as needed
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )

        },
        contentScale = ContentScale.Crop
    )
}


@Preview(showBackground = true)
@Composable
fun FillingImagePreview() {
    MaterialTheme {
        FillingImage(
            imageUrl = "https://fake.website.com/1.png",
            imageDescription = "Example Image"
        )
    }
}

@Preview(showBackground = true, widthDp = 200, heightDp = 100)
@Composable
fun FillingImageSmallPreview() {
    MaterialTheme {
        FillingImage(
            imageUrl = "https://fake.website.com/1.png",
            imageDescription = "Small Example"
        )
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
fun FillingImageErrorPreview() {
    MaterialTheme {
        FillingImage(
            imageUrl = "invalid_url",
            imageDescription = "Error State Example"
        )
    }
}