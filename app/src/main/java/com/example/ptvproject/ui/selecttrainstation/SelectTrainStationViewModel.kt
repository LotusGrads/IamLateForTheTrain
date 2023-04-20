package com.example.ptvproject.ui.selecttrainstation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ptvproject.api.PtvApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "VM"

class SelectTrainStationViewModel : ViewModel() {
    private val mutableTrainStateFlow: MutableStateFlow<SelectTrainStationState> =
        MutableStateFlow(SelectTrainStationState.NoSearchQuery)
    val trainStateFlow = mutableTrainStateFlow.asStateFlow()

    fun generateListOfTrains(input: String) {
        mutableTrainStateFlow.value = SelectTrainStationState.Loading

        viewModelScope.launch() {
            if (input.length > 2) {
                Log.d(TAG, "input: $input")
                val response = PtvApi.retrofitService.getStation(input)

                if (response.isSuccessful) {
                    val successBody = response.body()?.stops

                    if (successBody != null) {
                        if (successBody.isEmpty()) {
                            mutableTrainStateFlow.emit(
                                SelectTrainStationState.NoTrainStationsFound
                            )
                            Log.d(TAG, "No train stations found")
                        } else {
                            Log.d(TAG, "stations founds")

                            val trainStationList = buildList {
                                for (stop in successBody) {
                                    if (stop.routeType == 0 && stop.stopName.contains(
                                            input,
                                            ignoreCase = true
                                        )
                                    ) {
                                        Log.d(TAG, "valid station: ${stop.stopName}")
                                        var station = SelectTrainStationState.Station(
                                            stationName = stop.stopName,
                                            stopId = stop.stopId
                                        )
                                        add(station)
                                    }
                                }
                            }
                            mutableTrainStateFlow.emit(
                                SelectTrainStationState.Success(
                                    listOfStations = trainStationList
                                )
                            )
                        }
                    }
                } else {
                    mutableTrainStateFlow.emit(
                        SelectTrainStationState.Error
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
