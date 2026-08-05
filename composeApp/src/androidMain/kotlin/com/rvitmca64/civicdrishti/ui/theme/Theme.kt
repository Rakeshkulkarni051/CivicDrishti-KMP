package com.rvitmca64.civicdrishti.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = BackgroundLight,
    secondary = SecondaryColor,
    background = BackgroundLight,
    onBackground = TextPrimary,
)

@Composable
fun CivicDrishtiTheme(
    content: @Composable () -> Unit
) {
    // ✅ Always use light mode
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
