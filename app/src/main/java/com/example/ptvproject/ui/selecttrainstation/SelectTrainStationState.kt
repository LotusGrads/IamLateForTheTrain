package com.example.ptvproject.ui.selecttrainstation

sealed class SelectTrainStationState {
    object NoSearchQuery : SelectTrainStationState()
    object NoTrainStationsFound : SelectTrainStationState()

    data class ListOfStations(
        val listOfTrains: List<TrainStations>
    ) : SelectTrainStationState()
}

data class TrainStations(
    val name: String
)