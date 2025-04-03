package com.uptoncedar.plant.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uptoncedar.common.model.PlantDetails
import com.uptoncedar.plant.details.domain.GetPlantByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.uptoncedar.plant.details.domain.PlantDetailsError

/**
 * Defines the possible states for the Plant Details UI.
 */
sealed interface PlantDetailsUiState {
    /** Represents a successful state with plant details. */
    data class Success(val plantDetails: PlantDetails) : PlantDetailsUiState

    /** Represents an error state during data fetching. */
    data class Error(val error: PlantDetailsError) : PlantDetailsUiState

    /** Represents a state where data is currently being loaded. */
    data object Loading : PlantDetailsUiState
}

@HiltViewModel
class PlantDetailsViewModel @Inject constructor(
    private val getPlantByIdUseCase: GetPlantByIdUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<PlantDetailsUiState>(PlantDetailsUiState.Loading)
    val uiState: StateFlow<PlantDetailsUiState> = _uiState.asStateFlow()


    fun fetchPlantDetails(plantId: String) {
        _uiState.value = PlantDetailsUiState.Loading
        viewModelScope.launch {
            getPlantByIdUseCase(plantId).onSuccess {
                _uiState.value = PlantDetailsUiState.Success(it)
            }.onFailure { error ->
                val uiError = when (error) {
                    is PlantDetailsError -> error
                    else -> PlantDetailsError.UnknownError(error)
                }
                _uiState.value = PlantDetailsUiState.Error(uiError)
            }
        }
    }
}