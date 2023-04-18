package com.example.ptvproject.ui.selecttrainstation

import com.example.ptvproject.model.PtvSearchResponse
import com.example.ptvproject.model.Stops

sealed class SelectTrainStationState {
    object NoSearchQuery : SelectTrainStationState()
    object NoTrainStationsFound : SelectTrainStationState()

    data class ListOfStations(
        val listOfStations: MutableList<Station>
    ) : SelectTrainStationState()

    data class Station(
        var stationName: String,
        var stopId: Int
    ) : SelectTrainStationState()
}

// ListOfStations([Station("watergardens", 1), Station("Melbourne Central", 2)]