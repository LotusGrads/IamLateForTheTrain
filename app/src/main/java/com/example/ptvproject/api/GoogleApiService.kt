package com.example.ptvproject.api

import com.google.api.gax.core.NoCredentialsProvider
import com.google.maps.routing.v2.*
import com.google.type.LatLng

object GoogleApiService {
    fun getEta(
        originLatitude: Double,
        originLongitude: Double,
        destinationLatitude: Double,
        destinationLongitude: Double
    ) {
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
                .setTravelMode(RouteTravelMode.WALK).build()
        )
        val routesList = response.routesList
        val firstRoute = routesList.firstOrNull()
        //TODO: find shortest route
        println("route: $firstRoute")
    }
}


/*
import com.google.maps.routing.v2.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
  public static void main(String[] arguments) throws IOException {
      RoutesSettings routesSettings = RoutesSettings.newBuilder()
              .setHeaderProvider(() -> {
                  Map headers = new HashMap<>();
                  headers.put("X-Goog-FieldMask", "*");
                  return headers;
              }).build();
      RoutesClient routesClient = RoutesClient.create(routesSettings);

      ComputeRoutesResponse response = routesClient.computeRoutes(ComputeRoutesRequest.newBuilder()
              .setOrigin(Waypoint.newBuilder().setPlaceId("ChIJeRpOeF67j4AR9ydy_PIzPuM").build())
              .setDestination(Waypoint.newBuilder().setPlaceId("ChIJG3kh4hq6j4AR_XuFQnV0_t8").build())
              .setRoutingPreference(RoutingPreference.TRAFFIC_AWARE)
              .setTravelMode(RouteTravelMode.DRIVE).build());
      System.out.println("Response: " + response.toString());
  }
}
 */