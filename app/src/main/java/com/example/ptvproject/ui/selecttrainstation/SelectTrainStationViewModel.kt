package com.example.ptvproject.ui.selecttrainstation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ptvproject.api.PtvApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class SelectTrainStationViewModel : ViewModel() {
    private val mutableTrainStateFlow: MutableStateFlow<SelectTrainStationState> =
        MutableStateFlow(SelectTrainStationState.NoSearchQuery)
    val trainStateFlow = mutableTrainStateFlow.asStateFlow()

    fun searchInputUpdated(input: String) {
        viewModelScope.launch() {
            if (input.length > 2) {
                //TODO: Condition -> if train station list is empty
                if (1==2) {
                    mutableTrainStateFlow.emit(
                        SelectTrainStationState.NoTrainStationsFound
                    )
                } else {
                    var listResult = PtvApi.retrofitService.getStation(input)

                    mutableTrainStateFlow.emit(
                        SelectTrainStationState.ListOfStations(
                            listOfTrains = listResult
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
