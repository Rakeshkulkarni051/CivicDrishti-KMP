package com.rvitmca64.civicdrishti.ui.screens.report

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rvitmca64.civicdrishti.R
import com.rvitmca64.civicdrishti.ui.theme.PrimaryColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreen(
    reportId: String,
    onNavigateHome: () -> Unit
) {
    // Animation for check icon
    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        // Auto navigate after 5 seconds
        delay(5000)
        onNavigateHome()
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Success Icon with Animation
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .scale(scale.value)
                    .background(
                        color = Color(0xFF4CAF50).copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Success Title
            Text(
                text = "Report Submitted!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                textAlign = TextAlign.Center,
                color = PrimaryColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Success Message
            Text(
                text = "Your civic issue has been successfully reported to the authorities.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Report ID Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "REPORT ID",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily(Font(R.font.inter_18pt_medium))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = reportId,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryColor,
                        fontFamily = FontFamily(Font(R.font.inter_18pt_medium))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Save this for future reference",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.inter_18pt_medium))
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // What's Next Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = PrimaryColor.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "What happens next?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = PrimaryColor,
                        fontFamily = FontFamily(Font(R.font.inter_18pt_medium))
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    NextStepItem("✓", "Your report is being reviewed")
                    NextStepItem("✓", "Authorities will be notified")
                    NextStepItem("✓", "You'll receive status updates")
                    NextStepItem("✓", "Earn civic coins on resolution!")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Thank You Message
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF3CD)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎉",
                        fontSize = 32.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = "Thank you for being a responsible citizen and helping improve our community!",
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                        color = Color(0xFF856404)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Home Button
            Button(
                onClick = onNavigateHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Go to Home",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.inter_18pt_medium))
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Auto Redirect Text
            Text(
                text = "Redirecting to home in 5 seconds...",
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.inter_18pt_medium))
            )
        }
    }
}

@Composable
fun NextStepItem(icon: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = icon,
            fontSize = 16.sp,
            color = Color(0xFF4CAF50),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 12.dp, top = 2.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFF424242),
            fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
            lineHeight = 20.sp
        )
    }
}