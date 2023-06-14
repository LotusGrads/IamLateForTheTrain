package com.example.ptvproject.ui.notification

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ptvproject.api.GoogleApiService
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
import java.time.Duration
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
                Log.d(LOCATION_TAG, "${location.latitude},${location.longitude}")
            }
        }
    }


    private val _notificationsUiState = locationFlow.map { location ->

        // user ETA is based on selected arrival time minus distance to next station
        // user on time is based on the user's distance to the station of choice and the ETA

        val userInfo = if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude

            val walkingTime = GoogleApiService.getWalkingDurationInSeconds(
                originLatitude = latitude,
                originLongitude = longitude,
                //temporarily hard-coded to North Richmond Station
                destinationLatitude = -37.810193346702796,
                destinationLongitude = 144.99246514027507
            )
            val estimatedArrivalTime = ZonedDateTime.now().plus(walkingTime)

            UserInfo(
                onTime = estimatedArrivalTime.isBefore(departureTime)
                        || estimatedArrivalTime.isEqual(departureTime),
                estimatedArrivalTime = estimatedArrivalTime
            )
        } else {
            UserInfo(false, null)
        }

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