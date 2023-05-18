package com.example.ptvproject.model

import com.google.gson.annotations.SerializedName


data class PtvDirectionsResponse (
    @SerializedName("directions") var directions : List<Directions>,
)

data class Directions (

    @SerializedName("route_direction_description") var routeDirectionDescription : String,
    @SerializedName("direction_id") var directionId : Int,
    @SerializedName("direction_name") var directionName : String,
    @SerializedName("route_id") var routeId : Int,
    @SerializedName("route_type") var routeType : Int

)