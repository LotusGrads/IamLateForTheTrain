package com.example.ptvproject.ui.selecttrainstation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// generate list of train stations
// the ui here handles the search bar, keyboard and generation/return of results

@Composable
fun SelectTrainStationScreen(
    modifier: Modifier = Modifier,
    selectTrainStationViewModel: SelectTrainStationViewModel
    ) {

    SearchForTrainStation(searchText = "", )
    // this composable should contain the combined overall layout for the page
    // it should refresh when we change from the search bar?
}

@Composable
fun SearchForTrainStation(
    searchText: String,
    onSearchTextInput: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    modifier: Modifier = Modifier) {

    // composable with empty rounded search bar, bring up keyboard
    // use outlinedtextfield for the text box
    // https://semicolonspace.com/jetpack-compose-textfield/

    Column {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextInput
            )
    }

    // after this, the page should close the keyboard, save user's
    // input and load any matching results from the database

}