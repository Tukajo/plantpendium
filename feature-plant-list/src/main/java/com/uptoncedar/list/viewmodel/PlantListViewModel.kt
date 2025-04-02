package com.uptoncedar.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uptoncedar.common.model.PlantListEntry
import com.uptoncedar.list.viewmodel.PlantListUiState.*
import com.uptoncedar.networking.data.FloraCodexRepository
import com.uptoncedar.networking.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Defines the possible states for the Plant List UI.
 */
sealed interface PlantListUiState {
    /** Represents a successful state with a list of plants. */
    data class Success(val plants: List<PlantListEntry>) : PlantListUiState

    /** Represents an error state during data fetching. */
    data object Error : PlantListUiState

    /** Represents a state where data is currently being loaded. */
    data object Loading : PlantListUiState

    /** Represents an empty state when no data is available. */
    data object Empty : PlantListUiState
}

@HiltViewModel
class PlantListViewModel @Inject constructor(
    private val floraCodexRepository: FloraCodexRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlantListUiState>(PlantListUiState.Empty)

    val uiState: StateFlow<PlantListUiState> = _uiState.asStateFlow()


    /**
     * Called when the user submits a search query.
     * Triggers fetching plants based on the query.
     *
     * @param query The search string entered by the user.
     */
    fun onPlantQuerySubmitted(query: String) {
        fetchPlants(query)
    }

    /**
     * Fetches plant data from the repository based on the provided query.
     * Updates the UI state accordingly (Loading, Success, Error).
     *
     * @param query The search query. If empty or blank, fetches the default/all list.
     */
    private fun fetchPlants(query: String) {
        _uiState.value = Loading
        viewModelScope.launch {
            val result: Result<List<PlantListEntry>> = floraCodexRepository.getPlantsByQuery(query)

            _uiState.value = when (result) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        Empty
                    } else {
                        Success(result.data)
                    }
                }

                is Result.Failure -> {
                    Error
                }

                Result.Loading -> Loading
            }
        }
    }
}