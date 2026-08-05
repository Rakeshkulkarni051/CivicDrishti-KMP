package com.rvitmca64.civicdrishti.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rvitmca64.civicdrishti.R
import com.rvitmca64.civicdrishti.ui.theme.LightPrimaryColor
import com.rvitmca64.civicdrishti.ui.theme.PrimaryColor
import com.rvitmca64.civicdrishti.ui.theme.TextPrimary

@Composable
fun WelcomeScreen(
    onCivilianSelected: () -> Unit = {}   // Navigate to AadharAuthScreen
) {
    var selectedRole by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Top abstract background
            Image(
                painter = painterResource(id = R.drawable.wellcome_abstract),
                contentDescription = "Top Abstract",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )

            // Content Column
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.height(150.dp))

                // Welcome Text
                Text(
                    text = "Welcome to",
                    fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                    fontSize = 24.sp,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Civic Drishti text (two color)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "Civic ",
                        fontFamily = FontFamily(Font(R.font.manrope_bold)),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryColor
                    )
                    Text(
                        text = "Drishti",
                        fontFamily = FontFamily(Font(R.font.manrope_bold)),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightPrimaryColor
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Tagline
                Text(
                    text = "Different Roles. One Vision.",
                    fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                    fontSize = 18.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Role buttons (Civilian / Authority)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoleButton(
                        image = R.drawable.civilianpng,
                        label = "CIVILIAN",
                        selected = selectedRole == "CIVILIAN",
                        onClick = { selectedRole = "CIVILIAN" }
                    )

                    RoleButton(
                        image = R.drawable.authoritypng,
                        label = "AUTHORITY",
                        selected = selectedRole == "AUTHORITY",
                        onClick = { selectedRole = "AUTHORITY" }
                    )
                }

                Spacer(modifier = Modifier.height(68.dp))

                // Get Started button image
                Image(
                    painter = painterResource(id = R.drawable.start_btn),
                    contentDescription = "Get Started Button",
                    modifier = Modifier
                        .width(318.dp)
                        .height(58.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .clickable {
                            when (selectedRole) {
                                "CIVILIAN" -> onCivilianSelected()
                                "AUTHORITY" -> showDialog = true
                            }
                        },
                    contentScale = ContentScale.FillBounds
                )
            }

            // ---------- Authority Info Dialog ----------
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text(
                                "OK",
                                color = PrimaryColor,
                                fontFamily = FontFamily(Font(R.font.inter_18pt_medium))
                            )
                        }
                    },
                    title = {
                        Text(
                            "For Authorities",
                            fontFamily = FontFamily(Font(R.font.manrope_bold)),
                            fontSize = 22.sp,
                            color = PrimaryColor
                        )
                    },
                    text = {
                        Text(
                            "Authorities have a dedicated desktop app to resolve and monitor civic issues in real time.",
                            fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    },
                    containerColor = Color.White,
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
    }
}

@Composable
fun RoleButton(
    image: Int,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (selected) PrimaryColor else Color.Transparent
    val bgColor = if (selected) LightPrimaryColor.copy(alpha = 0.15f) else Color.White

    Column(
        modifier = Modifier
            .size(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = label,
            modifier = Modifier.size(70.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
            fontSize = 14.sp,
            color = TextPrimary
        )
    }
}
