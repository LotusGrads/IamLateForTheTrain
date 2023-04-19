package com.example.ptvproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ptvproject.ui.selecttrainstation.SelectTrainStation
import com.example.ptvproject.ui.selecttrainstation.SelectTrainStationViewModel
import com.example.ptvproject.ui.theme.PTVprojectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PTVprojectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //DestinationsNavHost(navGraph = NavGraphs.root)
                    SelectTrainStation(SelectTrainStationViewModel())
                }
            }
        }
    }
}