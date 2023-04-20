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
import androidx.navigation.NavGraph
import com.example.ptvproject.ui.selecttrainstation.SelectTrainStation
import com.example.ptvproject.ui.selecttrainstation.SelectTrainStationViewModel
import com.example.ptvproject.ui.theme.PTVprojectTheme
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PTVprojectTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}
