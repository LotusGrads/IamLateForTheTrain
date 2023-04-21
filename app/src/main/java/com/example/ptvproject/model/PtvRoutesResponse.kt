package com.example.ptvproject.model

import com.google.gson.annotations.SerializedName


data class PtvRoutesResponse (
    @SerializedName("stop") var stop : Stop,
)

data class Stop (

    @SerializedName("disruption_ids") var disruptionIds : List<Int>,
    @SerializedName("station_type") var stationType : String,
    @SerializedName("station_description") var stationDescription : String,
    @SerializedName("route_type") var routeType : Int,
    @SerializedName("routes") var routes : List<Routes>,
    @SerializedName("stop_id") var stopId : Int,
    @SerializedName("stop_name") var stopName : String,
    @SerializedName("stop_landmark") var stopLandmark : String

)