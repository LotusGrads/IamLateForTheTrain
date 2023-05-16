package com.example.ptvproject

import androidx.compose.runtime.Composable
import com.example.ptvproject.ui.notification.NotificationViewModel
import com.example.ptvproject.ui.selecttrainline.SelectTrainLine
import com.example.ptvproject.ui.selecttrainline.SelectTrainLineViewModel
import com.example.ptvproject.ui.selecttrainstation.SelectTrainStation
import com.example.ptvproject.ui.selecttrainstation.SelectTrainStationViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun SelectTrainStation(navigator: DestinationsNavigator) {
    SelectTrainStation(
        viewModel = SelectTrainStationViewModel(),
        onTrainStationSelected = { stopId ->
//            navigator.navigate(
//                SelectTrainLineDestination(stopId)
//            )
        }
    )
}

@Destination
@Composable
fun SelectTrainLine(navigator: DestinationsNavigator, stopId: Int) {
    SelectTrainLine(vM = SelectTrainLineViewModel(stationId = stopId)) {

    }
}

@Destination
@Composable
fun Notification() {
//    Notification(vM = NotificationViewModel(stationId = 1, trainLineId = 1))
}
