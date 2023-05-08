package com.example.ptvproject.ui.selecttrainline

data class TrainLineUiState(
    val stopName: String = "",
    val listOfDepartures: List<Departures>,
    val isTrainLineConfirmed: Boolean = false
)

data class Departures(
    val routeName: String,
    val direction: String,
    val listOfDepartureTimes: List<DepartureTimes>
)

data class DepartureTimes(
    val timeOfDeparture: String = ""
)



