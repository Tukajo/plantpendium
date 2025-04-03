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
import androidx.hilt.navigation.compose.hiltViewModel
import com.uptoncedar.common.ui.FillingImage
import com.uptoncedar.plant.details.viewmodel.PlantDetailsViewModel
import com.uptoncedar.plant.details.R
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uptoncedar.common.model.PlantDetails
import com.uptoncedar.common.ui.theme.Dimensions
import com.uptoncedar.plant.details.domain.PlantDetailsError
import com.uptoncedar.plant.details.viewmodel.PlantDetailsUiState


@Composable
fun PlantDetailsScreen(
    viewModel: PlantDetailsViewModel = hiltViewModel(), plantId: String
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(plantId) {
        viewModel.fetchPlantDetails(plantId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.PaddingMedium)
    ) {
        when (uiState) {
            is PlantDetailsUiState.Loading -> LoadingIndicator(modifier = Modifier.align(Alignment.Center))
            is PlantDetailsUiState.Error -> ErrorView(
                error = (uiState as PlantDetailsUiState.Error).error,
                onRetry = { viewModel.fetchPlantDetails(plantId) },
                modifier = Modifier.align(Alignment.Center)
            )

            is PlantDetailsUiState.Success -> PlantDetailsContent(
                plantDetails = (uiState as PlantDetailsUiState.Success).plantDetails
            )

        }
    }
}

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier)
}

@Composable
fun ErrorView(error: PlantDetailsError, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    val errorMessage = when (error) {
        is PlantDetailsError.PlantNotFound -> stringResource(R.string.error_plant_not_found)
        is PlantDetailsError.NetworkError -> stringResource(R.string.error_network)
        is PlantDetailsError.UnknownError -> stringResource(R.string.error_unknown)
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.error_prefix) + errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(Dimensions.PaddingSmall))
        Button(onClick = onRetry) {
            Text(stringResource(android.R.string.ok))
        }
    }
}

@Composable
fun PlantDetailsContent(
    plantDetails: PlantDetails, modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimensions.PaddingMedium)
    ) {
        plantDetails.main_species.image_url.let { imageUrl ->
            PlantImageCard(
                imageUrl = imageUrl, description = plantDetails.common_name, onDoubleClick = {
                    val intent = Intent(Intent.ACTION_VIEW, imageUrl.toUri())
                    context.startActivity(intent)
                })
        }

        CommonInfoCard(commonName = plantDetails.common_name)

        ScientificInfoCard(
            scientificName = plantDetails.scientific_name,
            family = plantDetails.main_species.family,
            genus = plantDetails.main_species.genus
        )
    }
}


@Composable
fun PlantImageCard(
    imageUrl: String, description: String?, onDoubleClick: () -> Unit, modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = { onDoubleClick() })
            }, elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.CardElevation)
    ) {
        FillingImage(
            imageUrl = imageUrl,
            imageDescription = description,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.ImageHeight),
        )
    }
}

@Composable
fun CommonInfoCard(commonName: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(Dimensions.PaddingMedium)) {
            Text(
                text = stringResource(R.string.common_section_title),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(Dimensions.PaddingSmall))
            InfoRow(label = stringResource(R.string.common_name_field_label), value = commonName)
        }
    }
}

@Composable
fun ScientificInfoCard(
    scientificName: String?, family: String?, genus: String?, modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(Dimensions.PaddingMedium)) {
            Text(
                text = stringResource(R.string.scientific_classification_section_title),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(Dimensions.PaddingSmall))
            scientificName?.let {
                InfoRow(
                    label = stringResource(R.string.scientific_name_field_label), value = it
                )
            }
            family?.let {
                InfoRow(
                    label = stringResource(R.string.scientific_family_field_label), value = it
                )
            }
            genus?.let {
                InfoRow(
                    label = stringResource(R.string.scientific_genus_field_label), value = it
                )
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row {
        Text(
            text = label, style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.width(Dimensions.PaddingSmall))
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
    Spacer(modifier = Modifier.height(Dimensions.PaddingSmall))
}