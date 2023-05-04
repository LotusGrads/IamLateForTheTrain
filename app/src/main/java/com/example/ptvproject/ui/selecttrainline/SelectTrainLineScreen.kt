package com.example.ptvproject.ui.selecttrainline


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column as Column

//Train Line Screen composable
@Composable
fun SelectTrainLineScreen(
    options: List<TrainLineUiState>,
    item: TrainLineUiState,
    onSelectionChanged: (TrainLineUiState) -> Unit,
    modifier: Modifier = Modifier
    ) {
    var selectedTrainLine by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

            Text(
                text = item.stopName,
                //style = MaterialTheme.typography.h6
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { item ->

                    SelectTrainLineItemRow(
                        item = item,
                        selectedTrainLine = selectedTrainLine,
                        onSelectionItemChanged = { selectedTrainLine = item.routeName },
                        onSelectionChanged = onSelectionChanged
                    )
                }
            }
        }
    }
}

@Composable
fun SelectTrainLineItemRow(
    item: TrainLineUiState,
    selectedTrainLine: String,
    onSelectionItemChanged: (String) -> Unit,
    onSelectionChanged: (TrainLineUiState) -> Unit,
    modifier: Modifier = Modifier
    ) {
    Row(
        modifier = Modifier.selectable(
            selected = selectedTrainLine == item.routeName,
            onClick = {
                onSelectionItemChanged(item.routeName)
                onSelectionChanged(item)
            }
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = item.routeName
            )
            Text(
                text = item.timeOfDeparture
            )

            Text(
                text = item.trainLineDirection
            )
        }
    }
}

//I want the train line screen to show the stopName at the top of the screen, in bold and in a different heading
//The first item row shows the direction towards City
//The second item row shows another direction e.g. Alamein
//The third item row shows the next direction e.g. Craigeburn
//And so on
//Each item row shows the next three departures

@Composable
fun ConfirmTrainLineDialog() {
    AlertDialog(
        onDismissRequest = {
            //Dismiss dialog when user clicks outside dialog
            // openDialog.value = false} }
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
                }) {
                    Text("Confirm")
                }
        },
        dismissButton = {
            Button(
                onClick = {
                }) {
                Text("Back")
            }
        }
    )
}


@Preview
@Composable
fun TrainLineScreenPreview(){
    SelectTrainLineScreen(
        item = TrainLineUiState(),
    )
}

