package com.uptoncedar.plant.details.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uptoncedar.common.ui.FillingImage
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (errorMessage != null) {
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(onClick = { viewModel.clearError() }) {
                    Text("Dismiss")
                }
            } else if (plantDetails != null) {
                // Image Section
                plantDetails?.main_species?.image_url?.let { imageUrl ->
                    FillingImage(
                        imageUrl = imageUrl,
                        imageDescription = plantDetails?.common_name ?: "Plant Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) // Adjust height as needed
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Common Information Section
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Common Information",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        plantDetails?.common_name?.let {
                            Row {
                                Text(
                                    text = "Common Name:",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = it, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }

                // Scientific Classification Section
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Scientific Classification",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        plantDetails?.scientific_name?.let {
                            Row {
                                Text(
                                    text = "Scientific Name:",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = it, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                        plantDetails?.main_species?.family?.let {
                            Row {
                                Text(
                                    text = "Family:",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = it, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                        plantDetails?.main_species?.genus?.let {
                            Row {
                                Text(
                                    text = "Genus:",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = it, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }

                // You can add more sections here for other details
            } else {
                Text("No plant details found.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}