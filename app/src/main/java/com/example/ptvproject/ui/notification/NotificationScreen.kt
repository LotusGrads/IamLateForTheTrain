package com.example.ptvproject.ui.notification

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun TrainInfoCard(
    modifier: Modifier = Modifier,
    userTrainInfo: NotificationViewModel) {

    Text(
        text = userTrainInfo
    )




}