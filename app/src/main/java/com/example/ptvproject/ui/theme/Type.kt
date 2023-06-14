package com.example.ptvproject.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ptvproject.R



val PlexSansCondensed = FontFamily(
    Font(R.font.plex_sans_condensed_regular),
    Font(R.font.plex_sans_condensed_bold)
)

val typography2: Typography
    @Composable get() = androidx.compose.material.Typography(
        body1 = MaterialTheme.typography.body1.copy(
            fontSize = 16.sp
        ),
    )

val typography: Typography
    @Composable get() = androidx.compose.material.Typography(
        defaultFontFamily = PlexSansCondensed,
        h1 = MaterialTheme.typography.h1.copy(
            fontFamily = null,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        h2 = MaterialTheme.typography.h2.copy(
            fontFamily = null,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
        h3 = MaterialTheme.typography.h3.copy(
            fontFamily = null,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        body1 = MaterialTheme.typography.body1.copy(
            fontFamily = null,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        body2 = MaterialTheme.typography.body2.copy(
            fontFamily = null,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    )
