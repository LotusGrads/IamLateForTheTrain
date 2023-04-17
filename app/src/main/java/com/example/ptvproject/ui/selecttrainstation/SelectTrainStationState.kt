package com.example.ptvproject.ui.selecttrainstation

import com.example.ptvproject.model.PtvSearchResponse

sealed class SelectTrainStationState {
    object NoSearchQuery : SelectTrainStationState()
    object NoTrainStationsFound : SelectTrainStationState()

    data class ListOfStations(
        val listOfTrains: PtvSearchResponse
    ) : SelectTrainStationState()
}