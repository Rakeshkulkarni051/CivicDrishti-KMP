package com.rvitmca64.civicdrishti.ui.screens.report

import android.Manifest
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.rvitmca64.civicdrishti.ui.viewmodels.ReportUiState
import android.graphics.BitmapFactory
import com.rvitmca64.civicdrishti.ui.viewmodels.AuthViewModel
import com.rvitmca64.civicdrishti.ui.viewmodels.ReportViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ReportIssueScreen(
    viewModel: ReportViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(), // ✅ ADD AUTHVIEWMODEL
    onNavigateBack: () -> Unit,
    onNavigateToSuccess: (String) -> Unit
) {
    val context = LocalContext.current

    // ✅ GET REAL LOGGED-IN USER
    val loggedInUser by authViewModel.loggedInUser.collectAsState()

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    val uiState by viewModel.uiState.collectAsState()
    val capturedImage by viewModel.capturedImage.collectAsState()
    val detectedLocation by viewModel.detectedLocation.collectAsState()
    val selectedLocation by viewModel.selectedLocation.collectAsState()
    val locationAddress by viewModel.locationAddress.collectAsState()
    val description by viewModel.description.collectAsState()

    var showCameraScreen by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // ✅ DEBUG: Log user info when screen loads
    LaunchedEffect(loggedInUser) {
        if (loggedInUser == null) {
            Log.e("ReportIssueScreen", "❌ NO USER LOGGED IN!")
        } else {
            Log.d("ReportIssueScreen", "✅ User logged in:")
            Log.d("ReportIssueScreen", "   User ID: ${loggedInUser!!.userId}")
            Log.d("ReportIssueScreen", "   Name: ${loggedInUser!!.name}")
        }
    }

    LaunchedEffect(Unit) {
        if (!permissionsState.allPermissionsGranted) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(permissionsState.allPermissionsGranted) {
        if (permissionsState.allPermissionsGranted && detectedLocation == null) {
            viewModel.detectCurrentLocation()
        }
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is ReportUiState.Success -> {
                viewModel.resetUiState()
                onNavigateToSuccess(state.reportId)
            }
            is ReportUiState.Error -> {
                errorMessage = state.message
                showErrorDialog = true
                delay(3000)
                viewModel.resetUiState()
            }
            else -> {}
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(64.dp)
                )
            },
            title = {
                Text(
                    text = "Error",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            },
            text = {
                Text(
                    text = errorMessage,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F)
                    )
                ) {
                    Text("OK")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (showCameraScreen) {
        CameraScreen(
            onPhotoCaptured = { bytes, filePath ->
                viewModel.setCapturedImage(bytes, filePath)
                showCameraScreen = false
            },
            onBack = { showCameraScreen = false }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report Civic Issue") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Spot It ? Report It!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                // CAMERA CARD
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clickable {
                            if (permissionsState.allPermissionsGranted) {
                                showCameraScreen = true
                            } else {
                                permissionsState.launchMultiplePermissionRequest()
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (capturedImage != null) Color.Transparent else Color.LightGray
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        if (capturedImage != null) {
                            val bitmap = BitmapFactory.decodeByteArray(
                                capturedImage!!.bytes, 0, capturedImage!!.bytes.size
                            )
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Captured Issue",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            IconButton(
                                onClick = { showCameraScreen = true },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            ) {
                                Icon(Icons.Default.Refresh, "Retake", tint = Color.White)
                            }

                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Camera,
                                    contentDescription = "Add Photo",
                                    modifier = Modifier.size(48.dp),
                                    tint = Color.DarkGray
                                )
                                Text("Tap to capture photo", color = Color.DarkGray)
                            }
                        }
                    }
                }

                // LOCATION
                Column {
                    Text("Detected Location", fontSize = 16.sp, fontWeight = FontWeight.Bold)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = selectedLocation?.let {
                                    "${String.format("%.4f", it.latitude)}, ${String.format("%.4f", it.longitude)}"
                                } ?: "Detecting...",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                            Text(locationAddress, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }

                Text("Pin Location Manually", fontSize = 16.sp)

                if (selectedLocation != null) {
                    GoogleMapView(
                        location = selectedLocation!!,
                        onLocationSelected = { viewModel.updateSelectedLocation(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        colors = CardDefaults.cardColors(Color.LightGray)
                    ) {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }

                // DESCRIPTION
                Column {
                    Text("Description", fontSize = 16.sp)

                    OutlinedTextField(
                        value = description,
                        onValueChange = { viewModel.updateDescription(it) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        trailingIcon = {
                            Text("${description.length}/100", fontSize = 12.sp, color = Color.Gray)
                        },
                        placeholder = { Text("Write Short Description About Report") }
                    )
                }

                Spacer(Modifier.height(80.dp))
            }

            // ✅ FIXED BUTTON - USES REAL USER DATA
            Button(
                onClick = {
                    if (loggedInUser == null) {
                        // Should never happen, but handle gracefully
                        errorMessage = "Please login to submit reports"
                        showErrorDialog = true
                    } else {
                        // ✅ USE REAL USER DATA
                        Log.d("ReportIssueScreen", "Submitting report with:")
                        Log.d("ReportIssueScreen", "  User ID: ${loggedInUser!!.userId}")
                        Log.d("ReportIssueScreen", "  User Name: ${loggedInUser!!.name}")

                        viewModel.submitReport(
                            userId = loggedInUser!!.userId,    // ✅ REAL USER ID
                            userName = loggedInUser!!.name     // ✅ REAL USER NAME
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                enabled = viewModel.isFormValid() &&
                        uiState !is ReportUiState.Loading &&
                        loggedInUser != null  // ✅ ONLY ENABLED IF USER LOGGED IN
            ) {
                if (uiState is ReportUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("REPORT", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun GoogleMapView(
    location: LatLng,
    onLocationSelected: (LatLng) -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
    }

    var markerPosition by remember { mutableStateOf(location) }

    LaunchedEffect(location) {
        markerPosition = location
        cameraPositionState.position = CameraPosition.fromLatLngZoom(location, 15f)
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        onMapClick = {
            markerPosition = it
            onLocationSelected(it)
        }
    ) {
        Marker(
            state = MarkerState(position = markerPosition),
            title = "Selected Location",
            draggable = true,
            onClick = { false }
        )
    }
}