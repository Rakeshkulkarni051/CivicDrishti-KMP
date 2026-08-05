package com.rvitmca64.civicdrishti.ui.screens.user

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rvitmca64.civicdrishti.R
import com.rvitmca64.civicdrishti.ui.viewmodels.AuthViewModel
import com.rvitmca64.civicdrishti.ui.viewmodels.UserProfileViewModel

@Composable
fun User_profile(
    onLogout: () -> Unit = {}, // ✅ RECEIVE LOGOUT CALLBACK FROM MAINSCREEN
    viewModel: UserProfileViewModel = viewModel()
) {
    val user by viewModel.userState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (user != null) {
        User_info(
            name = user!!.name,
            phoneNumber = "XXXXX (Hidden)",
            isAadharVerified = true,
            civicCoins = user!!.civic_coin,
            impactScore = user!!.impact_score,
            totalReports = user!!.total_reports,
            reportsSubmitted = user!!.total_reports,
            issuesResolved = (user!!.total_reports * 0.8).toInt(),
            userRank = 15, // You can calculate this from Firestore later
            achievedBadge = user!!.badges.getOrNull(1) ?: "Urban Vanguard",
            onLogoutClick = {
                // ✅ CLEAR LOCAL VIEWMODEL DATA
                viewModel.logout()

                Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()

                // ✅ CALL LOGOUT CALLBACK TO TRIGGER ROOT NAVIGATION
                onLogout()
            }
        )
    }else {
        // No user logged in
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "No user logged in",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Button(
                    onClick = onLogout
                )
                 {
                    Text("Login")
                }
            }
        }
    }
}

@Composable
fun User_info(
    name: String = "Rakesh Kulkarni",
    phoneNumber: String = "+91 944XXXXXX87",
    isAadharVerified: Boolean = true,
    civicCoins: Int = 35,
    impactScore: Int = 1230,
    totalReports: Int = 12,
    reportsSubmitted: Int = 12,
    issuesResolved: Int = 10,
    userRank: Int = 15,
    achievedBadge: String = "Urban Vanguard",
    onLogoutClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(28.dp))

        // Abstract background image at the top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(176.dp)
                .align(Alignment.TopStart)
                .offset(y = 30.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.user_absrt),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = 16.dp)
                    .offset(x = (-16).dp),
                alignment = Alignment.CenterStart
            )
        }

        // Main content on top of background
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(56.dp))

            // "Your Civic Identity" title
            Text(
                text = "Your Civic Identity",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.padding(start = 24.dp)
            )

            // User Details Card
            UserDetailsCard(
                name = name,
                phoneNumber = phoneNumber,
                isAadharVerified = isAadharVerified
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Drishti Dashboard Section
            DrishtiDashboard(
                civicCoins = civicCoins,
                impactScore = impactScore,
                totalReports = totalReports
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Badge Info Section
            BadgeInfo(
                reportsSubmitted = reportsSubmitted,
                issuesResolved = issuesResolved,
                userRank = userRank,
                achievedBadge = achievedBadge
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Action Buttons Section
            ActionButtons(
                onLogoutClick = onLogoutClick
            )

            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

@Composable
fun UserDetailsCard(
    name: String,
    phoneNumber: String,
    isAadharVerified: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // User icon
            Image(
                painter = painterResource(id = R.drawable.user_icon),
                contentDescription = "User Icon",
                modifier = Modifier.size(width = 86.dp, height = 86.dp)
            )

            Spacer(modifier = Modifier.width(24.dp))

            // User details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Name
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.66f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Phone number
                Text(
                    text = phoneNumber,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.66f)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Aadhar verification status
                if (isAadharVerified) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.verified_badge),
                            contentDescription = "Verified",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Aadhar Verified",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.66f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActionButtons(
    onLogoutClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var showNotificationDialog by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Notifications Button
        ActionButtonItem(
            iconRes = R.drawable.icon_notifi,
            iconSize = 13.dp to 14.dp,
            text = "Notifications",
            onClick = {
                showNotificationDialog = true
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Your Privacy Button
        ActionButtonItem(
            iconRes = R.drawable.icn_privacy,
            iconSize = 18.dp to 18.dp,
            text = "Your Privacy",
            onClick = {
                val privacyUrl = "https://www.civicdrishti.com/privacy"
                if (privacyUrl.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(privacyUrl)
                    }
                    context.startActivity(intent)
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Log Out Button
        ActionButtonItem(
            iconRes = R.drawable.icn_logout,
            iconSize = 13.dp to 13.dp,
            text = "Log Out",
            onClick = onLogoutClick
        )
    }

    // 🔔 Show dialog when Notifications row is tapped
    if (showNotificationDialog) {
        NotificationPreferenceDialog(
            isEnabled = notificationsEnabled,
            onToggle = { notificationsEnabled = it },
            onDismiss = { showNotificationDialog = false }
        )
    }
}

@Composable
fun NotificationPreferenceDialog(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White,
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notification Preference",
                    fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = isEnabled,
                    onCheckedChange = { onToggle(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF54B39C),
                        uncheckedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFBFE7D9),
                        uncheckedTrackColor = Color(0xFFCCCCCC)
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Done",
                    fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                    fontSize = 14.sp,
                    color = Color(0xFF54B39C)
                )
            }
        }
    )
}

@Composable
fun ActionButtonItem(
    iconRes: Int,
    iconSize: Pair<androidx.compose.ui.unit.Dp, androidx.compose.ui.unit.Dp>,
    text: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(),
                onClick = onClick
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier.size(width = iconSize.first, height = iconSize.second)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Text
        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
            fontSize = 20.sp,
            color = Color.Black.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun DrishtiDashboard(
    civicCoins: Int,
    impactScore: Int,
    totalReports: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Dashboard Title
        Text(
            text = "Drishti Dashboard",
            fontFamily = FontFamily(Font(R.font.manrope_bold)),
            fontSize = 22.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dashboard Card
        Box(
            modifier = Modifier
                .width(392.dp)
                .height(124.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(Color(0xFFD9D9D9))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Coin Image
                Image(
                    painter = painterResource(id = R.drawable.coin),
                    contentDescription = "Civic Coin",
                    modifier = Modifier.size(width = 69.dp, height = 69.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Civic Coins Section
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Civic Coins",
                        fontFamily = FontFamily(Font(R.font.manrope_bold)),
                        fontSize = 12.sp,
                        color = Color.Black.copy(alpha = 0.5f)
                    )

                    Text(
                        text = civicCoins.toString(),
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontSize = 32.sp,
                        color = Color(0xFFF99500)
                    )

                    Text(
                        text = "Redeem Now >",
                        fontFamily = FontFamily(Font(R.font.manrope_bold)),
                        fontSize = 10.sp,
                        color = Color(0xFF0088D6).copy(alpha = 0.5f)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Impact Score Section
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Your Impact Score",
                        fontFamily = FontFamily(Font(R.font.manrope_bold)),
                        fontSize = 12.sp,
                        color = Color.Black.copy(alpha = 0.5f)
                    )

                    Text(
                        text = impactScore.toString(),
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontSize = 30.sp,
                        color = Color(0xFFF99500)
                    )

                    Text(
                        text = "Out of $totalReports Reports",
                        fontFamily = FontFamily(Font(R.font.manrope_bold)),
                        fontSize = 10.sp,
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun BadgeInfo(
    reportsSubmitted: Int,
    issuesResolved: Int,
    userRank: Int,
    achievedBadge: String
) {
    Box(
        modifier = Modifier
            .width(410.dp)
            .height(202.dp)
            .padding(horizontal = 16.dp)
    ) {
        // Dotted Frame Background
        Image(
            painter = painterResource(id = R.drawable.dotted_frame),
            contentDescription = "Badge Frame",
            modifier = Modifier.matchParentSize()
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Side - Stats
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Reports Submitted
                Column {
                    Text(
                        text = "Reports Submitted",
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "$reportsSubmitted Total Civic Reports",
                        fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Issues Resolved
                Column {
                    Text(
                        text = "Issues Resolved",
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "$issuesResolved Successfully Resolved",
                        fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Your Rank
                Column {
                    Text(
                        text = "Your Rank",
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "#$userRank in Your City",
                        fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Right Side - Badge
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(0.6f)
                    .offset(x = 14.dp)
            ) {
                Text(
                    text = "My Badge",
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontSize = 14.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Image(
                    painter = painterResource(id = R.drawable.urban_vangaurd),
                    contentDescription = "Badge",
                    modifier = Modifier.size(width = 49.dp, height = 53.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = achievedBadge,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontSize = 8.sp,
                    color = Color(0xFFF99500)
                )
            }
        }
    }
}