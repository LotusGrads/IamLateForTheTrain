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
import com.example.ptvproject.ui.theme.PTVprojectTheme
import kotlinx.coroutines.delay
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://timetableapi.ptv.vic.gov.au/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: PtvService = retrofit.create(PtvService::class.java)
        setContent {
            PTVprojectTheme {

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
    PTVprojectTheme {
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




