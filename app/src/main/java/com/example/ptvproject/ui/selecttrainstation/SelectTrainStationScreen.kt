package com.example.ptvproject.ui.selecttrainstation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ptvproject.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.concurrent.TimeUnit

/**
 * Input: User inputs a string
 * Action: Search, click on train station
 * Output: List of train stations
 */

lateinit var locationCallback: LocationCallback
lateinit var locationProvider: FusedLocationProviderClient

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SelectTrainStation(
    viewModel: SelectTrainStationViewModel,
    onTrainStationSelected: (stopId: Int, stationName: String) -> Unit
) {
    Scaffold(
        topBar = {
            PtvAppBar(
                canNavigateBack = true,
                navigateUp = { /*TODO: Handle navigation */ }
            )
        }
    ) {
        val state by viewModel.trainStateFlow.collectAsState()

        SelectTrainStation(
            onSearchClicked = { value: String -> viewModel.generateListOfTrains(value) },
            stateOfTrains = state,
            onTrainStationSelected = onTrainStationSelected,
        )
    }
}

@Composable
private fun SelectTrainStation(
    onSearchClicked: (String) -> Unit,
    stateOfTrains: SelectTrainStationState,
    onTrainStationSelected: (stopId: Int, stationName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val input = remember { mutableStateOf("") }

        // use button and text, then call the functions from the VM
        Spacer(modifier = Modifier.height(30.dp))
        RequestUserLocationPermission()
        RetrieveUserLocation()
        Spacer(modifier = Modifier.height(20.dp))

        SearchBar(
            input = input,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(input.value)
                    focusManager.clearFocus()
                }
            )
        )
        Spacer(modifier = Modifier.height(50.dp))
        when (stateOfTrains) {
            is SelectTrainStationState.Success -> {
                TrainStationList(
                    stationList = stateOfTrains.listOfStations,
                    onTrainStationSelected = onTrainStationSelected
                )
            }

            SelectTrainStationState.NoSearchQuery -> {
                Text(
                    text = "Please type something in",
                    style = MaterialTheme.typography.body1
                )
            }

            SelectTrainStationState.NoTrainStationsFound -> {
                Text(
                    text = "No train stations found :'(",
                    style = MaterialTheme.typography.body1
                )
            }

            SelectTrainStationState.Loading -> {
                CircularProgressIndicator(
                    color = Color(0xFF317B3A)
                )
            }

            SelectTrainStationState.Error -> {
                Text(
                    text = "FATAL ERROR!",
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }


}

@Composable
private fun SearchBar(
    input: MutableState<String>,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier
) {
    //TODO: Insert magnifying glass icon to leadingIcon

    OutlinedTextField(
        value = input.value,
        onValueChange = { value: String ->
            input.value = value
        },
        placeholder = {
            Icon(
                painter = painterResource(R.drawable.baseline_search_24),
                contentDescription = "search icon",
            )
            Text(
                text = "Search for a train station..",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 30.dp)
            )
        },
        shape = RoundedCornerShape(50),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
    )
}

@Composable
private fun TrainStationCard(
    station: SelectTrainStationState.Station,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(10.dp)
            .clickable(onClick = onClick),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = station.stationName,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TrainStationList(
    stationList: List<SelectTrainStationState.Station>,
    onTrainStationSelected: (stopId: Int, stationName: String) -> Unit
) {
    LazyColumn {
        items(stationList) { station ->
            TrainStationCard(
                station = station,
                onClick = { onTrainStationSelected(station.stopId, station.stationName) },
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}

@Composable
private fun RequestUserLocationPermission() {

    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    )
    Button(
        onClick = {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        },
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color(0xFF317B3A)),
        ) {
        Text(
            text = "Use current location as starting point",
            style = MaterialTheme.typography.body1,
        color = Color.White)
    }
}

// call this function when permission has been granted
@Composable
private fun RetrieveUserLocation() {

    val context = LocalContext.current

    val location = getUserLocation(context)
    Text(
        text = location.toString()
    )

}

const val LOCATION_TAG = "LOCATION_TAG"

@SuppressLint("MissingPermission")
@Composable
fun getUserLocation(context: Context): SelectTrainStationState.Location {

    // The Fused Location Provider provides access to location APIs.
    locationProvider = LocationServices.getFusedLocationProviderClient(context)

    var currentUserLocation by remember { mutableStateOf(SelectTrainStationState.Location()) }

    DisposableEffect(key1 = locationProvider) {

        // locationCallBack notifies the user of their location
        locationCallback = object : LocationCallback() {
            // the function below retrieves that location information
            override fun onLocationResult(result: LocationResult) {
                /**
                 * Option 1
                 * This option returns the locations computed, ordered from oldest to newest.
                 * */
                for (location in result.locations) {
                    // Update data class with location data
                    currentUserLocation =
                        SelectTrainStationState.Location(location.latitude, location.longitude)
                    Log.d(LOCATION_TAG, "${location.latitude},${location.longitude}")
                }


                /**
                 * Option 2
                 * This option returns the most recent historical location currently available.
                 * Will return null if no historical location is available
                 * */
                locationProvider.lastLocation
                    .addOnSuccessListener { location ->
                        location?.let {
                            val lat = location.latitude
                            val long = location.longitude
                            // Update data class with location data
                            currentUserLocation =
                                SelectTrainStationState.Location(latitude = lat, longitude = long)
                        }
                    }
                    .addOnFailureListener {
                        Log.e("Location_error", "${it.message}")
                    }

            }
        }
        //2
//        if (hasPermissions(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//        ) {
        initiateLocationRequest()
//        } else {
//            askPermissions(
//                context, REQUEST_LOCATION_PERMISSION, Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//        }
        //3
        onDispose {
            stopLocationUpdate()
        }
    }
    //4
    return currentUserLocation
}

//data class to store the user Latitude and longitude


@SuppressLint("MissingPermission")
fun initiateLocationRequest() {
    locationCallback.let {
        //An encapsulation of various parameters for requesting
        // location through FusedLocationProviderClient.
        val locationRequest: LocationRequest =
            LocationRequest.create().apply {
                interval = TimeUnit.SECONDS.toMillis(60)
                fastestInterval = TimeUnit.SECONDS.toMillis(30)
                maxWaitTime = TimeUnit.MINUTES.toMillis(2)
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
        //use FusedLocationProviderClient to request location update
        locationProvider.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper()
        )
    }

}

fun stopLocationUpdate() {
    try {
        //Removes all location updates for the given callback.
        val removeTask = locationProvider.removeLocationUpdates(locationCallback)
        removeTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(LOCATION_TAG, "Location Callback removed.")
            } else {
                Log.d(LOCATION_TAG, "Failed to remove Location Callback.")
            }
        }
    } catch (se: SecurityException) {
        Log.e(LOCATION_TAG, "Failed to remove Location Callback.. $se")
    }
}

@Composable
fun PtvAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = "Home",
                style = MaterialTheme.typography.h3,
                color = Color.White,
                modifier = Modifier.padding(start = 10.dp)
            )
        },
        backgroundColor = Color(0xFF317B3A),
        modifier = modifier,

    )
}

@Preview
@Composable
private fun SelectTrainStationPreview() {


}
