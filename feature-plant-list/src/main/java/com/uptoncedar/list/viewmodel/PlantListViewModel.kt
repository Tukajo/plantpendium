package com.uptoncedar.list.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uptoncedar.list.domain.GetPlantsByQueryUseCase
import com.uptoncedar.networking.model.Plant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantListViewModel @Inject constructor(
    private val getPlantsByQueryUseCase: GetPlantsByQueryUseCase
) : ViewModel() {

    private val _plants = MutableStateFlow<List<Plant>>(emptyList())
    val plants: StateFlow<List<Plant>> = _plants.asStateFlow()

    fun searchPlants(query: String) {
        viewModelScope.launch {
            try {
                val newPlants = getPlantsByQueryUseCase(query)
                _plants.emit(newPlants)
            } catch (e: Exception) {
                // TODO, handle this error.
            }
        }
    }
}