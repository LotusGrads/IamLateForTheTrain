package com.example.ptvproject.api

import android.util.Log
import com.example.ptvproject.model.*
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Response as OkHttpResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import retrofit2.Response

private const val BASE_URL =
    "https://timetableapi.ptv.vic.gov.au/"

private const val TAG = "PtvApiService"

/**
 * Implement Retrofit service API
 * Create Retrofit object with base URL and converter factory to convert strings
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
//                    .addNetworkInterceptor(logger)
            .addInterceptor(
                SignatureAddingInterceptor(
                    privateKey = "79943beb-734e-4421-b1d6-1ce6b531baaf",
                    developerId = 3002376,
                )
            )
            .build()
    )
    .build()

class SignatureAddingInterceptor(
    private val privateKey: String,
    private val developerId: Int,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): OkHttpResponse {
        val request = chain.request()
        //https://timetableapi.ptv.vic.gov.au/path?something=value
        val url = request.url
        Log.d(TAG, "initial request: $request")

        //https://timetableapi.ptv.vic.gov.au/path?something=value&devid=$developerId&secret=$secretGoesHere
        val newUrl = buildTTAPIURL(
            baseURL = url.scheme + "://" + url.host,
            privateKey = privateKey,
            uri = url.encodedPath,
            developerId = developerId,
        )
        // val newNewUrl = newUrl.toHttpUrl()
        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()

        Log.d(TAG, "new request: $newRequest")
        return chain.proceed(newRequest)
    }

    private fun buildTTAPIURL(
        baseURL: String,
        privateKey: String,
        uri: String,
        developerId: Int,
    ): String {
        val HMAC_SHA1_ALGORITHM = "HmacSHA1"
        val uriWithDeveloperID = StringBuilder(uri).append(if (uri.contains("?")) "&" else "?")
            .append("devid=$developerId")
        val keyBytes = privateKey.toByteArray()
        val uriBytes = uriWithDeveloperID.toString().toByteArray()
        val signingKey = SecretKeySpec(keyBytes, HMAC_SHA1_ALGORITHM)
        val mac = Mac.getInstance(HMAC_SHA1_ALGORITHM)
        mac.init(signingKey)
        val signatureBytes = mac.doFinal(uriBytes)
        val signature = StringBuilder(signatureBytes.size * 2)
        for (signatureByte in signatureBytes) {
            val intVal = signatureByte.toInt() and 0xff
            if (intVal < 0x10) {
                signature.append("0")
            }
            signature.append(Integer.toHexString(intVal))
        }
        val url = StringBuilder(baseURL).append(uri).append(if (uri.contains("?")) "&" else "?")
            .append("devid=$developerId").append("&signature=${signature.toString().toUpperCase()}")

        return url.toString()
    }
}

/**
 * define how Retrofit talks to web server using HTTP requests
 * Use suspend to make it asynchronous and not block the calling thread
 */
interface PtvApiService {
    @GET("/v3/search/{search}")
    suspend fun getStation(@Path("search") searchString: String): Response<PtvSearchResponse>

    @GET("/v3/departures/route_type/{route_type}/stop/{stop_id}")
    suspend fun getDepartures(
        @Path("route_type") routeType: Int = 0,
        @Path("stop_id") stopId: Int,
    ): Response<PtvDeparturesResponse>

    @GET("/v3/directions/{direction_id}")
    suspend fun getDirections(@Path("direction_id") directionId: Int): Response<PtvDirectionsResponse>

    @GET("/v3/routes/{route_id}")
    suspend fun getRoutes(@Path("route_id") routeId: Int): Response<PtvRoutesResponse>

}

sealed class fakeDataType {
    object success : fakeDataType()
    object failure : fakeDataType()
    object multipleItems : fakeDataType()

}


class PtvRepository(
    val userDemoData: Boolean = false,
) {
    val retrofitService: PtvApiService by lazy {
        retrofit.create(PtvApiService::class.java)
    }

    suspend fun getStation(
        searchString: String,
        fakeDataType: fakeDataType = com.example.ptvproject.api.fakeDataType.success,
    ): Response<PtvSearchResponse> {
        if (userDemoData)
            return when (fakeDataType) {
                com.example.ptvproject.api.fakeDataType.failure ->
                    Response.success(
                        PtvSearchResponse(
                            arrayListOf(
                                Stops(
                                    stopName = "",
                                    stopId = null,
                                    routes = emptyList(),
                                    routeType = null
                                )
                            )
                        )
                    )
                com.example.ptvproject.api.fakeDataType.multipleItems ->
                    Response.success(
                        PtvSearchResponse(
                            arrayListOf(
                                Stops(
                                    stopName = "Richmond Station",
                                    stopId = 1162,
                                    routes = arrayListOf(
                                        Routes(
                                            routeName = "Alamein",
                                            routeId = 1,
                                            routeType = 0
                                        ),
                                        Routes(
                                            routeName = "Belgrave",
                                            routeId = 2,
                                            routeType = 0
                                        ),
                                        Routes(
                                            routeName = "Crnabourne",
                                            routeId = 4,
                                            routeType = 0
                                        ),
                                        Routes(
                                            routeName = "Frankston",
                                            routeId = 6,
                                            routeType = 0
                                        ),
                                        Routes(
                                            routeName = "Glen Waverley",
                                            routeId = 7,
                                            routeType = 0
                                        ),
                                        Routes(
                                            routeName = "Lilydale",
                                            routeId = 9,
                                            routeType = 0
                                        ),
                                        Routes(
                                            routeName = "Pakenham",
                                            routeId = 11,
                                            routeType = 0
                                        ),
                                        Routes(
                                            routeName = "Sandringham",
                                            routeId = 12,
                                            routeType = 0
                                        )
                                    ),
                                    routeType = 0
                                ),
                                Stops(
                                    stopName = "North Richmond Station",
                                    stopId = 1145,
                                    routes = arrayListOf(
                                        Routes(
                                            routeName = "Mernda",
                                            routeId = 5,
                                            routeType = 0
                                        ),
                                        Routes(
                                            routeName = "Hurstbridge",
                                            routeId = 8,
                                            routeType = 0
                                        )
                                    ),
                                    routeType = 0
                                ),
                                Stops(
                                    stopName = "East Richmond Station",
                                    stopId = 1059,
                                    routes = arrayListOf(
                                        Routes(
                                            routeName = "Alamein",
                                            routeId = 1,
                                            routeType = 0
                                        ),
                                        Routes(
                                            routeName = "Belgrave",
                                            routeId = 2,
                                            routeType = 0
                                        ),
                                        Routes(
                                            routeName = "Glen Waverley",
                                            routeId = 7,
                                            routeType = 0
                                        ),
                                        Routes(
                                            routeName = "Lilydale",
                                            routeId = 9,
                                            routeType = 0
                                        )
                                    ),
                                    routeType = 0
                                ),
                                Stops(
                                    stopName = "West Richmond Station",
                                    stopId = 1207,
                                    routes = arrayListOf(
                                        Routes(
                                            routeName = "Mernda",
                                            routeId = 5,
                                            routeType = 0
                                        ),
                                        Routes(
                                            routeName = "Hurstbridge",
                                            routeId = 8,
                                            routeType = 0
                                        )
                                    ),
                                    routeType = 0
                                )
                            )
                        )
                    )
                com.example.ptvproject.api.fakeDataType.success ->
                    Response.success(
                        PtvSearchResponse(
                            arrayListOf(
                                Stops(
                                    stopName = "Alamein",
                                    stopId = 10,
                                    routes = arrayListOf(
                                        Routes(
                                            routeName = "Alamein",
                                            routeId = 2,
                                            routeType = 0
                                        )
                                    ),
                                    routeType = 0
                                )
                            )
                        )
                    )
            }
        else {
            return retrofitService.getStation(searchString)
        }
    }

    suspend fun getDepartures(
        routeType: Int,
        stopId: Int,
        fakeDataType: fakeDataType = com.example.ptvproject.api.fakeDataType.success,
    ): Response<PtvDeparturesResponse> {
        if (userDemoData)
            return when (fakeDataType) {
                com.example.ptvproject.api.fakeDataType.failure -> TODO()
                com.example.ptvproject.api.fakeDataType.multipleItems -> TODO()
                com.example.ptvproject.api.fakeDataType.success ->
                    Response.success(
                        PtvDeparturesResponse(
                            arrayListOf(
                                Departures(
                                    stopId = 10,
                                    routeId = 10,
                                    runId = 10,
                                    runRef = "",
                                    directionId = 10,
                                    disruptionIds = listOf(1, 2),
                                    scheduledDepartureUtc = "2pm",
                                    estimatedDepartureUtc = "3pm",
                                    atPlatform = true,
                                    platformNumber = "Seven",
                                    flags = "",
                                    departureSequence = 1
                                )
                            ),

                        )
                    )
            }
        else {
            return retrofitService.getDepartures(routeType, stopId)
        }
    }

    suspend fun getRoutes(
        routeId: Int,
        fakeDataType: fakeDataType = com.example.ptvproject.api.fakeDataType.success,
    ): Response<PtvRoutesResponse> {
        if (userDemoData)
            return when (fakeDataType) {
                com.example.ptvproject.api.fakeDataType.failure ->
                    Response.success(
                        PtvRoutesResponse(
                                Route(
                                    routeType = 0,
                                    routeId = 0,
                                    routeName = "",
                                    routeNumber = "",
                                    routeGtfsId = "",
                                    geopath = listOf("Hey", "Hi")
                                )
                        )
                    )
                com.example.ptvproject.api.fakeDataType.multipleItems,
                -> TODO()
                com.example.ptvproject.api.fakeDataType.success ->
                    Response.success(
                        PtvRoutesResponse(
                                Route(
                                    routeType = 0,
                                    routeId = 7,
                                    routeName = "Glen Waverley",
                                    routeNumber = "",
                                    routeGtfsId = "2-GLW",
                                    geopath = listOf("[]")
                                )
                        )
                    )

            }
        else {
            return retrofitService.getRoutes(routeId)
        }
    }

    suspend fun getDirections(directionId: Int): Response<PtvDirectionsResponse> {
        if (userDemoData)
            return Response.success(
                PtvDirectionsResponse(
                    listOf(
                        Directions(
                            "Outbound toward city",
                            directionId = directionId,
                            "Outbound",
                            routeId = 1,
                            routeType = 0
                        )
                    )
                )
            )
        else {

        return retrofitService.getDirections(directionId)}
    }
}

object PtvApi {
    val PtvRepo: PtvRepository by lazy {
        PtvRepository(false)
    }
}
