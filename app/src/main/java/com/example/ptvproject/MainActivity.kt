package com.example.ptvproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ptvproject.api.PtvService
import com.example.ptvproject.ui.routetypes.RouteTypesViewModel
import com.example.ptvproject.ui.theme.PtvProjectTheme
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val logger = HttpLoggingInterceptor(
//            Logger() {
//                Override fun log(message: String) {
//                    Log.d("OkHttp", message);
//                }
//            }
//        )

        val retrofit = Retrofit.Builder()
            .baseUrl("https://timetableapi.ptv.vic.gov.au/")
            .addConverterFactory(GsonConverterFactory.create())
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

        val service: PtvService = retrofit.create(PtvService::class.java)
        setContent {
            val routeTypesViewModel: RouteTypesViewModel = viewModel()

            /*
            val routeTypesViewModel = remember {
                RouteTypesViewModel()
            }*/

            PtvProjectTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        Greeting("Android")
                        output(routeTypesViewModel)
                        Trigger(routeTypesViewModel)
                    }
                }
            }
        }
    }

    @Composable
    private fun Trigger(routeTypesViewModel: RouteTypesViewModel) {
        Button(onClick = { routeTypesViewModel.getRouteTypes() }) {
            Text("Get route types!")
        }
    }
}

@Composable
fun output(routeTypesViewModel: RouteTypesViewModel) {
    val state = routeTypesViewModel.stateFlow.collectAsState()
    Text(state.value.outputText)
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PtvProjectTheme {
        Greeting("Android")
    }
}

class SignatureAddingInterceptor(
    private val privateKey: String,
    private val developerId: Int,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        //https://timetableapi.ptv.vic.gov.au/path?something=value
        val url = request.url

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

        return chain.proceed(newRequest)
    }

    fun buildTTAPIURL(baseURL: String, privateKey: String, uri: String, developerId: Int): String {
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




