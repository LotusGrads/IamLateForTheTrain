package com.example.ptvproject.ui.selecttrainstation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


/**
 * Input: User inputs a string
 * Action: Search, click on train station
 * Output: List of train stations
 */

@Composable
fun SelectTrainStation(viewModel: SelectTrainStationViewModel, onClick: () -> Unit) {
    val state by viewModel.trainStateFlow.collectAsState()

    SelectTrainStation(
        searchInputUpdated = { value: String -> viewModel.generateListOfTrains(value) },
        stateOfTrains = state,
        onClick = onClick
    )
}

@Composable
private fun SelectTrainStation(
    searchInputUpdated: (String) -> Unit,
    stateOfTrains: SelectTrainStationState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val input = remember { mutableStateOf("") }

        SearchBar(input = input, searchInputUpdated = searchInputUpdated)

        when (stateOfTrains) {
            is SelectTrainStationState.ListOfStations -> {
                TrainStationList(stationList = stateOfTrains.listOfStations, onClick = onClick)
            }
            SelectTrainStationState.NoSearchQuery -> {
                Text(text = "Please type something in")
            }
            SelectTrainStationState.NoTrainStationsFound -> {
                Text(text = "No train stations found :'(")
            }
            else -> {
                Text(text = "ERROR")
            }
        }
    }

}

@Composable
private fun SearchBar(
    input: MutableState<String>,
    searchInputUpdated: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    //TODO: Insert magnifying glass icon to leadingIcon
    OutlinedTextField(
        value = input.value,
        onValueChange = { value: String ->
            input.value = value
            searchInputUpdated(value)
        },
        placeholder = { Text(text = "Search for a train station..") },
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    )
}

@Composable
private fun TrainStationCard(
    station: SelectTrainStationState.Station,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable(onClick = onClick),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = station.stationName,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Composable
private fun TrainStationList(
    stationList: List<SelectTrainStationState.Station>,
    onClick: () -> Unit
) {
    LazyColumn {
        items(stationList) { station ->
            TrainStationCard(
                station = station,
                onClick = onClick
            )
        }
    }
}