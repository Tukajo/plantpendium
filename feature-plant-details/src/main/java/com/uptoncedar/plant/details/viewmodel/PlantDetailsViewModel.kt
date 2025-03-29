package com.uptoncedar.plant.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uptoncedar.plant.details.domain.GetPlantByIdUseCase
import com.uptoncedar.networking.model.PlantDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantDetailsViewModel @Inject constructor(
    private val getPlantByIdUseCase: GetPlantByIdUseCase
) : ViewModel() {

    private val _plantDetails = MutableStateFlow<PlantDetails?>(null)
    val plantDetails: StateFlow<PlantDetails?> = _plantDetails

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchPlantDetails(plantId: String) {
        viewModelScope.launch {
            _isLoading.update { true }
            _errorMessage.update { null }
            try {
                val plant = getPlantByIdUseCase(plantId)
                _plantDetails.update { plant }
            } catch (e: Exception) {
                _errorMessage.update { e.localizedMessage ?: "An unexpected error occurred" }
                _plantDetails.update { null }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun clearError() {
        _errorMessage.update { null }
    }
}