package com.example.ptvproject.ui.selecttrainline

data class TrainLineUiState(
    val stopName: String = "",
    val timeOfDeparture: String = "",
    val routeName: String = "",
    val trainLineDirection: String = "",

    //Dialog state
    val confirmingTrainLine: Boolean = false,
    val userSelectedTrainLine: String = ""
)

