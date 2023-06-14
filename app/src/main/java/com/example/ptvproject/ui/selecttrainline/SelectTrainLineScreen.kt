package com.example.ptvproject.ui.selecttrainline


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ptvproject.R
import com.example.ptvproject.ui.theme.PTVprojectTheme
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import androidx.compose.foundation.layout.Column as Column

@Composable
fun SelectTrainLineScreen(
    selectTrainLineViewModel: SelectTrainLineViewModel = viewModel(),
    onTrainLineSelected: (String, String, String, String) -> Unit,

    ) {
    //:: function reference
    Scaffold(
        topBar = {
            PtvAppBar(
                canNavigateBack = true,
                navigateUp = { /*TODO: Handle navigation */ }
            )
        }
    ) {
        val selectTrainLineUiState by selectTrainLineViewModel.uiState.collectAsState()

        SelectTrainLineContent(
            modifier = Modifier.padding(it),
            trainLineUiState = selectTrainLineUiState,
            updateTrainLineScreenState = selectTrainLineViewModel::updateTrainLineScreenState,
            onTrainLineSelected = onTrainLineSelected,
        )

    }
}

@Composable
fun SelectTrainLineContent(
    modifier: Modifier = Modifier,
    trainLineUiState: NewTrainUiState,
    updateTrainLineScreenState: () -> String,
    onTrainLineSelected: (String, String, String, String) -> Unit,
) {
    Card(
        modifier = Modifier
            .background(color = Color(0xFFE1FFD7))
            .border(width = 1.dp, color = Color(0xFF317B3A))
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (trainLineUiState) {
                NewTrainUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }}
                    NewTrainUiState.NoTrainsFound -> {
                    showNoTrainsFound(
                    )
                }
                is NewTrainUiState.SucessfulState -> {
                    showLoadedState(
                        modifier = modifier,
                        onTrainLineSelected = onTrainLineSelected,
                        trainLineUiState = trainLineUiState,
                        updateTrainLineScreenState = updateTrainLineScreenState
                    )
                }
            }
        }
    }
}

@Composable
fun showLoadedState(
    modifier: Modifier = Modifier,
    onTrainLineSelected: (departureTime: String, stationName: String, trainLine: String, direction: String) -> Unit,
    trainLineUiState: NewTrainUiState.SucessfulState,
    updateTrainLineScreenState: () -> String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = trainLineUiState.stopName,
            style = MaterialTheme.typography.h1
        )
        Spacer(modifier = Modifier.width(100.dp))
        Image(
            modifier = modifier
                .size(40.dp)
                .wrapContentWidth(Alignment.End),
            contentDescription = null,
            painter = painterResource(id = R.drawable.logo_simple)
        )
    }
        LazyColumn(
            modifier = modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            items(
                trainLineUiState.listOfDepartures
            ) { item ->
                SelectTrainLineItemRow(
                    item = item,
                    selectedDepartureTime = updateTrainLineScreenState(),
                    trainLineUiState = trainLineUiState,
                    onTrainLineSelected = onTrainLineSelected,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }


@Composable
fun showNoTrainsFound() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(100.dp))
        Image(
            modifier = Modifier
                .size(40.dp)
                .wrapContentWidth(Alignment.End),
            contentDescription = null,
            painter = painterResource(id = R.drawable.logo_simple)
        )
    }
        Spacer(modifier = Modifier.height(50.dp))
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .background(color = Color(0xFFE0E0E0))
                    .border(width = 1.dp, color = Color(0xFF317B3A))
                    .padding(vertical = 6.dp, horizontal = 50.dp)

            ) {
                Text(
                    text = "No upcoming train departures for this \r\ntrain station. Go back to select a \ndifferent train station.",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .wrapContentSize()
                )
            }
        }
    }

@Composable
fun SelectTrainLineItemRow(
    item: Departures,
    selectedDepartureTime: String,
    modifier: Modifier = Modifier,
    trainLineUiState: NewTrainUiState.SucessfulState,
    onTrainLineSelected: (departureTime: String, stationName: String, trainLine: String, direction: String) -> Unit,
) {
    Row(
        modifier = Modifier
            .border(width = 1.dp, color = Color(0xFF317B3A)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp)
        ) {
            Row {
                Text(
                    text = item.routeName,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End),
                    text = item.direction,
                    style = MaterialTheme.typography.body2
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                item.listOfDepartureTimes.forEach {
                    Box(
                        modifier = Modifier
                            .background(color = Color(0xFFE0E0E0))
                            .border(width = 1.dp, color = Color(0xFF317B3A))
                            .padding(vertical = 6.dp, horizontal = 32.dp)

                    ) {
                        Text(
                            text = "Leaving at: " + formatDepartureTime(it.timeOfDeparture),
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier
                                .wrapContentSize()
                                .selectable(
                                    selected = selectedDepartureTime == it.timeOfDeparture,
                                    onClick = {
                                        onTrainLineSelected(
                                            it.timeOfDeparture,
                                            trainLineUiState.stopName,
                                            item.routeName,
                                            item.direction
                                        )
                                    }
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

private fun formatDepartureTime(timeOfDeparture: String): String {
    val zonedDateTime = ZonedDateTime.parse(timeOfDeparture)
    val localDateTime = LocalDateTime.ofInstant(zonedDateTime.toInstant(), zonedDateTime.zone)
    val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    return formatter.format(localDateTime)
}


@Composable
fun PtvAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = "Go Back",
                style = MaterialTheme.typography.h3,
                color = Color.White
            )
        },
        backgroundColor = Color(0xFF317B3A),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            }
        }
    )
}

//@Preview
//@Composable
//fun TrainLineScreenLotsOfDeparturesPreview() {
//    PTVprojectTheme {
//        Box(
//            modifier = Modifier.background(color = Color(0xFFE1FFD7))
//        ) {
//            SelectTrainLineContent(
//                trainLineUiState = TrainLineUiState(
//                    stopName = "Melbourne Central Station",
//                    listOfDepartures = listOf(
//                        Departures(
//                            routeName = "Alamein",
//                            direction = "Toward Flinders",
//                            listOfDepartureTimes = listOf(
//                                DepartureTimes("2pm"),
//                                DepartureTimes("3pm"),
//                                DepartureTimes("4pm")
//                            )
//                        ),
//                        Departures(
//                            routeName = "Alamein",
//                            direction = "Toward Alamein",
//                            listOfDepartureTimes = listOf(
//                                DepartureTimes("2pm"),
//                                DepartureTimes("3pm"),
//                                DepartureTimes("4pm")
//                            )
//                        ),
//                        Departures(
//                            routeName = "Cranbourne",
//                            direction = "Toward Cranbourne",
//                            listOfDepartureTimes = listOf(
//                                DepartureTimes("2pm"),
//                                DepartureTimes("3pm"),
//                                DepartureTimes("4pm")
//                            )
//                        )
//                    ),
//                ),
//                updateTrainLineScreenState = { "Alamein" },
//                onTrainLineSelected = { _, _, _, _ -> }
//            )
//        }
//    }
//}
//
//@Preview
//@Composable
//fun TrainLineScreenOneDeparturePreview() {
//    PTVprojectTheme {
//        Box(
//            modifier = Modifier.background(color = Color(0xFFE1FFD7))
//        ) {
//            SelectTrainLineContent(
//                trainLineUiState = TrainLineUiState(
//                    stopName = "Melbourne Central Station",
//                    listOfDepartures = listOf(
//                        Departures(
//                            routeName = "Alamein",
//                            direction = "Toward Flinders",
//                            listOfDepartureTimes = listOf(
//                                DepartureTimes("2pm"),
//                                DepartureTimes("3pm"),
//                                DepartureTimes("4pm")
//                            )
//                        )
//                    ),
//                ),
//                updateTrainLineScreenState = { "Alamein " },
//                onTrainLineSelected = { _, _, _, _ -> }
//            )
//        }
//    }
//}
//
//@Preview
//@Composable
//fun TrainLineScreenNoDeparturesPreview() {
//    PTVprojectTheme {
//        Box(
//            modifier = Modifier.background(color = Color(0xFFE1FFD7))
//        ) {
//            SelectTrainLineContent(
//                trainLineUiState = TrainLineUiState(
//                    stopName = "Melbourne Central Station",
//                    listOfDepartures = listOf(
//                    ),
//                ),
//                updateTrainLineScreenState = { "Alamein " },
//                onTrainLineSelected = { _, _, _, _ -> }
//            )
//        }
//    }
//}
//
//@Preview
//@Composable
//fun TrainLineScreenScrollableDeparturesPreview() {
//    PTVprojectTheme {
//        Box(
//            modifier = Modifier.background(color = Color(0xFFE1FFD7))
//        ) {
//            SelectTrainLineContent(
//                trainLineUiState = TrainLineUiState(
//                    stopName = "Melbourne Central Station",
//                    listOfDepartures = listOf(
//                        Departures(
//                            routeName = "Alamein",
//                            direction = "Toward Flinders",
//                            listOfDepartureTimes = listOf(
//                                DepartureTimes("2pm"),
//                                DepartureTimes("3pm"),
//                                DepartureTimes("4pm")
//                            )
//                        ),
//                        Departures(
//                            routeName = "Alamein",
//                            direction = "Toward Alamein",
//                            listOfDepartureTimes = listOf(
//                                DepartureTimes("2pm"),
//                                DepartureTimes("3pm"),
//                                DepartureTimes("4pm")
//                            )
//                        ),
//                        Departures(
//                            routeName = "Alamein",
//                            direction = "Toward Alamein",
//                            listOfDepartureTimes = listOf(
//                                DepartureTimes("2pm"),
//                                DepartureTimes("3pm"),
//                                DepartureTimes("4pm")
//                            )
//                        ),
//                        Departures(
//                            routeName = "Alamein",
//                            direction = "Toward Alamein",
//                            listOfDepartureTimes = listOf(
//                                DepartureTimes("2pm"),
//                                DepartureTimes("3pm"),
//                                DepartureTimes("4pm")
//                            )
//                        ),
//                        Departures(
//                            routeName = "Alamein",
//                            direction = "Toward Alamein",
//                            listOfDepartureTimes = listOf(
//                                DepartureTimes("2pm"),
//                                DepartureTimes("3pm"),
//                                DepartureTimes("4pm")
//                            )
//                        ),
//                        Departures(
//                            routeName = "Alamein",
//                            direction = "Toward Alamein",
//                            listOfDepartureTimes = listOf(
//                                DepartureTimes("2pm"),
//                                DepartureTimes("3pm"),
//                                DepartureTimes("4pm")
//                            )
//                        ),
//                        Departures(
//                            routeName = "Alamein",
//                            direction = "Toward Alamein",
//                            listOfDepartureTimes = listOf(
//                                DepartureTimes("2pm"),
//                                DepartureTimes("3pm"),
//                                DepartureTimes("4pm")
//                            )
//                        ),
//                        Departures(
//                            routeName = "Cranbourne",
//                            direction = "Toward Cranbourne",
//                            listOfDepartureTimes = listOf(
//                                DepartureTimes("2pm"),
//                                DepartureTimes("3pm"),
//                                DepartureTimes("4pm")
//                            )
//                        )
//                    ),
//                ),
//                updateTrainLineScreenState = { "Alamein " },
//                onTrainLineSelected = { _, _, _, _ -> }
//            )
//        }
//    }
//}
//
//
//@Preview
//@Composable
//fun TopBarPreview() {
//    PTVprojectTheme {
//        PtvAppBar(
//            canNavigateBack = true,
//            navigateUp = { })
//    }
//}
//
//
//
//
//
//
