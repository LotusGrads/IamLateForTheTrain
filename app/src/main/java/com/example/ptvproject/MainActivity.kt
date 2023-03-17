package com.example.ptvproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                        SuperButton(service)
                    }
                }
            }
        }
    }
}

@Composable
fun SuperButton(service: PtvService) {
    var output by remember {
        mutableStateOf("test")
    }
    Text(output)
    LaunchedEffect(key1 = "a") {
        delay(1000)
        try {
            val routeTypesResponse = service.getRouteTypes(
                devid = "3002376",
                signature = "EE9FF24FAF161C2FCD10DA238898D43851D79943"
            )

            if (routeTypesResponse.isSuccessful) {
                val successBody = routeTypesResponse.body()
                output = "Success: ${successBody.toString()}"
            } else {
                val errorBody = routeTypesResponse.errorBody().toString()
                output = "Error: ${routeTypesResponse.code()} - $errorBody"
            }
        } catch (e: Throwable) {
            output = "Exception: $e"
        }
    }

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