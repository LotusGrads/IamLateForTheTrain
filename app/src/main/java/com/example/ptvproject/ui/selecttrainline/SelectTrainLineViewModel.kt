package com.example.ptvproject.ui.selecttrainline



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ptvproject.api.PtvApi
import com.example.ptvproject.ui.selecttrainstation.SelectTrainStationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SelectTrainLineViewModel(
    // station stopId and stopName from previous screen
    private val station: SelectTrainStationState.Station,
) : ViewModel() {
    private val _uiState = MutableStateFlow(listOf(TrainLineUiState()))
    val uiState: StateFlow <List<TrainLineUiState>> = _uiState.asStateFlow()
    init {
        getDepartures()
    }

    fun getDepartures(){
        viewModelScope.launch() {
            //Make API call to PTV app to get departures for stop (from previous screen)
            val response = PtvApi.retrofitService.getDepartures(stopId = station.stopId)
            //Gets the body of the API response and retrieves the departures
            //You can use .map() method to transform the items emitted by an Observable into a new form
            val successBody = response.body()?.departures?.map {
                ReducedDepartures(it.stopId, it.routeId, it.directionId, it.scheduledDepartureUtc, it.atPlatform, it.platformNumber)
            }?.map {
                val routeInfo = getRoute(it.routeId)
                TrainLineUiState(it.scheduledDepartureUtc, routeInfo.routeName)
            }
            //Will update screen based on what ui state emits
            _uiState.emit(successBody?: emptyList())

        }
    }

    suspend fun getRoute(routeId: Int) : ReducedRoute {
        //Make API call to PTV app to get routes for stop (from previous screen)
        val response = PtvApi.retrofitService.getRoutes(station.stopId)
        //Gets the body of the API response and retrieves the routes
        val successBody = response.body()?.route
        // Get the routeName
        return ReducedRoute(successBody?.routeName?: "Error")
    }


    //Show next three departures for each directionId
    fun showNextThreeDeparturesForEachDirection() {
        viewModelScope.launch() {
            val response = PtvApi.retrofitService.getDepartures(stopId = station.stopId)

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
            //The flatMap() function can take a Stream of List and return a Stream of values combined from all those lists.
            val arrayOfDeparturesWithDirection = nextThreeDepartures.flatMap { (directionId, departures) ->
                val directionName = response.body()?.directions?.get(directionId)?.directionName ?: ""
                    departures.map { TrainLineUiState(it.scheduledDepartureUtc, directionName) }
            }

            // Display the departure times in the UI
            _uiState.emit(arrayOfDeparturesWithDirection)
        }
    }


    //When user selects train line, this notification appears
    //Two notification options:
    //1. Currently, you are a "" minute walk from the the train station. Go back and select another departure time to ensure that you makes your train. *Back button*
    //2. Currently, you are a "" minute walk from the the train station. Select continue to receive notifications of your timeliness. *Continue button*

    //view model makes decision as to what happens
    //update screen state when user selects train line by setting confirmingTrainLine to true
    fun userSelectedTrainLine(){

    }
}
