package com.uptoncedar.common.ui

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoomableDialog(
    modifier: Modifier? = Modifier,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier ?: Modifier,
        properties = DialogProperties(),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { centroid, pan, zoom, rotation ->
                            scale *= zoom
                            val oldOffsetX = offsetX
                            val oldOffsetY = offsetY
                            offsetX += pan.x
                            offsetY += pan.y
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offsetX,
                            translationY = offsetY
                        )
                ) {
                    content()
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ZoomableDialogPreview() {
    androidx.compose.material3.MaterialTheme {
        var showDialog by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            androidx.compose.material3.Button(onClick = { showDialog = true }) {
                androidx.compose.material3.Text("Open Zoomable Content")
            }
            if (showDialog) {
                ZoomableDialog(
                    onDismissRequest = { showDialog = false }
                ) {
                    FillingImage(
                        imageUrl = "https://fake.website.com/large_image.png",
                        imageDescription = "Large Example Image"
                    )
                }
            }
        }
    }
}