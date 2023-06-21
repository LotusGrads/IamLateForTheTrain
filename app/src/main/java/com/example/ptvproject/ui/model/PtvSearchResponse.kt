package com.example.ptvproject.ui.model

import com.google.gson.annotations.SerializedName

/**
 * Serialization parses JSON data into Kotlin objects
 * To do this, a data class is needed to store the parsed results
 */

// Linked to PtvSearchService
data class PtvSearchResponse (
    @SerializedName(value = "stops"    ) var stops      : ArrayList<Stops>
)

data class Stops(
    @SerializedName(value = "stop_name" ) var stopName: String,
    @SerializedName(value = "stop_id"   ) var stopId: Int?,
    @SerializedName(value = "routes"    ) var routes: List<Any>,
    @SerializedName(value = "route_type") var routeType: Int?
)

data class Routes (
    @SerializedName(value = "route_name") var routeName : String,
    @SerializedName(value = "route_id"  ) var routeId   : Int,
    @SerializedName(value = "route_type") var routeType : Int
)