package com.example.ptvproject.ui.routetypes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ptvproject.SignatureAddingInterceptor
import com.example.ptvproject.api.PtvService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class RouteTypesViewModel : ViewModel() {
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

    private val mutableStateFlow: MutableStateFlow<RouteTypesUiState> =
        MutableStateFlow(RouteTypesUiState())
    val stateFlow = mutableStateFlow.asStateFlow()

    fun getRouteTypes() {
        viewModelScope.launch(context = Dispatchers.IO) {
            var output: String = ""

            try {
                val routeTypesResponse = service.getRouteTypes()


                if (routeTypesResponse.isSuccessful) {
                    val successBody = routeTypesResponse.body()
                    val firstRoute = successBody?.routeTypes?.firstOrNull { it.routeType == 2 }

                    output = "Success: ${firstRoute?.routeTypeName ?: "Invalid route"}"


                } else {
                    val errorBody = routeTypesResponse.errorBody().toString()
                    output = "Error: ${routeTypesResponse.code()} - $errorBody"
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }


            mutableStateFlow.emit(RouteTypesUiState(output))
        }

    }
}