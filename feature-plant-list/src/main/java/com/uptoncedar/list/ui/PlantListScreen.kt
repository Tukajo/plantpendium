package com.uptoncedar.list.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.uptoncedar.list.viewmodel.PlantListViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue



@Composable
fun PlantListScreen(
    modifier: Modifier = Modifier,
    viewModel: PlantListViewModel = hiltViewModel()
) {
    val plants by viewModel.plants.collectAsState()
    if(plants.isNotEmpty()) {
        LazyColumn {
            items(plants) {
                PlantCard(it)
            }
        }
    }

}

