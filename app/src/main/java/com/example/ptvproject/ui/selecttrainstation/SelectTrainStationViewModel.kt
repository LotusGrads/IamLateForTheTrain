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
        viewModelScope.launch() {
            if (input.length > 2) {
                if (mutableTrainStateFlow.value !is SelectTrainStationState.Success) {
                    mutableTrainStateFlow.value = SelectTrainStationState.Loading
                }
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


                            val trainStationList = buildList {
                                for (stop in successBody) {
                                    if (stop.routeType == 0 && stop.stopName.contains(
                                            input,
                                            ignoreCase = true
                                        )
                                    ) {
                                        Log.d(TAG, "valid station: ${stop.stopName}")
                                        val station = SelectTrainStationState.Station(
                                            stationName = stop.stopName,
                                            stopId = stop.stopId
                                        )
                                        add(station)
                                    }
                                }
                            }
                            Log.d(TAG, "${trainStationList.size} stations founds")

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
