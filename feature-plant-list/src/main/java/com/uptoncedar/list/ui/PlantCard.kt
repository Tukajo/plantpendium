package com.uptoncedar.list.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.uptoncedar.networking.model.Plant

@Composable
fun PlantCard(
    plant: Plant
) {
    Column {
        Text(text = plant.common_name)
    }
}