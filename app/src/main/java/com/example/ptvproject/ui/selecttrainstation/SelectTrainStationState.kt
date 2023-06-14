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
    data class Location(
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val isGranted: Boolean = false
    )

    //TODO: Add a dataclass that refers to the location + location permission (isGranted)
}