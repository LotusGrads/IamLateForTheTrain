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


val PlexSansCondensedRegular = FontFamily(
    Font(R.font.plex_sans_condensed_regular)
)

val PlexSansCondensedBold = FontFamily(
    Font(R.font.plex_sans_condensed_bold)
)

val typography2: Typography
    @Composable get() = androidx.compose.material.Typography(
        body1 = MaterialTheme.typography.body1.copy(
            fontSize = 16.sp
        ),
    )

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = PlexSansCondensedBold,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    h2 = TextStyle(
        fontFamily = PlexSansCondensedBold,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    h3 = TextStyle(
        fontFamily = PlexSansCondensedRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body1 = TextStyle(
        fontFamily = PlexSansCondensedRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    body2 = TextStyle(
        fontFamily = PlexSansCondensedBold,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)