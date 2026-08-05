package com.rvitmca64.civicdrishti.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rvitmca64.civicdrishti.ui.theme.Manrope
import com.rvitmca64.civicdrishti.ui.theme.Inter
import com.rvitmca64.civicdrishti.ui.theme.Poppins

/**
 * Login Screen for Civic Drishti Authority Dashboard
 *
 * Features:
 * - Department selection dropdown
 * - Department ID validation
 * - Hardcoded credential verification
 * - Navigation to dashboard on success
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    // Valid department credentials (hardcoded)
    val validDeptIds = mapOf(
        "BBMP" to "BBMP123",
        "Sanitation" to "SAN456",
        "Streetlights" to "STREET789",
        "Roads" to "RD789",
        "Garbage" to "GRB101"
    )

    // State management
    var selectedDepartment by remember { mutableStateOf<String?>(null) }
    var departmentId by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    // Department options
    val departments = listOf("BBMP", "Sanitation", "Streetlights", "Roads", "Garbage")

    // Handle login validation
    fun handleLogin() {
        when {
            selectedDepartment == null -> {
                showError = true
                errorMessage = "Please select a department"
            }
            departmentId.isBlank() -> {
                showError = true
                errorMessage = "Please enter Department ID"
            }
            validDeptIds[selectedDepartment] != departmentId -> {
                showError = true
                errorMessage = "Invalid Department ID for selected department"
            }
            else -> {
                showError = false
                onLoginSuccess()
            }
        }
    }

    // Main layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(40.dp)
                .widthIn(max = 1200.dp)
        ) {
            // Welcome Text
            Text(
                text = "Welcome to",
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Inter,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // App Title: Civic Drishti
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = Color(0xFF20605F),
                            fontSize = 80.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = Manrope
                        )
                    ) {
                        append("Civic ")
                    }
                    withStyle(
                        SpanStyle(
                            color = Color(0xFF54B39C),
                            fontSize = 80.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = Manrope
                        )
                    ) {
                        append("Drishti")
                    }
                },
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle: Authority Dashboard
            Text(
                text = "Authority Dashboard",
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = Poppins,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Form Section (Left Aligned)
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.width(800.dp)
            ) {
                // Department Label
                Text(
                    text = "Department",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Manrope,
                    color = Color(0xFF4A4A4A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Department Dropdown
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFE8E8E8), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(8.dp))
                            .clickable { dropdownExpanded = true }
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedDepartment ?: "Select Department",
                                fontSize = 18.sp,
                                color = if (selectedDepartment == null) Color(0xFF999999) else Color.Black,
                                fontFamily = Manrope
                            )
                            // Simple dropdown arrow using text
                            Text(
                                text = "▼",
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }

                    // Dropdown Menu
                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier.width(800.dp)
                    ) {
                        departments.forEach { dept ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedDepartment = dept
                                    dropdownExpanded = false
                                    showError = false
                                }
                            ) {
                                Text(
                                    text = dept,
                                    fontSize = 16.sp,
                                    fontFamily = Manrope
                                )
                            }
                        }
                    }
                }

                // Helper text
                Text(
                    text = "e.g Sanitation, Streetlights, Roads",
                    fontSize = 14.sp,
                    color = Color(0xFF999999),
                    fontFamily = Manrope,
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Department ID Label
                Text(
                    text = "Department ID",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Manrope,
                    color = Color(0xFF4A4A4A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Department ID TextField
                TextField(
                    value = departmentId,
                    onValueChange = {
                        departmentId = it
                        showError = false
                    },
                    placeholder = {
                        Text(
                            text = "Enter Dept Access Code",
                            fontSize = 18.sp,
                            color = Color(0xFF999999),
                            fontFamily = Manrope
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFE8E8E8),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        textColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 18.sp,
                        fontFamily = Manrope
                    ),
                    singleLine = true
                )

                // Error message
                if (showError) {
                    Text(
                        text = errorMessage,
                        fontSize = 14.sp,
                        color = Color.Red,
                        fontFamily = Manrope,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Go to Dashboard Button
            Button(
                onClick = { handleLogin() },
                modifier = Modifier
                    .width(320.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF20605F)
                ),
                shape = RoundedCornerShape(50) // Pill shape
            ) {
                Text(
                    text = "Go to Dashboard",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Manrope,
                    color = Color.White
                )
            }
        }
    }
}