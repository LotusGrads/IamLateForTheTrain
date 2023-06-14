package com.example.ptvproject.ui.notification

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ptvproject.ui.selecttrainstation.LOCATION_TAG
import com.example.ptvproject.ui.selecttrainstation.SelectTrainStationState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.temporal.TemporalAmount
import java.util.concurrent.TimeUnit

class NotificationViewModel(
    private val station: SelectTrainStationState.Station,
    //private val trainInfo: SelectTrainStationState.Station,
    // train line from previous screen
    private val line: String,
    // train departure time from previous screen
    private val departureTime: ZonedDateTime,
    private val locationProvider: FusedLocationProviderClient,
) : ViewModel() {

    private val locationFlow = MutableStateFlow<Location?>(null)

    private val locationCallback: LocationCallback = object : LocationCallback() {
        // the function below retrieves that location information
        override fun onLocationResult(result: LocationResult) {
            /**
             * Option 1
             * This option returns the locations computed, ordered from oldest to newest.
             * */
            for (location in result.locations) {
                locationFlow.tryEmit(Location(location.latitude, location.longitude))
                // Update data class with location data
//                currentUserLocation =
//                    SelectTrainStationState.Location(location.latitude, location.longitude)
                Log.d(LOCATION_TAG, "${location.latitude},${location.longitude}")
            }
        }
    }


    private val _notificationsUiState = locationFlow.map { location ->
        // TODO: figure out the ETA
        // TODO: Figure out if the user is on time

        // user ETA is based on selected arrival time - distance to next station
        // user on time is based on the user's distance to the station of choice and the ETA

        // 1. use the user's current location and station location, plug it into google to find walking time
        // 2. use the walking time and add it on top of the departureTime
        // 3. display this for the user

        // 1. for the onTime boolean, if the ETA is the same or earlier than departureTime,
        //    user is on time, else user is late

        val latitude = location?.latitude
        val longitude = location?.longitude
        val walkingTime: TemporalAmount? = null
        val estimatedArrivalTime = ZonedDateTime.now().plus(walkingTime)

        if (estimatedArrivalTime.isBefore(departureTime) || estimatedArrivalTime.isEqual(departureTime) ) {
            UserInfo(
                onTime = true,
                estimatedArrivalTime = estimatedArrivalTime
            )
        } else {
            UserInfo(
                onTime = false,
                estimatedArrivalTime = estimatedArrivalTime
            )
        }

        val userInfo = UserInfo(false, null)
        NotificationsUiState(
            userInfo = userInfo,
            trainInfo = TrainInfo(station.stationName, line, departureTime)
        )
    }.stateIn(
        this.viewModelScope, SharingStarted.Eagerly, initialValue =
        NotificationsUiState(
            userInfo = UserInfo(false, null),
            trainInfo = TrainInfo(station.stationName, line, departureTime)
        )
    )

    val notificationsUiState: StateFlow<NotificationsUiState> = _notificationsUiState

    //https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state#4

    // function to calculate whether the user is on time or not

    fun isUserOnTime() {

    }

    // this is displaying the user's ETA from their current location to the station location
    fun updateUserEstimatedArrivalTime() {

    }

    // here, we want to allow the screen to retrieve the information regarding the train
    // supplied by the previous screen and display it
    fun displayTrainInfo() {

        viewModelScope.launch()
        {
//            PtvApi.retrofitService.getStation()
        }


//        _notificationsUiState.value =
//            NotificationsUiState(trainInfo =
//            TrainInfo(
//                stationName = "",
//                lineName = "",
//                departureTime = null
//            ))
    }

    fun displayStationName() {

    }

    fun displayLineName() {

    }

    fun displayTrainDepartureTime() {

    }


    @SuppressLint("MissingPermission")
    fun initiateLocationRequest() {
        //An encapsulation of various parameters for requesting
        // location through FusedLocationProviderClient.
        val locationRequest: LocationRequest =
            LocationRequest.create().apply {
                interval = TimeUnit.SECONDS.toMillis(60)
                fastestInterval = TimeUnit.SECONDS.toMillis(30)
                maxWaitTime = TimeUnit.MINUTES.toMillis(2)
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
        //use FusedLocationProviderClient to request location update
        locationProvider.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper(),
        )
    }

    fun stopLocationUpdate() {
        try {
            //Removes all location updates for the given callback.
            val removeTask = locationProvider.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(LOCATION_TAG, "Location Callback removed.")
                } else {
                    Log.d(LOCATION_TAG, "Failed to remove Location Callback.")
                }
            }
        } catch (se: SecurityException) {
            Log.e(LOCATION_TAG, "Failed to remove Location Callback.. $se")
        }
    }


}