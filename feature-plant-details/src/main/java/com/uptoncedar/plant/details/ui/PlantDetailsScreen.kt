package com.uptoncedar.plant.details.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uptoncedar.plant.details.viewmodel.PlantDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailsScreen(
    viewModel: PlantDetailsViewModel = hiltViewModel(),
    plantId: String
) {
    val plantDetails by viewModel.plantDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(plantId) {
        viewModel.fetchPlantDetails(plantId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Plant Details") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (errorMessage != null) {
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error
                )
                Button(onClick = { viewModel.clearError() }) {
                    Text("Dismiss")
                }
            } else if (plantDetails != null) {
                Text(text = "Common Name: ${plantDetails?.common_name}")
                Text(text = "Scientific Name: ${plantDetails?.scientific_name}")
                Text(text = "Family: ${plantDetails?.main_species?.family}")
                Text(text = "Genus: ${plantDetails?.main_species?.genus}")
            } else {
                Text("No plant details found.")
            }
        }
    }
}