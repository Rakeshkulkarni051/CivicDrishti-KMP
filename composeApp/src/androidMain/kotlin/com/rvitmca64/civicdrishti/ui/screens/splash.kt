package com.rvitmca64.civicdrishti.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rvitmca64.civicdrishti.R
import com.rvitmca64.civicdrishti.ui.theme.LightPrimaryColor
import com.rvitmca64.civicdrishti.ui.theme.TextPrimary
import kotlinx.coroutines.delay
import androidx.compose.ui.layout.ContentScale


@Composable
fun SplashScreen() {
    val texts = listOf("Spot", "Report", "Fix")
    var currentIndex by remember { mutableStateOf(0) }
    var triggerAnimation by remember { mutableStateOf(0) }

    // Infinite transition for continuous animation
    val infiniteTransition = rememberInfiniteTransition(label = "text_cycle")

    // Progress from 0 to 1 for each word cycle
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = texts.size.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = texts.size * 2000, // 2 seconds per word
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    // Update current index based on animation progress
    LaunchedEffect(animationProgress) {
        val newIndex = animationProgress.toInt() % texts.size
        if (newIndex != currentIndex) {
            currentIndex = newIndex
            triggerAnimation++
        }
    }

    // Calculate phase within current word (0 to 1)
    val wordProgress = animationProgress % 1f

    // Smooth easing for slide-up effect
    val easing = FastOutSlowInEasing

    // Current word animation (sliding up from below)
    val currentAlpha = when {
        wordProgress < 0.25f -> easing.transform(wordProgress / 0.25f)
        wordProgress > 0.75f -> 1f - easing.transform((wordProgress - 0.75f) / 0.25f)
        else -> 1f
    }

    val currentOffsetY = when {
        wordProgress < 0.25f -> 60.dp * (1f - easing.transform(wordProgress / 0.25f))
        wordProgress > 0.75f -> -60.dp * easing.transform((wordProgress - 0.75f) / 0.25f)
        else -> 0.dp
    }

    // Previous word animation (sliding up and fading out)
    val previousIndex = if (currentIndex == 0) texts.size - 1 else currentIndex - 1
    val showPrevious = wordProgress < 0.25f
    val previousAlpha = if (showPrevious) 1f - easing.transform(wordProgress / 0.25f) else 0f
    val previousOffsetY = if (showPrevious) -60.dp * easing.transform(wordProgress / 0.25f) else -60.dp

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 160.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo_transperent),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(210.dp)
                )

                Spacer(modifier = Modifier.height(1.dp))

                // App title
                Text(
                    text = "Civic Drishti",
                    fontFamily = FontFamily(Font(R.font.manrope_bold)),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 48.sp,
                    color = TextPrimary
                )

                // Tagline
                Text(
                    text = "Vision for Better Cities",
                    fontFamily = FontFamily(Font(R.font.inter_18pt_regular)),
                    fontSize = 20.sp,
                    color = LightPrimaryColor.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Animated text container with Box for overlapping animations
                Box(
                    modifier = Modifier.height(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Previous word (sliding up and fading out)
                    if (showPrevious) {
                        Text(
                            text = texts[previousIndex],
                            fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .alpha(previousAlpha)
                                .offset(y = previousOffsetY)
                        )
                    }

                    // Current word (sliding up from below)
                    Text(
                        text = texts[currentIndex],
                        fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .alpha(currentAlpha)
                            .offset(y = currentOffsetY)
                    )
                }
            }

            // Bottom wave graphic
            Image(
                painter = painterResource(id = R.drawable.splash_abstract),
                contentDescription = "Bottom abstract shape",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}