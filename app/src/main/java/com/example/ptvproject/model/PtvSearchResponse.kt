package com.example.ptvproject.model

import com.google.gson.annotations.SerializedName

/**
 * Serialization parses JSON data into Kotlin objects
 * To do this, a data class is needed to store the parsed results
 */

// Linked to PtvSearchService
data class PtvSearchResponse (
    @SerializedName(value = "stops"    ) var stops    : ArrayList<Stops> = arrayListOf(),
)

data class Stops (
    @SerializedName(value = "stop_name") var stopName : String?           = null,
    @SerializedName(value = "stop_id"  ) var stopId   : Int?              = null,
    @SerializedName(value = "routes"   ) var routes   : ArrayList<Routes> = arrayListOf()
)

data class Routes (
    @SerializedName(value = "route_name") var routeName : String? = null,
    @SerializedName(value = "route_id"  ) var routeId   : Int?    = null,
    @SerializedName(value = "route_type") var routeType : Int?    = null
)