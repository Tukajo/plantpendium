package com.uptoncedar.list.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    val plants by viewModel.plants.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        PlantListSearchBar(
            onSearchSubmit = {
                viewModel.searchPlants(it)
            })
        LazyColumn {
            items(plants) {
                PlantCard(
                    plantListEntry = it,
                    onNavigateToDetails = onNavigateToDetails
                )
            }
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