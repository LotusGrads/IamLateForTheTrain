package com.example.ptvproject.ui.notification

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.Duration.Companion.minutes

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotificationScreen(viewModel: NotificationViewModel) {

    //when we notice the screen appears, we start location request
    //when we notice the screen disappears, we stop location request
    //using disposable effect
    DisposableEffect(key1 = Unit) {
        viewModel.initiateLocationRequest()
        onDispose { viewModel.stopLocationUpdate() }
    }
    Scaffold(
        topBar = {
            PtvAppBar(
                canNavigateBack = true,
                navigateUp = { /*TODO: Handle navigation */ }
            )
        }
    ) {
        val state by viewModel.notificationsUiState.collectAsState()
        NotificationScreen(state = state)
    }


}

@SuppressLint("SuspiciousIndentation")
@Composable
private fun NotificationScreen(
    state: NotificationsUiState,
) {
    val userInfo = state.userInfo
    val onTime = userInfo.onTime
    val estimatedArrivalTime = userInfo.estimatedArrivalTime

    val trainInfo = state.trainInfo
    val stationName = trainInfo.stationName
    val departureTime = trainInfo.departureTime
    val lineName = trainInfo.lineName

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 70.dp,
                    start = 45.dp,
                    end = 45.dp
                )
        ) {
            DisplayUserOnTime(onTime)
            DisplayEstimatedArrivalTime(estimatedArrivalTime)
            DividerLine()
            TrainInfoBlock(stationName, lineName, departureTime)
        }
    }
}

@Composable
private fun DisplayUserOnTime(onTime: Boolean) {
    Column(
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (onTime) {
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .background(color = Color(0xFF5c940d), shape = CircleShape)
                )
                Text(
                    text = "On time! :)",
                    style = MaterialTheme.typography.h4,
                    color = Color(0xFF5c940d),
                    modifier = Modifier.padding(10.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .background(color = Color(0xFFd9480f), shape = CircleShape)
                )
                Text(
                    text = "Walk faster!",
                    style = MaterialTheme.typography.h4,
                    color = Color(0xFFd9480f),
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@Composable
private fun DisplayEstimatedArrivalTime(estimatedArrivalTime: ZonedDateTime?) {

    val timeBlock = if (estimatedArrivalTime != null) {
        DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(estimatedArrivalTime)
    } else {
        "Unknown time"
    }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Text(
            text = "ETA $timeBlock",
            style = MaterialTheme.typography.h6
        )
    }

}

@Composable
private fun DisplayStationName(stationName: String) {
    Column(
    ) {
        Text(
            stationName,
            style = MaterialTheme.typography.h6,
        )
    }
}

@Composable
private fun DisplayLineName(lineName: String) {
    Column() {
        Text(
            lineName,
        )
    }
}

@Composable
private fun DividerLine() {
    Divider(
        modifier = Modifier
            .padding(
                top = 10.dp,
                bottom = 20.dp
            )
            .fillMaxWidth()
            .height(2.dp)
    )
}

@Composable
private fun TrainInfoBlock(stationName: String, lineName: String, departureTime: ZonedDateTime?) {

    val timeBlock = if (departureTime != null) {
        DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(departureTime)
    } else {
        "Unknown time"
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            DisplayStationName(stationName)
            DisplayLineName(lineName)
        }
        Column(
            modifier = Modifier.padding(6.dp)
        ) {
            Text(
                text = timeBlock,
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Preview(
    device = "spec:width=1280dp,height=800dp,dpi=480",
    showBackground = true, showSystemUi = true
)
@Composable
private fun Preview_NotificationScreen_OnTime() {
    NotificationScreen(
        state = NotificationsUiState(
            UserInfo(
                onTime = true,
                estimatedArrivalTime = ZonedDateTime.now().plusMinutes(5)
            ),
            TrainInfo(
                stationName = "Sunshine Station",
                lineName = "to Southern Cross",
                departureTime = ZonedDateTime.now()
            ),
        )
    )
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
                text = "Arrival Time",
                style = MaterialTheme.typography.h3,
                color = Color.White,
                modifier = Modifier.padding(start = 10.dp)
            )
        },
        backgroundColor = Color(0xFF317B3A),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            }
        }
        )
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(
    device = "spec:width=1280dp,height=800dp,dpi=480",
    showBackground = true, showSystemUi = true
)
@Composable
private fun Preview_NotificationScreen_Late() {
    NotificationScreen(
        state = NotificationsUiState(
            UserInfo(
                onTime = false,
                estimatedArrivalTime = ZonedDateTime.now().plusMinutes(5)
            ),
            TrainInfo(
                stationName = "Sunshine Station",
                lineName = "to Southern Cross",
                departureTime = ZonedDateTime.now()
            ),
        )
    )
}