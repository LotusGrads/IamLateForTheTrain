package com.example.ptvproject.ui.notification

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationViewModel() : ViewModel() {

    private val _notificationsUiState:
            MutableStateFlow<NotificationsUiState> =
        MutableStateFlow(NotificationsUiState())

    val notificationsUiState: StateFlow<NotificationsUiState> =
        _notificationsUiState.asStateFlow()

    // need to take inputs from previous screens to display

    // Do I need this function because I have not provided the parameters previously?
    private fun NotificationsUiState(): NotificationsUiState {


    }


    //https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state#4

    // Here, we will contain the logic for the notifications

    // function to calculate whether the user is on time or not

    // function to take/pass inputs and display them on the screen

}