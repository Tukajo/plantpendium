package com.uptoncedar.list.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.uptoncedar.list.viewmodel.PlantListViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun PlantListScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetails: (plantId: String) -> Unit,
    viewModel: PlantListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column(modifier = modifier) {
        Column(modifier = modifier.fillMaxSize()) {
            SearchBar(
                onSearchSubmit = viewModel::onPlantQuerySubmitted
            )
            PlantListContent(
                uiState = uiState,
                onNavigateToDetails = onNavigateToDetails,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PlantListScreenPreview() {
    MaterialTheme {
        PlantListScreen(
            modifier = Modifier,
            onNavigateToDetails = {},
        )
    }
}