package com.example.ptvproject.ui.selecttrainstation

import com.example.ptvproject.model.PtvSearchResponse
import com.example.ptvproject.model.Stops

sealed class SelectTrainStationState {
    object NoSearchQuery : SelectTrainStationState()
    object NoTrainStationsFound : SelectTrainStationState()

    data class ListOfStations(
        val listOfStations: List<Station>
    ) : SelectTrainStationState()

    data class Station(
        var stationName: String,
        var stopId: Int
    ) : SelectTrainStationState()
}
