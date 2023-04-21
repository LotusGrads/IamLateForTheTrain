package com.example.ptvproject.ui.notification

import java.time.ZonedDateTime

data class NotificationsUiState(
    val userInfo: UserInfo,
    val trainInfo: TrainInfo,
)

data class UserInfo(
    val onTime: Boolean,
    val estimatedArrivalTime: ZonedDateTime,
)

data class TrainInfo(
    val stationName: String,
    val lineName: String,
    val departureTime: ZonedDateTime,
)
