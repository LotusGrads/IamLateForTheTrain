package com.example.ptvproject

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ptvproject.destinations.NotificationDestination
import com.example.ptvproject.destinations.SelectTrainLineDestination
import com.example.ptvproject.ui.notification.NotificationScreen
import com.example.ptvproject.ui.notification.NotificationViewModel
import com.example.ptvproject.ui.selecttrainline.SelectTrainLineScreen
import com.example.ptvproject.ui.selecttrainline.SelectTrainLineViewModel
import com.example.ptvproject.ui.selecttrainstation.SelectTrainStation
import com.example.ptvproject.ui.selecttrainstation.SelectTrainStationState
import com.example.ptvproject.ui.selecttrainstation.SelectTrainStationViewModel
import com.example.ptvproject.ui.selecttrainstation.locationProvider
import com.google.android.gms.location.LocationServices
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.ZonedDateTime

@Destination(start = true)
@Composable
fun SelectTrainStation(navigator: DestinationsNavigator) {
    SelectTrainStation(
        viewModel = SelectTrainStationViewModel(),
        onTrainStationSelected = { stopId, stationName ->
            navigator.navigate(SelectTrainLineDestination(stationName = stationName, stopId = stopId))
        }
    )
}

@Destination
@Composable
fun SelectTrainLine(navigator: DestinationsNavigator, stationName: String, stopId: Int) {
    SelectTrainLineScreen(
        selectTrainLineViewModel = SelectTrainLineViewModel(
            station = SelectTrainStationState.Station(
                stationName,
                stopId
            )
        ),
        onTrainLineSelected = { departureTime, stationName, trainLine, direction ->
            navigator.navigate(NotificationDestination())
        }
    )
}
//stationName
//departureTime

@Destination
@Composable
fun Notification(
    navigator: DestinationsNavigator,
    stationName: String,
    stationId: Int,
    trainLineId: String,
    departureTime: String) {
//    Notification(vM = NotificationViewModel(stationId = 1, trainLineId = 1))
    // we need train station name, train line and departure time
    val context = LocalContext.current
    val locationProvider = LocationServices.getFusedLocationProviderClient(context)

    NotificationScreen(
        viewModel = NotificationViewModel(
            station = SelectTrainStationState.Station(
                stationName,
                stationId
            ),
            line = trainLineId,
            departureTime = ZonedDateTime.parse(departureTime),
            locationProvider = locationProvider,
        )
    )

}
