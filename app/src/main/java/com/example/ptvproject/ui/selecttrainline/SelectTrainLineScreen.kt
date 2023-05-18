package com.example.ptvproject.ui.selecttrainline


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ptvproject.R
import com.example.ptvproject.ui.theme.PTVprojectTheme
import java.time.ZonedDateTime
import androidx.compose.foundation.layout.Column as Column
import com.example.ptvproject.ui.selecttrainline.ConfirmTrainLineDialog as ConfirmTrainLineDialog

@Composable
fun SelectTrainLineScreen(
    selectTrainLineViewModel: SelectTrainLineViewModel = viewModel(),
    //onTrainLineSelected: (departureTime: ZonedDateTime, stationName: String, trainLine: String, direction: String) -> Unit
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
            trainLineUiState = selectTrainLineUiState,
            updateTrainLineScreenState = selectTrainLineViewModel::updateTrainLineScreenState,
            showAlertDialog = selectTrainLineViewModel::showAlertDialog,
            modifier = Modifier.padding(it)
        )
        if (selectTrainLineUiState.isDepartureTimeSelected) {
            ConfirmTrainLineDialog(
                onConfirm = { /*TODO: Handle navigation */ }
            )
        }
    }
}

@Composable
fun SelectTrainLineContent(
    modifier: Modifier = Modifier,
    trainLineUiState: TrainLineUiState,
    updateTrainLineScreenState: () -> String,
    showAlertDialog: () -> Unit,
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    text = trainLineUiState.stopName,
                    style = MaterialTheme.typography.h1
                )
                Spacer(modifier = Modifier.width(90.dp))
                Image(
                    modifier = modifier
                        .size(40.dp)
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
                            text = "Leaving at: " + it.timeOfDeparture,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier
                                .wrapContentSize()
                                .selectable(
                                    selected = selectedDepartureTime == it.timeOfDeparture,
                                    onClick = showAlertDialog
                                )
                        )
                    }
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
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(0),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF317B3A),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Confirm",
                    style = MaterialTheme.typography.body1
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    //  activity.finish()
                },
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(0),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF317B3A),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Back",
                    style = MaterialTheme.typography.body1
                )
            }
        }
    )
}

@Composable
fun PtvAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = {
            Text(
                text = "Go Back",
                style = MaterialTheme.typography.h3,
                color = Color.White
            )
                },
        backgroundColor = Color(0xFF317B3A) ,
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

@Preview
@Composable
fun TrainLineScreenLotsOfDeparturesPreview() {
    PTVprojectTheme {
        Box(
            modifier = Modifier.background(color = Color(0xFFE1FFD7))
        ) {
            SelectTrainLineContent(
                trainLineUiState = TrainLineUiState(
                    stopName = "Melbourne Central Station",
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
                    ),
                    isDepartureTimeSelected = false
                ),
                updateTrainLineScreenState = { "Alamein " }) {
            }
        }
    }
}

@Preview
@Composable
fun TrainLineScreenOneDeparturePreview() {
    PTVprojectTheme {
        Box(
            modifier = Modifier.background(color = Color(0xFFE1FFD7))
        ) {
            SelectTrainLineContent(
                trainLineUiState = TrainLineUiState(
                    stopName = "Melbourne Central Station",
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
                    ),
                    isDepartureTimeSelected = false
                ),
                updateTrainLineScreenState = { "Alamein " }) {
            }
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
                    stopName = "Melbourne Central Station",
                    listOfDepartures = listOf(
                    ),
                    isDepartureTimeSelected = false
                ),
                updateTrainLineScreenState = { "Alamein " }) {
            }
        }
    }
}

@Preview
@Composable
fun TrainLineScreenScrollableDeparturesPreview() {
    PTVprojectTheme {
        Box(
            modifier = Modifier.background(color = Color(0xFFE1FFD7))
        ) {
            SelectTrainLineContent(
                trainLineUiState = TrainLineUiState(
                    stopName = "Melbourne Central Station",
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
                    ),
                    isDepartureTimeSelected = false
                ),
                updateTrainLineScreenState = { "Alamein " }) {
            }
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

@Preview
@Composable
fun TopBarPreview(){
    PTVprojectTheme {
        PtvAppBar(
            canNavigateBack = true,
            navigateUp = { })
    }
}






