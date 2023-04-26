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

    fun selectNextThreeTrainDepartures() {}


}
