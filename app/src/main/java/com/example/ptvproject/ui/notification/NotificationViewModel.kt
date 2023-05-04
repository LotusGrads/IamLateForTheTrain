package com.example.ptvproject.ui.notification

import androidx.compose.foundation.MutatePriority
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ptvproject.api.PtvApi
import com.example.ptvproject.ui.selecttrainstation.SelectTrainStationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class NotificationViewModel(
    private val trainInfo: SelectTrainStationState.Station,
    // train line from previous screen
    private val line: String,
    // train departure time from previous screen
    private val departureTime: ZonedDateTime
    ) : ViewModel() {


    // we are using ZonedDatedTime vs. String?
    // what is the purpose of this?
    private val _notificationsUiState =
        MutableStateFlow(NotificationsUiState(
            userInfo = UserInfo(false, null),
            trainInfo = TrainInfo(trainInfo.stationName, line, departureTime)
        ))

    val notificationsUiState: StateFlow<NotificationsUiState> =
        _notificationsUiState.asStateFlow()

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

        viewModelScope.launch ()
        {
            PtvApi.retrofitService.getStation()
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


}