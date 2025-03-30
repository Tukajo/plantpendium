package com.uptoncedar.plant.details.ui

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uptoncedar.common.ui.FillingImage
import com.uptoncedar.plant.details.viewmodel.PlantDetailsViewModel
import com.uptoncedar.plant.details.R
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.core.net.toUri

@Composable
fun PlantDetailsScreen(
    viewModel: PlantDetailsViewModel = hiltViewModel(), plantId: String
) {
    val plantDetails by viewModel.plantDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(plantId) {
        viewModel.fetchPlantDetails(plantId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (isLoading) {
            true -> CircularProgressIndicator()
            else -> {
                if (errorMessage != null) {
                    Text(
                        text = "Error: $errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(onClick = { viewModel.clearError() }) {
                        Text(stringResource(android.R.string.ok))
                    }
                } else if (plantDetails != null) {
                    plantDetails?.main_species?.image_url?.let { imageUrl ->

                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onDoubleTap = {
                                            val intent =
                                                Intent(Intent.ACTION_VIEW, imageUrl.toUri())
                                            context.startActivity(intent)
                                        }
                                    )
                                },
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 8.dp
                            )
                        ) {
                            FillingImage(
                                imageUrl = imageUrl,
                                imageDescription = plantDetails?.common_name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.common_section_title),
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            plantDetails?.common_name?.let {
                                Row {
                                    Text(
                                        text = stringResource(R.string.common_name_field_label),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = it, style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                        }
                    }
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.scientific_classification_section_title),
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            plantDetails?.scientific_name?.let {
                                Row {
                                    Text(
                                        text = stringResource(R.string.scientific_name_field_label),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = it, style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                            plantDetails?.main_species?.family?.let {
                                Row {
                                    Text(
                                        text = stringResource(R.string.scientific_family_field_label),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = it, style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                            plantDetails?.main_species?.genus?.let {
                                Row {
                                    Text(
                                        text = stringResource(R.string.scientific_genus_field_label),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = it, style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                        }

                    }
                } else {
                    Text(
                        stringResource(R.string.no_plant_details_found_warning),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}