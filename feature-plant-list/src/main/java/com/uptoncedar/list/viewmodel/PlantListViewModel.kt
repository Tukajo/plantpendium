package com.uptoncedar.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uptoncedar.list.domain.GetPlantsByQueryUseCase
import com.uptoncedar.networking.model.Plant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantListViewModel @Inject constructor(
    private val getPlantsByQueryUseCase: GetPlantsByQueryUseCase
) : ViewModel() {

    private val _plants = MutableLiveData<List<Plant>>()
    val plants: LiveData<List<Plant>> = _plants

    init {
        fetchPlants()
    }

    private fun fetchPlants() {
        viewModelScope.launch {
            try {
                val plantList = getPlantsByQueryUseCase("snow white")
                _plants.value = plantList
            } catch (e: Exception) {
                // TODO, handle this error.
            }
        }
    }
}