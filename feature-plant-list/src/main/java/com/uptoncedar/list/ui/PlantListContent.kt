package com.uptoncedar.list.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uptoncedar.list.viewmodel.PlantListUiState

@Composable
fun PlantListContent(
    uiState: PlantListUiState,
    onNavigateToDetails: (plantId: String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is PlantListUiState.Loading -> {
                CircularProgressIndicator()
            }

            is PlantListUiState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Error loading plants.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is PlantListUiState.Success -> {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(uiState.plants) {
                        PlantCard(
                            plantListEntry = it,
                            onNavigateToDetails = onNavigateToDetails
                        )
                    }
                }
            }

            PlantListUiState.Empty -> {
                Text(
                    text = "No plants found.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }


    }
}
