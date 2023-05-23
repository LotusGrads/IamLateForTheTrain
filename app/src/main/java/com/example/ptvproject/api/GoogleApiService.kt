package com.example.ptvproject.api

import android.util.Log
import com.google.api.gax.core.NoCredentialsProvider
import com.google.maps.routing.v2.*
import com.google.type.LatLng
import java.time.Duration

private const val TAG = "Google Api Service"

object GoogleApiService {
    fun getWalkingDurationInSeconds(
        originLatitude: Double,
        originLongitude: Double,
        destinationLatitude: Double,
        destinationLongitude: Double
    ): Duration {
        val routesSettings: RoutesSettings = RoutesSettings.newBuilder()
            .setHeaderProvider {
                buildMap {
                    put("X-Goog-FieldMask", "*")
                    put("X-goog-api-key", "AIzaSyBEXdO50GkrYvXOXVgbFFNiH9Yl9qTKHIg")
                }
            }
            .setCredentialsProvider(NoCredentialsProvider())
            .build()
        val routesClient: RoutesClient = RoutesClient.create(routesSettings)
        val response: ComputeRoutesResponse = routesClient.computeRoutes(
            ComputeRoutesRequest.newBuilder()
                .setOrigin(
                    Waypoint.newBuilder()
                        .setLocation(
                            Location.newBuilder()
                                .setLatLng(
                                    LatLng.newBuilder()
                                        .setLatitude(originLatitude)
                                        .setLongitude(originLongitude)
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .setDestination(
                    Waypoint.newBuilder()
                        .setLocation(
                            Location.newBuilder()
                                .setLatLng(
                                    LatLng.newBuilder()
                                        .setLatitude(destinationLatitude)
                                        .setLongitude(destinationLongitude)
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .setComputeAlternativeRoutes(true)
                .setTravelMode(RouteTravelMode.WALK)
                .build()
        )
        val routesList = response.routesList

        Log.d(TAG, "Route List Size: ${routesList.size}")
        Log.d(
            TAG, "Route Durations\n" +
                    "${routesList.forEach { println(it.staticDuration.seconds) }}"
        )

        return Duration.ofSeconds(
            routesList.minBy { it.staticDuration.seconds }.staticDuration.seconds
        )
    }
}