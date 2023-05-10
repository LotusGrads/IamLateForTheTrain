package com.example.ptvproject.ui.selecttrainstation

sealed class SelectTrainStationState {
    object NoSearchQuery : SelectTrainStationState()
    object NoTrainStationsFound : SelectTrainStationState()
    object Loading : SelectTrainStationState()
    object Error: SelectTrainStationState()

    data class Success(
        val listOfStations: List<Station>
    ) : SelectTrainStationState()

    data class Station(
        var stationName: String,
        var stopId: Int
    )
}