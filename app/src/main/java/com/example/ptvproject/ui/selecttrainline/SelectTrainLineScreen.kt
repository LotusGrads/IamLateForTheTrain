package com.example.ptvproject.ui.selecttrainline


import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ptvproject.ui.theme.PTVprojectTheme
import androidx.compose.foundation.layout.Column as Column
import com.example.ptvproject.ui.selecttrainline.ConfirmTrainLineDialog as ConfirmTrainLineDialog

@Composable
fun SelectTrainLineScreen(
    selectTrainLineViewModel: SelectTrainLineViewModel = viewModel(),
    listOfDepartures: List<TrainLineUiState>,
    modifier: Modifier = Modifier,
) {
    val selectTrainLineUiState by selectTrainLineViewModel.uiState.collectAsState()
    //:: function reference

    SelectTrainLineContent(
        trainLineUiState = selectTrainLineUiState,
        updateTrainLineScreenState =  selectTrainLineViewModel::updateTrainLineScreenState
    )
    if (selectTrainLineUiState.isTrainLineConfirmed) {
        ConfirmTrainLineDialog(
            onConfirm = { selectTrainLineViewModel.updateTrainLineScreenState()})
    }

}

@Composable
fun SelectTrainLineContent(
    modifier: Modifier = Modifier,
    trainLineUiState: TrainLineUiState,
    updateTrainLineScreenState: () -> String,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = trainLineUiState.stopName,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
        )

        Spacer(modifier = Modifier.height(25.dp))

        LazyColumn(
            modifier = modifier
                .padding(16.dp)
        ) {
            items(
                trainLineUiState.listOfDepartures
            ) { item ->
                SelectTrainLineItemRow(
                    item = item,
                    selectedTrainLine = updateTrainLineScreenState(),
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SelectTrainLineItemRow(
    item: Departures,
    selectedTrainLine: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .border(width = 1.dp, color = Color(0xFF317B3A))
            .selectable(
            selected = selectedTrainLine == item.routeName,
            onClick = {
            }
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp)
        ) {
            Row{
                Text(
                    text = item.routeName,
                    fontWeight = FontWeight.Bold
                    )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End),
                    text = item.direction
                )
            }
            Column {
                    item.listOfDepartureTimes.forEach {
                        Text(text = "Leaving at: " + it.timeOfDeparture)
                    }
                }
            }
        }
    }

@Composable
private fun ConfirmTrainLineDialog(
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {
            //Dismiss dialog when user clicks outside dialog
        },
        title = {
            Text(text = "You have selected a train line!")
        },
        text = {
            Text("Click continue to receive notifications of your timeliness.")
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    activity.finish()
                }
            ) {
                Text("Back")
            }
        }
    )
}

@Preview
@Composable
fun TrainLineScreenPreview() {
    PTVprojectTheme {
        Box(
            modifier = Modifier.background(color = Color(0xFFBDF2BE))
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
                ), updateTrainLineScreenState = {"Alamein"}
            )
        }
    }
}

@Preview
@Composable
fun TrainLineScreenTwoPreview() {
    PTVprojectTheme {
        Box(
            modifier = Modifier.background(color = Color(0xFFBDF2BE))
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
                ), updateTrainLineScreenState = {"Alamein"}
            )
        }
    }
}

//@Preview
//@Composable
//fun AlertDialogPreview(){
//    PTVprojectTheme {
//        Box(
//            modifier = Modifier.background(color = Color(0x96C99C))
//        ) {
//            ConfirmTrainLineDialog(
//                onConfirm = { }
//            )
//        }
//    }
//}






