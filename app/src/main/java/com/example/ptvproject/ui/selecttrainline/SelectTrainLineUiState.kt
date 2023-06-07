package com.example.ptvproject.ui.selecttrainline


data class TrainLineUiState(
    val stopName: String = "",
    val listOfDepartures: List<Departures>,
)

data class Departures(
    val routeName: String,
    val direction: String,
    val listOfDepartureTimes: List<DepartureTimes>
)

data class DepartureTimes(
    val timeOfDeparture: String = ""
)

sealed class NewTrainUiState(){
    data class SucessfulState(
        val stopName: String = "",
        val listOfDepartures: List<Departures>,
    ): NewTrainUiState()
    object Loading : NewTrainUiState()
    object NoTrainsFound : NewTrainUiState()
}


