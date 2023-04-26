package com.example.ptvproject.ui.selecttrainline

data class TrainLineUiState(
    // Time of departure
    // This information is shown as "YYYY-MM-DDTHH:MM:SS" (ISO 8601 format)
    val timeOfDeparture: String = "",
    val routeName: String = "",

    )