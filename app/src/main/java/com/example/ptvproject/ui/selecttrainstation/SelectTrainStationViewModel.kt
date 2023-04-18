package com.example.ptvproject.ui.selecttrainstation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ptvproject.api.PtvApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectTrainStationViewModel : ViewModel() {
    private val mutableTrainStateFlow: MutableStateFlow<SelectTrainStationState> =
        MutableStateFlow(SelectTrainStationState.NoSearchQuery)
    val trainStateFlow = mutableTrainStateFlow.asStateFlow()

    fun searchInputUpdated(input: String) {
        viewModelScope.launch() {
            if (input.length > 2) {
                val response = PtvApi.retrofitService.getStation(input)
                val successBody = response.body()?.stops

                if (successBody != null) {
                    if (successBody.isEmpty()) {
                        mutableTrainStateFlow.emit(
                            SelectTrainStationState.NoTrainStationsFound
                        )
                    } else {
                        val trainStationList: MutableList<SelectTrainStationState.Station> =
                            mutableListOf()
                        var station: SelectTrainStationState.Station

                        for (stop in successBody) {
                            if (stop.routeType == 0) {
                                station = SelectTrainStationState.Station(
                                    stationName = stop.stopName,
                                    stopId = stop.stopId
                                )
                                trainStationList.add(station)
                            }
                        }

                        mutableTrainStateFlow.emit(
                            SelectTrainStationState.ListOfStations(
                                listOfStations = trainStationList
                            )
                        )
                    }
                }
            } else {
                mutableTrainStateFlow.emit(
                    SelectTrainStationState.NoSearchQuery
                )
            }
        }
    }
}
