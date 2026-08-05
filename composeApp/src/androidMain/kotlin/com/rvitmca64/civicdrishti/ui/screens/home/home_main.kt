package com.rvitmca64.civicdrishti.ui.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rvitmca64.civicdrishti.R
import com.rvitmca64.civicdrishti.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

// 🏠 MAIN HOME SCREEN
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeMainScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel() // ✅ ADD AUTHVIEWMODEL
) {
    // ✅ GET LOGGED IN USER
    val loggedInUser by authViewModel.loggedInUser.collectAsState()
    val userName = loggedInUser?.name ?: "Guest"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.home_abst),
                contentDescription = "Top Abstract Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Image(
                painter = painterResource(id = R.drawable.greenimage_home),
                contentDescription = "Green City Illustration",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(430.dp)
                    .height(192.dp)
                    .offset(y = (-30).dp)
                    .graphicsLayer {
                        alpha = 0.85f
                        blendMode = BlendMode.Overlay
                    }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 24.dp, top = 30.dp)
                    .align(Alignment.TopStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_overlay),
                    contentDescription = "Logo Transparent",
                    modifier = Modifier
                        .size(50.dp)
                        .alpha(0.5f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "CIVIC DRISHTI",
                    fontFamily = FontFamily(Font(R.font.manrope_bold)),
                    fontSize = 32.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .align(Alignment.TopStart)
                    .padding(top = 110.dp)
            ) {
                var showFirst by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    while (true) {
                        delay(3000)
                        showFirst = !showFirst
                    }
                }

                AnimatedContent(
                    targetState = showFirst,
                    transitionSpec = {
                        slideInHorizontally(
                            animationSpec = tween(450),
                            initialOffsetX = { it }
                        ) + fadeIn(animationSpec = tween(400)) with
                                slideOutHorizontally(
                                    animationSpec = tween(450),
                                    targetOffsetX = { -it }
                                ) + fadeOut(animationSpec = tween(300))
                    },
                    label = "home-slide"
                ) { isFirst ->
                    if (isFirst) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // ✅ DISPLAY USER NAME
                            Text(
                                text = "Welcome, $userName!",
                                fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                                fontSize = 22.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "A Crowdsourced Platform Helping Authorities Identify And Resolve Civic Issues Faster.",
                                fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                                fontSize = 18.sp,
                                color = Color.White.copy(alpha = 0.7f),
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    text = "Spot",
                                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                                    fontSize = 28.sp,
                                    color = Color(0xFFD7D7D7).copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "Report",
                                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                                    fontSize = 28.sp,
                                    color = Color(0xFFFFB84E).copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "Fix",
                                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                                    fontSize = 28.sp,
                                    color = Color(0xFFD7D7D7).copy(alpha = 0.7f)
                                )
                            }
                        }
                    } else {
                        SecondSlideCard()
                    }
                }
            }
        }

        ReportIssueCard(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .offset(y = (-38).dp)
                .align(Alignment.CenterHorizontally)
        ) {
            navController.navigate("ReportIssueScreen")
        }

        TrustBoard(
            location = "JP Nagar 7th Phase, Bengaluru South",
            issuesReported = 110,
            resolved = 98,
            trustScore = 75.09f,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally)
                .offset(y = (-18).dp)
        )
    }
}

// 🧍 SECOND SLIDE CARD (Avatar behind text)
@Composable
private fun SecondSlideCard() {
    Box(
        modifier = Modifier
            .width(512.dp)
            .height(266.dp)
            .offset(y = (-40).dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.cityhero_avtar),
            contentDescription = "City Hero Avatar",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = 14.dp)
                .width(210.dp)
                .height(214.dp)
                .zIndex(0f)
        )

        Column(
            modifier = Modifier
                .matchParentSize()
                .padding(start = 1.dp, top = 20.dp, end = 20.dp, bottom = 10.dp)
                .zIndex(1f)
        ) {
            Text(
                text = "Recognizing Citizens\nWho Make Cities\nBetter",
                fontFamily = FontFamily(Font(R.font.manrope_bold)),
                fontSize = 22.sp,
                lineHeight = 30.sp,
                color = Color.White,
                maxLines = 3,
                modifier = Modifier.offset(y = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-30).dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "City Hero",
                            fontFamily = FontFamily(Font(R.font.poppins_extrabold)),
                            fontSize = 26.sp,
                            color = Color(0xFF54B39C)
                        )
                        Text(
                            text = "  |  ",
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontSize = 20.sp,
                            color = Color(0x80333333)
                        )
                        Text(
                            text = "Anil Kumar",
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "133+ Report This Month",
                        fontFamily = FontFamily(Font(R.font.manrope_bold)),
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// 📸 REPORT ISSUE CARD (Clickable button card)
@Composable
fun ReportIssueCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(52.dp))
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.report_issue_card),
            contentDescription = "Report issue card",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .width(140.dp)
                    .fillMaxHeight()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.camera_icon),
                    contentDescription = "Camera Icon",
                    modifier = Modifier
                        .size(width = 69.dp, height = 77.dp)
                        .offset(x = 30.dp)
                        .offset(y = 8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Image(
                    painter = painterResource(id = R.drawable.report_btn),
                    contentDescription = "Report Button",
                    modifier = Modifier
                        .width(151.dp)
                        .height(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Every Report\nHelps Make Your\nCity Better.",
                fontFamily = FontFamily(Font(R.font.inter_18pt_bold)),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                color = Color(0xFF1A1A1A),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// 📊 TRUST BOARD
@Composable
fun TrustBoard(
    location: String,
    issuesReported: Int,
    resolved: Int,
    trustScore: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(392.dp)
            .height(290.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFD9D9D9))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Text(
                text = "Trust Board",
                fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                fontSize = 22.sp,
                color = Color.Black.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location_pin),
                    contentDescription = "Location",
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = location,
                    fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                    fontSize = 12.sp,
                    color = Color(0xFF4D4D4D)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "ISSUES REPORTED",
                        fontFamily = FontFamily(Font(R.font.manrope_bold)),
                        fontSize = 12.sp,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = issuesReported.toString(),
                        fontFamily = FontFamily(Font(R.font.inter_18pt_extrabold)),
                        fontSize = 22.sp,
                        color = Color(0xFF20605F)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "RESOLVED",
                        fontFamily = FontFamily(Font(R.font.manrope_bold)),
                        fontSize = 12.sp,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = resolved.toString(),
                        fontFamily = FontFamily(Font(R.font.inter_18pt_extrabold)),
                        fontSize = 22.sp,
                        color = Color(0xFFFFB84E)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .offset(x = (-26).dp)
                        .offset(y = (-4).dp)
                ) {
                    Canvas(modifier = Modifier.size(100.dp)) {
                        val strokeWidth = 16.dp.toPx()

                        drawArc(
                            color = Color(0xFF20605F),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )

                        drawArc(
                            color = Color(0xFF54B39C),
                            startAngle = -90f,
                            sweepAngle = (trustScore / 100f) * 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = String.format("%.2f%%", trustScore),
                            fontFamily = FontFamily(Font(R.font.inter_18pt_bold)),
                            fontSize = 16.sp,
                            color = Color(0xFF54B39C)
                        )
                        Text(
                            text = "TRUST SCORE",
                            fontFamily = FontFamily(Font(R.font.manrope_bold)),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}