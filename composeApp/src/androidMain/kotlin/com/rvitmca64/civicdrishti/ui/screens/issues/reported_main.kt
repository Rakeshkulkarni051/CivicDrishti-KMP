package com.rvitmca64.civicdrishti.ui.screens.issues

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.rvitmca64.civicdrishti.R
import com.rvitmca64.civicdrishti.data.model.ReportData
import com.rvitmca64.civicdrishti.ui.viewmodels.AuthViewModel
import com.rvitmca64.civicdrishti.ui.viewmodels.ReportedIssuesViewModel
import com.rvitmca64.civicdrishti.ui.viewmodels.ReportStatusFilter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun IssuesReported(
    authViewModel: AuthViewModel = viewModel(),
    reportedIssuesViewModel: ReportedIssuesViewModel = viewModel()
) {
    val loggedInUser by authViewModel.loggedInUser.collectAsState()
    val uiState by reportedIssuesViewModel.uiState.collectAsState()
    val selectedFilter by reportedIssuesViewModel.selectedFilter.collectAsState()

    // Load reports when user is available
    LaunchedEffect(loggedInUser) {
        loggedInUser?.let { user ->
            reportedIssuesViewModel.loadUserReports(user.userId)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Abstract Section with Text Overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(162.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.reported_abstr),
                contentDescription = "Track Issues Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(16.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 14.dp, end = 24.dp, top = 4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Track Reported Issues",
                    fontFamily = FontFamily(Font(R.font.manrope_bold)),
                    fontSize = 30.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "View And Track Your Submissions",
                    fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ALL Reports Filter Chip (single)
        if (!uiState.isEmpty && !uiState.isLoading) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                FilterChip(
                    selected = true,
                    onClick = { },
                    label = {
                        Text(
                            text = "ALL Reports",
                            fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                            fontSize = 13.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF54B39C),
                        selectedLabelColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            when {
                uiState.isLoading -> {
                    // Skeleton Loading
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        // Summary Card Skeleton
                        item {
                            SkeletonSummaryCard()
                        }

                        // Report Cards Skeleton
                        items(3) {
                            SkeletonReportCard()
                        }
                    }
                }

                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_location_pin),
                                contentDescription = "Error",
                                tint = Color.Red,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = uiState.errorMessage ?: "Something went wrong",
                                fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                                fontSize = 16.sp,
                                color = Color.Red
                            )
                            Button(
                                onClick = {
                                    loggedInUser?.let { user ->
                                        reportedIssuesViewModel.refreshReports(user.userId)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF54B39C)
                                )
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }

                uiState.isEmpty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.camera_icon),
                                contentDescription = "No Reports",
                                modifier = Modifier.size(80.dp),
                                alpha = 0.5f
                            )
                            Text(
                                text = "No Reports To Show",
                                fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                                fontSize = 18.sp,
                                color = Color.Black.copy(alpha = 0.5f)
                            )
                            Text(
                                text = "Start reporting civic issues to track them here",
                                fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        // Summary Card
                        item {
                            ReportSummaryCard(
                                totalReports = uiState.reports.size,
                                reportedCount = uiState.reports.count { it.status == "REPORTED" },
                                resolvedCount = uiState.reports.count { it.status == "RESOLVED" }
                            )
                        }

                        // Report Cards
                        items(uiState.reports) { report ->
                            ReportCard(report = report)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SkeletonSummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(3) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp, 24.dp)
                            .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .size(60.dp, 14.dp)
                            .background(Color(0xFFE0E0E0), RoundedCornerShape(6.dp))
                    )
                }
                if (it < 2) {
                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .height(50.dp),
                        color = Color(0xFFEEEEEE)
                    )
                }
            }
        }
    }
}

@Composable
fun SkeletonReportCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Skeleton Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFE0E0E0))
            )

            // Skeleton Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(20.dp)
                            .background(Color(0xFFE0E0E0), RoundedCornerShape(6.dp))
                    )
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(24.dp)
                            .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .background(Color(0xFFE0E0E0), RoundedCornerShape(6.dp))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(14.dp)
                        .background(Color(0xFFE0E0E0), RoundedCornerShape(6.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .background(Color(0xFFE0E0E0), RoundedCornerShape(9.dp))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(14.dp)
                            .background(Color(0xFFE0E0E0), RoundedCornerShape(6.dp))
                    )
                }
            }
        }
    }
}

@Composable
fun ReportSummaryCard(
    totalReports: Int,
    reportedCount: Int,
    resolvedCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryItem(
                label = "Total",
                count = totalReports,
                color = Color(0xFF54B39C)
            )

            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(50.dp),
                color = Color(0xFFE0E0E0)
            )

            SummaryItem(
                label = "Pending",
                count = reportedCount,
                color = Color(0xFFFFB84E)
            )

            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(50.dp),
                color = Color(0xFFE0E0E0)
            )

            SummaryItem(
                label = "Resolved",
                count = resolvedCount,
                color = Color(0xFF4CAF50)
            )
        }
    }
}

@Composable
fun SummaryItem(
    label: String,
    count: Int,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = count.toString(),
            fontFamily = FontFamily(Font(R.font.poppins_bold)),
            fontSize = 26.sp,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ReportCard(report: ReportData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image Section
            if (report.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = report.imageUrl,
                    contentDescription = "Report Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.camera_icon),
                    error = painterResource(id = R.drawable.camera_icon)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera_icon),
                        contentDescription = "No Image",
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFFBDBDBD)
                    )
                }
            }

            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Issue Type & Status Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = report.issueType,
                        fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                        fontSize = 18.sp,
                        color = Color(0xFF212121),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )

                    StatusBadge(status = report.status)
                }

                // Description
                if (report.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = report.description,
                        fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                        fontSize = 14.sp,
                        color = Color(0xFF616161),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Location
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = Color(0xFF54B39C),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = report.location,
                        fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                        fontSize = 13.sp,
                        color = Color(0xFF757575),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Date
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        tint = Color(0xFF54B39C),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = formatDate(report.createdAt),
                        fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                        fontSize = 13.sp,
                        color = Color(0xFF757575)
                    )
                }

                // Coins Reward (if resolved)
                if (report.status == "RESOLVED" && report.civicCoinReward > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFFFFF8E1),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.coin),
                            contentDescription = "Coin",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Earned ${report.civicCoinReward} Civic Coins",
                            fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                            fontSize = 15.sp,
                            color = Color(0xFFF57C00),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (backgroundColor, textColor, displayText) = when (status) {
        "REPORTED" -> Triple(Color(0xFFFFE0B2), Color(0xFFE65100), "Reported")
        "ACKNOWLEDGED" -> Triple(Color(0xFFB3E5FC), Color(0xFF01579B), "Acknowledged")
        "IN_PROGRESS" -> Triple(Color(0xFFFFF9C4), Color(0xFFF57F17), "In Progress")
        "RESOLVED" -> Triple(Color(0xFFC8E6C9), Color(0xFF1B5E20), "Resolved")
        else -> Triple(Color(0xFFEEEEEE), Color(0xFF616161), status)
    }

    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = displayText,
            fontFamily = FontFamily(Font(R.font.poppins_semibold)),
            fontSize = 12.sp,
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}