package com.example.ptvproject.ui.notification

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.Duration.Companion.minutes

@Composable
fun Notification(viewModel: NotificationViewModel) {
    val state by viewModel.notificationsUiState.collectAsState()
    NotificationScreen(state = state)
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
            .width(intrinsicSize = IntrinsicSize.Min)

    ) {
        Column(
            modifier = Modifier
                .widthIn(100.dp, 400.dp)
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
                    style = MaterialTheme.typography.h3,
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
                    style = MaterialTheme.typography.h3,
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
            style = MaterialTheme.typography.body1,
            fontSize = 18.sp
        )
    }
}

@Composable
private fun DisplayLineName(lineName: String) {
    Column() {
        Text(
            lineName,
            style = MaterialTheme.typography.body1,
            fontSize = 15.sp
        )
    }
}

@Composable
private fun DividerLine() {

    Column(
        modifier = Modifier
            .padding(
                top = 10.dp,
                bottom = 20.dp
            )
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
        )
    }
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
        Column {
            Text(
                text = timeBlock,
                style = MaterialTheme.typography.body1,
                fontSize = 18.sp
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
private fun Preview_NotificationScreen() {
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
            )
        ),
    )
}