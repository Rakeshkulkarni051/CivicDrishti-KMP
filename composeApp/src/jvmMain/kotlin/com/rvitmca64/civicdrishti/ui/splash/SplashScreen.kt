package com.rvitmca64.civicdrishti.ui.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rvitmca64.civicdrishti.ui.theme.Inter
import com.rvitmca64.civicdrishti.ui.theme.Manrope
import kotlinx.coroutines.delay

/**
 * Splash Screen for Civic Drishti Desktop Application
 *
 * Features:
 * - Full-screen background image
 * - Logo + text fade-in animation
 * - Custom fonts (Manrope & Inter)
 * - Auto navigation after delay
 */
@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit
) {
    // Controls visibility for fade-in animation
    var isVisible by remember { mutableStateOf(false) }

    // Alpha animation (0f -> 1f)
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 3000),
        label = "splash_fade_in"
    )

    // Start animation and auto navigate
    LaunchedEffect(Unit) {
        isVisible = true
        delay(5000)
        onNavigateToLogin()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Background image
        Image(
            painter = painterResource("images/splash_Union.png"),
            contentDescription = "Splash Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Center content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Logo
                Image(
                    painter = painterResource("images/logo.png"),
                    contentDescription = "Civic Drishti Logo",
                    modifier = Modifier
                        .width(400.dp)
                        .graphicsLayer { this.alpha = alpha },
                    contentScale = ContentScale.FillWidth
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Title: Civic Drishti
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = Color(0xFF20605F),
                                fontSize = 112.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = Manrope
                            )
                        ) {
                            append("Civic ")
                        }
                        withStyle(
                            SpanStyle(
                                color = Color(0xFF54B39C),
                                fontSize = 112.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = Manrope
                            )
                        ) {
                            append("Drishti")
                        }
                    },
                    modifier = Modifier.graphicsLayer { this.alpha = alpha }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Subtitle
                Text(
                    text = "Vision for Better Cities",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Inter,
                    color = Color(0xFF20605F),
                    modifier = Modifier.graphicsLayer { this.alpha = alpha }
                )
            }
        }
    }
}
