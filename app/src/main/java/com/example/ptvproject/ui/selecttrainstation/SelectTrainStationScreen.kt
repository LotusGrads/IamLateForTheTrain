package com.example.ptvproject.ui.selecttrainstation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*


/**
 * Input: User inputs a string
 * Action: Search, click on train station
 * Output: List of train stations
 */
@Composable
private fun SelectTrainStation(
    searchInputUpdated: (String) -> Unit,
    stateOfTrains: SelectTrainStationState
) {

    Column() {
        val input = remember { mutableStateOf("") }

        TextField(value = input.value, onValueChange = { value ->
            input.value = value
            searchInputUpdated(value)
        })

        when (stateOfTrains) {
            is SelectTrainStationState.ListOfStations -> {
                Text(text = stateOfTrains.listOfTrains.toString())
            }
            SelectTrainStationState.NoSearchQuery -> {
                Text(text = "Please type something in")
            }
            SelectTrainStationState.NoTrainStationsFound -> {
                Text(text = "No train stations found :'(")
            }
        }
    }

}

@Composable
fun SelectTrainStation(viewModel: SelectTrainStationViewModel) {
    val state by viewModel.trainStateFlow.collectAsState()

    SelectTrainStation(
        searchInputUpdated = { value: String -> viewModel.searchInputUpdated(value) },
        stateOfTrains = state
    )
}