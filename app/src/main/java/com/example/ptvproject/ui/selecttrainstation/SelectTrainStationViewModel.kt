package com.example.ptvproject.ui.selecttrainstation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SelectTrainStationViewModel: ViewModel() {

    // ensures that the VM can change the mutable one, but the UI should not be able to change it
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    // allow us to hide or make visible the search functionality
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()
    
}

/* Logic here: the user selects a train station using a search pad and inputting
* a String that is then matched to the station name. The VM retains the train station data/name */