package com.uptoncedar.list.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.uptoncedar.networking.model.Plant

@Composable
fun PlantCard(
    plant: Plant
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier.size(100.dp),
                model = plant.image_url,
                contentDescription = plant.scientific_name,
                loading = {
                    CircularProgressIndicator()
                },
                error = {
                    Image(
                        painter = painterResource(id = com.uptoncedar.list.R.drawable.image_placeholder_icon),
                        contentDescription = "Image not available",
                        modifier = Modifier.size(100.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                plant.common_name?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                plant.scientific_name?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}