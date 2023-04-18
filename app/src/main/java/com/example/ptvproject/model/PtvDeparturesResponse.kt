package com.example.ptvproject.model

import com.google.gson.annotations.SerializedName


data class PtvDeparturesResponse (

    @SerializedName("departures") var departures : List<Departures>,
    @SerializedName("stops") var stops : Stops,
    @SerializedName("routes") var routes : Routes,
    @SerializedName("directions") var directions : Directions,


)

data class Departures (

    @SerializedName("stop_id") var stopId : Int,
    @SerializedName("route_id") var routeId : Int,
    @SerializedName("run_id") var runId : Int,
    @SerializedName("run_ref") var runRef : String,
    @SerializedName("direction_id") var directionId : Int,
    @SerializedName("disruption_ids") var disruptionIds : List<Int>,
    @SerializedName("scheduled_departure_utc") var scheduledDepartureUtc : String,
    @SerializedName("estimated_departure_utc") var estimatedDepartureUtc : String,
    @SerializedName("at_platform") var atPlatform : Boolean,
    @SerializedName("platform_number") var platformNumber : String,
    @SerializedName("flags") var flags : String,
    @SerializedName("departure_sequence") var departureSequence : Int

)

data class Directions (

    @SerializedName("route_direction_description") var routeDirectionDescription : String,
    @SerializedName("direction_id") var directionId : Int,
    @SerializedName("direction_name") var directionName : String,
    @SerializedName("route_id") var routeId : Int,
    @SerializedName("route_type") var routeType : Int

)