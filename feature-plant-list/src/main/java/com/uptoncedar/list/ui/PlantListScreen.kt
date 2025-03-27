package com.uptoncedar.list.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.uptoncedar.list.viewmodel.PlantListViewModel
import androidx.compose.material3.Text


@Composable
fun PlantListScreen(modifier: Modifier = Modifier, viewModel: PlantListViewModel = hiltViewModel<PlantListViewModel>()) {
    val nameTemp: String = viewModel.getName()
    Text(
        text = "Hello $nameTemp!",
        modifier = modifier
    )

}