package com.example.ptvproject.ui.selecttrainstation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectTrainStationViewModel : ViewModel() {
    private val mutableTrainStateFlow: MutableStateFlow<SelectTrainStationState> =
        MutableStateFlow(SelectTrainStationState.NoSearchQuery)
    val trainStateFlow = mutableTrainStateFlow.asStateFlow()

    fun searchInputUpdated(input: String) {
        viewModelScope.launch {
            if (input.length > 2) {
                //TODO: Condition -> if train station list is empty
                if (1==1) {
                    mutableTrainStateFlow.emit(
                        SelectTrainStationState.NoTrainStationsFound
                    )
                } else {
                    mutableTrainStateFlow.emit(
                        SelectTrainStationState.ListOfStations(
                            listOfTrains = mutableListOf(
                                TrainStations("Melbourne Central")
                            )
                        )
                    )
                }
            } else {
                mutableTrainStateFlow.emit(
                    SelectTrainStationState.NoSearchQuery
                )
            }
        }
    }
}
