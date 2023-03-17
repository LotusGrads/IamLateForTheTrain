package com.example.ptvproject.api

import retrofit2.http.GET
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Query

interface PtvService {
    @GET("/v3/route_types")
    suspend fun getRouteTypes(
        @Query("devid") devid: String,
        @Query("signature") signature: String,
    ): Response<RouteTypeResponse>
    // http://timetableapi.ptv.vic.gov.au/v3/route_types?devid=3002376&signature=EE9FF24FAF161C2FCD10DA238898D43851D79943
}





data class RouteTypeResponse (

    @SerializedName("route_types" ) var routeTypes : ArrayList<RouteTypes> = arrayListOf(),
    @SerializedName("status"      ) var status     : Status?               = Status()

)

data class RouteTypes (

    @SerializedName("route_type_name" ) var routeTypeName : String? = null,
    @SerializedName("route_type"      ) var routeType     : Int?    = null

)

data class Status (

    @SerializedName("version" ) var version : String? = null,
    @SerializedName("health"  ) var health  : Int?    = null

)