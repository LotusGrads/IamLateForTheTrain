package com.example.ptvproject.ui.selecttrainline

data class ReducedDepartures(
    val stopId: Int,
    val routeId: Int,
    val directionId: Int,
    val scheduledDepartureUtc: String,
    val atPlatform: Boolean,
    val platformNumber: String)

data class ReducedRoute(
    val routeName: String
)
