package com.example.dessertclicker

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DessertUiState(
    val currentDessertIndex: Int = 0,
    val dessertsSold: Int = 0,
    val revenue: Int = 0,
    val currentDessertPrice: Int = Datasource.dessertList[0].price,
    val currentDessertImageId: Int = Datasource.dessertList[0].imageId
)

class DessertViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DessertUiState())
    val uiState: StateFlow<DessertUiState> = _uiState.asStateFlow()

    fun onDessertClicked() {
        _uiState.update { currentState ->
            val dessertsSold = currentState.dessertsSold + 1
            val nextDessertIndex = determineDessertIndex(dessertsSold)

            currentState.copy(
                revenue = currentState.revenue + currentState.currentDessertPrice,
                dessertsSold = dessertsSold,
                currentDessertIndex = nextDessertIndex,
                currentDessertPrice = Datasource.dessertList[nextDessertIndex].price,
                currentDessertImageId = Datasource.dessertList[nextDessertIndex].imageId
            )
        }
    }

    private fun determineDessertIndex(dessertsSold: Int): Int {
        var dessertIndex = 0
        for (index in Datasource.dessertList.indices) {
            if (dessertsSold >= Datasource.dessertList[index].startProductionAmount) {
                dessertIndex = index
            } else {
                break
            }
        }
        return dessertIndex
    }
}