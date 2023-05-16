package com.example.ptvproject.ui.selecttrainline



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ptvproject.api.PtvApi
import com.example.ptvproject.ui.selecttrainstation.SelectTrainStationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SelectTrainLineViewModel(
    // station stopId and stopName from previous screen
    private val station: SelectTrainStationState.Station,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TrainLineUiState("", emptyList()))
    val uiState: StateFlow<TrainLineUiState> = _uiState.asStateFlow()

    var selectedTrainLine by mutableStateOf("")


    init {
        showNextThreeDeparturesForEachDirection()
    }


    //Show next three departures for each directionId
    fun showNextThreeDeparturesForEachDirection() {
        viewModelScope.launch() {
            val response = PtvApi.PtvRepo.getDepartures(routeType = 0, stopId = station.stopId)

            //Return an emptyList of departures to allow for code to be executed and sorted
            val successBody = response.body()?.departures ?: emptyList()


            // Sort the departures by scheduled departure time in ascending order
            val sortedDepartures = successBody.sortedBy { it.scheduledDepartureUtc }

            //Group departures by their directionId
            val departuresByDirection = sortedDepartures.groupBy { it.directionId }

            // Display the next three upcoming train departures
            // You can use the .take() method to limit the number of items emitted by the Observable.
            val nextThreeDepartures = departuresByDirection.mapValues { (_, departures) ->
                departures.take(3)
            }


            // Create an array of TrainLineUiState objects for each direction ID
            val arrayOfDeparturesWithDirection =
                nextThreeDepartures.map { (directionId, departures) ->
                    val directionResponse = PtvApi.PtvRepo.getDirections(directionId)

                    val directionName =
                        //When you get response, get list of directions, take the first directionId, and get directionName for it
                        directionResponse.body()?.directions?.first { it.directionId == directionId }?.directionName
                            ?: ""
                    val routeResponse = PtvApi.PtvRepo.getRoutes(departures.first().routeId)
                    val routeName =
                        routeResponse.body()?.route?.routeName ?: "Unknown Route"
                    Departures(
                        routeName = routeName,
                        direction = directionName,
                        listOfDepartureTimes = departures.map { DepartureTimes(it.scheduledDepartureUtc) })
                }

            //Display a list of departures for the station
            _uiState.emit(
                TrainLineUiState(
                    stopName = station.stationName,
                    listOfDepartures = arrayOfDeparturesWithDirection
                )
            )
        }
    }


    //View model makes decision as to what happens
    //Update screen state when user selects train line by setting confirmingTrainLine to true
    //When user selects train line, the following dialog will show ""Click continue to receive notifications of your timeliness."

    //Update the user's train line selection

    fun updateTrainLineScreenState(): String {

        //if train line is confirmed by user clicking continue on dialog, go to next screen
        //else, current state is normal
        return "hi"
    }


    fun showAlertDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isDepartureTimeSelected = true
            )
        }
    }
}



