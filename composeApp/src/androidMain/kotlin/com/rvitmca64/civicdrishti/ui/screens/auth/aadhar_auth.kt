package com.rvitmca64.civicdrishti.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rvitmca64.civicdrishti.R
import com.rvitmca64.civicdrishti.ui.theme.TextPrimary
import com.rvitmca64.civicdrishti.ui.viewmodels.AuthUiState
import com.rvitmca64.civicdrishti.ui.viewmodels.AuthViewModel

@Composable
fun AadharAuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()

    // Handle UI state changes
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AuthUiState.Success -> {
                Toast.makeText(
                    context,
                    "Welcome ${state.user.name}!",
                    Toast.LENGTH_SHORT
                ).show()

                viewModel.resetUiState()

                // Navigate to main activity
                navController.navigate("main_activity") {
                    popUpTo("aadhar_auth") { inclusive = true }
                }
            }
            is AuthUiState.Error -> {
                Toast.makeText(
                    context,
                    state.message,
                    Toast.LENGTH_LONG
                ).show()
                viewModel.resetUiState()
            }
            else -> {}
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Title
            Text(
                text = "Civic Drishti",
                fontFamily = FontFamily(Font(R.font.manrope_bold)),
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            // Subtitle
            Text(
                text = "Verify Your Identity",
                fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                fontSize = 20.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Grey background container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFDDDDDD))
                    .padding(vertical = 28.dp, horizontal = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Please Verify Your Aadhaar To Ensure\nAuthentic Civic Participation.",
                        fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                        fontSize = 16.sp,
                        color = Color.Black,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Image(
                        painter = painterResource(id = R.drawable.aadhaar_logo),
                        contentDescription = "Aadhaar Logo",
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // ---- Name field ----
                    Text(
                        text = "Enter Your Full Name (As Per Aadhaar)",
                        fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = formState.name,
                        onValueChange = { viewModel.onNameChanged(it) },
                        placeholder = {
                            Text(
                                "Your Name",
                                fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                                color = Color.Gray,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        singleLine = true,
                        enabled = uiState !is AuthUiState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(6.dp)),
                        shape = RoundedCornerShape(6.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledContainerColor = Color.White.copy(alpha = 0.7f)
                        ),
                        textStyle = TextStyle(
                            fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                            color = Color.Black
                        )
                    )

                    if (formState.nameError != null) {
                        Text(
                            text = formState.nameError!!,
                            color = Color.Red,
                            fontSize = 13.sp,
                            modifier = Modifier
                                .padding(top = 4.dp, start = 4.dp)
                                .align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ---- Aadhaar field ----
                    Text(
                        text = "Enter Your 12-Digit Aadhaar Number",
                        fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = formState.aadhaar,
                        onValueChange = { viewModel.onAadhaarChanged(it) },
                        placeholder = {
                            Text(
                                "XXXXXXXXXXXX",
                                fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                                color = Color.Gray,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        singleLine = true,
                        enabled = uiState !is AuthUiState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(6.dp)),
                        shape = RoundedCornerShape(6.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledContainerColor = Color.White.copy(alpha = 0.7f)
                        ),
                        textStyle = TextStyle(
                            fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                            color = Color.Black
                        )
                    )

                    if (formState.aadhaarError != null) {
                        Text(
                            text = formState.aadhaarError!!,
                            color = Color.Red,
                            fontSize = 13.sp,
                            modifier = Modifier
                                .padding(top = 4.dp, start = 4.dp)
                                .align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // ---- Verify button ----
                    Box(
                        modifier = Modifier
                            .width(210.dp)
                            .height(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState is AuthUiState.Loading) {
                            CircularProgressIndicator(
                                color = TextPrimary,
                                modifier = Modifier.size(40.dp)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.verify_btn),
                                contentDescription = "Verify Button",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(50.dp))
                                    .clickable {
                                        viewModel.loginOrRegisterUser()
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}