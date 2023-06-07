package com.example.ptvproject.ui.selecttrainstation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * Input: User inputs a string
 * Action: Search, click on train station
 * Output: List of train stations
 */

@Composable
fun SelectTrainStation(viewModel: SelectTrainStationViewModel, onTrainStationSelected: (stopId: Int, stationName: String) -> Unit) {
    val state by viewModel.trainStateFlow.collectAsState()

    SelectTrainStation(
        onSearchClicked = { value: String -> viewModel.generateListOfTrains(value) },
        stateOfTrains = state,
        onTrainStationSelected = onTrainStationSelected,
    )
}

@Composable
private fun SelectTrainStation(
    onSearchClicked: (String) -> Unit,
    stateOfTrains: SelectTrainStationState,
    onTrainStationSelected: (stopId: Int, stationName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val input = remember { mutableStateOf("") }

        SearchBar(
            input = input,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(input.value)
                    focusManager.clearFocus()
                }
            )
        )
        Spacer(modifier = Modifier.height(50.dp))
        when (stateOfTrains) {
            is SelectTrainStationState.Success -> {
                TrainStationList(stationList = stateOfTrains.listOfStations, onTrainStationSelected = onTrainStationSelected)
            }
            SelectTrainStationState.NoSearchQuery -> {
                Text(text = "Please type something in")
            }
            SelectTrainStationState.NoTrainStationsFound -> {
                Text(text = "No train stations found :'(")
            }
            SelectTrainStationState.Loading -> {
                CircularProgressIndicator()
            }
            SelectTrainStationState.Error -> {
                Text(text = "FATAL ERROR!")
            }
        }
    }

}

@Composable
private fun SearchBar(
    input: MutableState<String>,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier
) {
    //TODO: Insert magnifying glass icon to leadingIcon
    OutlinedTextField(
        value = input.value,
        onValueChange = { value: String ->
            input.value = value
        },
        placeholder = { Text(text = "Search for a train station..") },
        shape = RoundedCornerShape(50),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
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
        modifier = modifier
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TrainStationList(
    stationList: List<SelectTrainStationState.Station>,
    onTrainStationSelected: (stopId: Int, stationName: String) -> Unit
) {
    LazyColumn {
        items(stationList) { station ->
            TrainStationCard(
                station = station,
                onClick = { onTrainStationSelected(station.stopId, station.stationName) },
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}
