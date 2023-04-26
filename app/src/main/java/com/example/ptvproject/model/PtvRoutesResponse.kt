package com.example.ptvproject.model

import com.google.gson.annotations.SerializedName


data class PtvRoutesResponse (

    @SerializedName("route") var route : Route,
    @SerializedName("status") var status : Status

)

data class Route (

    @SerializedName("route_service_status") var routeServiceStatus : RouteServiceStatus,
    @SerializedName("route_type") var routeType : Int,
    @SerializedName("route_id") var routeId : Int,
    @SerializedName("route_name") var routeName : String,
    @SerializedName("route_number") var routeNumber : String,
    @SerializedName("route_gtfs_id") var routeGtfsId : String,
    @SerializedName("geopath") var geopath : List<String>

)

data class RouteServiceStatus (

    @SerializedName("description") var description : String,
    @SerializedName("timestamp") var timestamp : String

)

data class Status (

    @SerializedName("version") var version : String,
    @SerializedName("health") var health : Int

)