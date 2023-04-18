package com.example.ptvproject.api

import android.util.Log
import com.example.ptvproject.model.PtvSearchResponse
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import retrofit2.Response as Response1

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
    override fun intercept(chain: Interceptor.Chain): Response {
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

    private fun buildTTAPIURL(baseURL: String, privateKey: String, uri: String, developerId: Int): String {
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
    suspend fun getStation(@Path("search") searchString: String): Response1<PtvSearchResponse>
}

object PtvApi {
    val retrofitService: PtvApiService by lazy {
        retrofit.create(PtvApiService::class.java)
    }
}
