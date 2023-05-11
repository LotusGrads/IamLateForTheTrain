package com.example.ptvproject.ui.selecttrainline


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
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
import androidx.compose.foundation.layout.Column as Column
import com.example.ptvproject.ui.selecttrainline.ConfirmTrainLineDialog as ConfirmTrainLineDialog

@Composable
fun SelectTrainLineScreen(
    selectTrainLineViewModel: SelectTrainLineViewModel = viewModel(),
    //onTrainLineSelected: (departureTime: ZonedDateTime, stationName: String, trainLine: String, direction: String) -> Unit
) {
    val selectTrainLineUiState by selectTrainLineViewModel.uiState.collectAsState()
    //:: function reference

    SelectTrainLineContent(
        trainLineUiState = selectTrainLineUiState,
        updateTrainLineScreenState = selectTrainLineViewModel::updateTrainLineScreenState
    )
    if (selectTrainLineUiState.isDepartureTimeSelected) {
        ConfirmTrainLineDialog(
            onConfirm = { }
            )
    }

}

@Composable
fun SelectTrainLineContent(
    modifier: Modifier = Modifier,
    trainLineUiState: TrainLineUiState,
    updateTrainLineScreenState: () -> String,
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
            Row(modifier = modifier.fillMaxWidth()) {
                Text(
                    text = trainLineUiState.stopName,
                    style = MaterialTheme.typography.h2
                )
                Spacer(modifier = Modifier.width(36.dp))
                Image(
                    modifier = modifier
                        .size(40.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End),
                    contentDescription = null,
                    painter = painterResource(id = R.drawable.logo_simple)
                )
            }
            if (trainLineUiState.listOfDepartures.isNotEmpty()) {
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
                            showAlertDialog = showAlertDialog
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            } else {
                Text(
                    text = "No upcoming train departures for this train station.",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 12.dp)
                        .border(width = 1.dp, color = Color(0xFF317B3A))
                )
            }
        }
    }
}


@Composable
fun SelectTrainLineItemRow(
    item: Departures,
    selectedDepartureTime: String,
    showAlertDialog: () -> Unit,
    modifier: Modifier = Modifier,
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
                Spacer(modifier = Modifier.height(8.dp))
            }
            Column {
                item.listOfDepartureTimes.forEach {
                    Text(
                        text = "Leaving at: " + it.timeOfDeparture,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .border(width = 1.dp, color = Color(0xFF317B3A))
                            .selectable(
                                selected = selectedDepartureTime == it.timeOfDeparture,
                                onClick = showAlertDialog
                            )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun ConfirmTrainLineDialog(
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = {
            //Dismiss dialog when user clicks outside dialog
        },
        title = {
            Text(
                text = "You have selected a train line!",
                style = MaterialTheme.typography.body2
            )
        },
        text = {
            Text(
                text = "Click continue to receive notifications of your timeliness.",
                style = MaterialTheme.typography.body1
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    //TODO: parse in > schedule departure time, stop name, train line and direction
                    onConfirm()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF317B3A),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Confirm",
                    style = MaterialTheme.typography.body2
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    //  activity.finish()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF317B3A),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Back",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    )
}


@Preview
@Composable
fun TrainLineScreenLotsOfDeparturesPreview() {
    PTVprojectTheme {
        Box(
            modifier = Modifier.background(color = Color(0xFFE1FFD7))
        ) {
            SelectTrainLineContent(
                trainLineUiState = TrainLineUiState(
                    stopName = "Melbourne Central" + " Station",
                    listOfDepartures = listOf(
                        Departures(
                            routeName = "Alamein",
                            direction = "Toward Flinders",
                            listOfDepartureTimes = listOf(
                                DepartureTimes("2pm"),
                                DepartureTimes("3pm"),
                                DepartureTimes("4pm")
                            )
                        ),
                        Departures(
                            routeName = "Alamein",
                            direction = "Toward Alamein",
                            listOfDepartureTimes = listOf(
                                DepartureTimes("2pm"),
                                DepartureTimes("3pm"),
                                DepartureTimes("4pm")
                            )
                        ),
                        Departures(
                            routeName = "Cranbourne",
                            direction = "Toward Cranbourne",
                            listOfDepartureTimes = listOf(
                                DepartureTimes("2pm"),
                                DepartureTimes("3pm"),
                                DepartureTimes("4pm")
                            )
                        )
                    )
                ), updateTrainLineScreenState = { "Alamein" }
            )
        }
    }
}

@Preview
@Composable
fun TrainLineScreenTwoPreview() {
    PTVprojectTheme {
        Box(
            modifier = Modifier.background(color = Color(0xFFE1FFD7))
        ) {
            SelectTrainLineContent(
                trainLineUiState = TrainLineUiState(
                    stopName = "Melbourne Central" + " Station",
                    listOfDepartures = listOf(
                        Departures(
                            routeName = "Alamein",
                            direction = "Toward Flinders",
                            listOfDepartureTimes = listOf(
                                DepartureTimes("2pm"),
                                DepartureTimes("3pm"),
                                DepartureTimes("4pm")
                            )
                        )
                    )
                ), updateTrainLineScreenState = { "Alamein" }
            )
        }
    }
}

@Preview
@Composable
fun TrainLineScreenNoDeparturesPreview() {
    PTVprojectTheme {
        Box(
            modifier = Modifier.background(color = Color(0xFFE1FFD7))
        ) {
            SelectTrainLineContent(
                trainLineUiState = TrainLineUiState(
                    stopName = "Melbourne Central" + " Station",
                    listOfDepartures = listOf(
                    )
                ), updateTrainLineScreenState = { "Alamein" }
            )
        }
    }
}

@Preview
@Composable
fun TrainLineScreenPreview() {
    PTVprojectTheme {
        Box(
            modifier = Modifier.background(color = Color(0xFFE1FFD7))
        ) {
            SelectTrainLineContent(
                trainLineUiState = TrainLineUiState(
                    stopName = "Melbourne Central" + " Station",
                    listOfDepartures = listOf(
                        Departures(
                            routeName = "Alamein",
                            direction = "Toward Flinders",
                            listOfDepartureTimes = listOf(
                                DepartureTimes("2pm"),
                                DepartureTimes("3pm"),
                                DepartureTimes("4pm")
                            )
                        ),
                        Departures(
                            routeName = "Alamein",
                            direction = "Toward Alamein",
                            listOfDepartureTimes = listOf(
                                DepartureTimes("2pm"),
                                DepartureTimes("3pm"),
                                DepartureTimes("4pm")
                            )
                        ),
                        Departures(
                            routeName = "Alamein",
                            direction = "Toward Alamein",
                            listOfDepartureTimes = listOf(
                                DepartureTimes("2pm"),
                                DepartureTimes("3pm"),
                                DepartureTimes("4pm")
                            )
                        ),
                        Departures(
                            routeName = "Alamein",
                            direction = "Toward Alamein",
                            listOfDepartureTimes = listOf(
                                DepartureTimes("2pm"),
                                DepartureTimes("3pm"),
                                DepartureTimes("4pm")
                            )
                        ),
                        Departures(
                            routeName = "Alamein",
                            direction = "Toward Alamein",
                            listOfDepartureTimes = listOf(
                                DepartureTimes("2pm"),
                                DepartureTimes("3pm"),
                                DepartureTimes("4pm")
                            )
                        ),
                        Departures(
                            routeName = "Alamein",
                            direction = "Toward Alamein",
                            listOfDepartureTimes = listOf(
                                DepartureTimes("2pm"),
                                DepartureTimes("3pm"),
                                DepartureTimes("4pm")
                            )
                        ),
                        Departures(
                            routeName = "Alamein",
                            direction = "Toward Alamein",
                            listOfDepartureTimes = listOf(
                                DepartureTimes("2pm"),
                                DepartureTimes("3pm"),
                                DepartureTimes("4pm")
                            )
                        ),
                        Departures(
                            routeName = "Cranbourne",
                            direction = "Toward Cranbourne",
                            listOfDepartureTimes = listOf(
                                DepartureTimes("2pm"),
                                DepartureTimes("3pm"),
                                DepartureTimes("4pm")
                            )
                        )
                    )
                ), updateTrainLineScreenState = { "Alamein" }
            )
        }
    }
}

@Preview
@Composable
fun AlertDialogPreview() {
    PTVprojectTheme {
        Box(
            modifier = Modifier.background(color = Color(0xFFE1FFD7))
        ) {
            ConfirmTrainLineDialog(
                onConfirm = {},
            )
        }
    }
}






