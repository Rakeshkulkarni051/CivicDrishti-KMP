package com.rvitmca64.civicdrishti.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rvitmca64.civicdrishti.ui.theme.Manrope

data class DepartmentAssignment(
    val department: String,
    val officerName: String,
    val officerContact: String,
    val workStatus: String,
    val progress: Float
)

@Composable
fun RoutingScreen() {
    val routedReports = RoutingState.routedReports

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7F7))
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Routing Queue",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Manrope,
            color = Color(0xFF20605F)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Department assignment, officer mapping and work progress tracking",
            fontSize = 17.sp,
            fontFamily = Manrope,
            color = Color(0xFF666666)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (routedReports.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFE3E7E7), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No routed reports yet",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    fontFamily = Manrope
                )
            }
        } else {
            routedReports.forEach { report ->
                RoutedReportCard(
                    report = report,
                    assignment = getDepartmentAssignment(report)
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}

@Composable
private fun RoutedReportCard(
    report: Report,
    assignment: DepartmentAssignment
) {
    val priorityColor = when (report.priority) {
        2 -> Color(0xFFE74C3C)
        1 -> Color(0xFFF39C12)
        else -> Color(0xFF54B39C)
    }

    val priorityLabel = when (report.priority) {
        2 -> "HIGH"
        1 -> "MEDIUM"
        else -> "LOW"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(18.dp))
            .border(1.dp, Color(0xFFE1E5E5), RoundedCornerShape(18.dp))
            .padding(22.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier.weight(1.2f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = report.reportId,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF20605F),
                    fontFamily = Manrope
                )

                Box(
                    modifier = Modifier
                        .background(priorityColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = priorityLabel,
                        color = priorityColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        fontFamily = Manrope
                    )
                }

                InfoText("Issue Type", report.issueType.ifEmpty { "Not specified" })
                InfoText("Reported By", report.reportedBy.ifEmpty { "Anonymous" })
                InfoText("Location", report.location.ifEmpty { "Unavailable" })
                InfoText("Description", report.description.ifEmpty { "No description available" })

                Text(
                    text = "Coordinates: ${"%.4f".format(report.latitude)}, ${"%.4f".format(report.longitude)}",
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280),
                    fontFamily = Manrope
                )
            }

            Column(
                modifier = Modifier
                    .weight(0.9f)
                    .background(Color(0xFFF8FBFB), RoundedCornerShape(14.dp))
                    .border(1.dp, Color(0xFFE2EBEA), RoundedCornerShape(14.dp))
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Assigned Department",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F3D3C),
                    fontFamily = Manrope
                )

                AssignmentRow(
                    iconType = "department",
                    label = "Department",
                    value = assignment.department
                )

                AssignmentRow(
                    iconType = "officer",
                    label = "Area Incharge",
                    value = assignment.officerName
                )

                AssignmentRow(
                    iconType = "contact",
                    label = "Contact",
                    value = assignment.officerContact
                )

                AssignmentRow(
                    iconType = "location",
                    label = "Assigned Zone",
                    value = extractZoneFromLocation(report.location)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Work Status",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F3D3C),
                    fontFamily = Manrope
                )

                StatusBadge(assignment.workStatus)

                ProgressSection(
                    progress = assignment.progress,
                    status = assignment.workStatus
                )
            }
        }
    }
}

@Composable
private fun InfoText(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF6B7280),
            fontWeight = FontWeight.SemiBold,
            fontFamily = Manrope
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color(0xFF2D3748),
            fontFamily = Manrope,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun AssignmentRow(
    iconType: String,
    label: String,
    value: String
) {
    val icon = when (iconType) {
        "department" -> Icons.Default.Business
        "officer" -> Icons.Default.AccountCircle
        "contact" -> Icons.Default.Call
        "location" -> Icons.Default.LocationOn
        else -> Icons.Default.Build
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF20605F),
            modifier = Modifier.size(18.dp)
        )

        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFF6B7280),
                fontWeight = FontWeight.SemiBold,
                fontFamily = Manrope
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color(0xFF2D3748),
                fontFamily = Manrope
            )
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val bgColor = when (status.uppercase()) {
        "COMPLETED" -> Color(0xFFDDF5E8)
        "IN PROGRESS" -> Color(0xFFFFF1D6)
        else -> Color(0xFFFFE1E1)
    }

    val textColor = when (status.uppercase()) {
        "COMPLETED" -> Color(0xFF1E8E5A)
        "IN PROGRESS" -> Color(0xFFCC8A00)
        else -> Color(0xFFD64545)
    }

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = status,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            fontFamily = Manrope
        )
    }
}

@Composable
private fun ProgressSection(
    progress: Float,
    status: String
) {
    val progressColor = when (status.uppercase()) {
        "COMPLETED" -> Color(0xFF1E8E5A)
        "IN PROGRESS" -> Color(0xFFF39C12)
        else -> Color(0xFFE74C3C)
    }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Completion Progress",
                fontSize = 12.sp,
                color = Color(0xFF6B7280),
                fontFamily = Manrope
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 12.sp,
                color = Color(0xFF2D3748),
                fontWeight = FontWeight.Bold,
                fontFamily = Manrope
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(Color(0xFFE4E8E8), RoundedCornerShape(10.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(progressColor, RoundedCornerShape(10.dp))
            )
        }
    }
}

private fun getDepartmentAssignment(report: Report): DepartmentAssignment {
    val issue = report.issueType.lowercase()
    val location = report.location.lowercase()

    return when {
        "pothole" in issue -> {
            when {
                "jp nagar" in location -> DepartmentAssignment(
                    department = "BBMP Road Maintenance",
                    officerName = "Ramesh Gowda",
                    officerContact = "+91 98765 12001",
                    workStatus = "IN PROGRESS",
                    progress = 0.65f
                )

                "jayanagar" in location -> DepartmentAssignment(
                    department = "BBMP Road Maintenance",
                    officerName = "Suresh Kumar",
                    officerContact = "+91 98765 12002",
                    workStatus = "PENDING",
                    progress = 0.25f
                )

                else -> DepartmentAssignment(
                    department = "BBMP Road Maintenance",
                    officerName = "Manjunath Rao",
                    officerContact = "+91 98765 12003",
                    workStatus = "IN PROGRESS",
                    progress = 0.50f
                )
            }
        }

        "garbage" in issue || "waste" in issue -> {
            DepartmentAssignment(
                department = "BBMP Solid Waste Management",
                officerName = "Shilpa Naik",
                officerContact = "+91 98765 22001",
                workStatus = "PENDING",
                progress = 0.30f
            )
        }

        "drainage" in issue || "sewage" in issue -> {
            DepartmentAssignment(
                department = "BWSSB",
                officerName = "Prakash Hegde",
                officerContact = "+91 98765 32001",
                workStatus = "IN PROGRESS",
                progress = 0.72f
            )
        }

        "streetlight" in issue || "light" in issue -> {
            DepartmentAssignment(
                department = "BESCOM",
                officerName = "Naveen Bhat",
                officerContact = "+91 98765 42001",
                workStatus = "COMPLETED",
                progress = 1.0f
            )
        }

        "water" in issue -> {
            DepartmentAssignment(
                department = "BWSSB",
                officerName = "Anita Shetty",
                officerContact = "+91 98765 52001",
                workStatus = "IN PROGRESS",
                progress = 0.80f
            )
        }

        else -> {
            DepartmentAssignment(
                department = "BBMP General Civic Cell",
                officerName = "Kiran R",
                officerContact = "+91 98765 62001",
                workStatus = "PENDING",
                progress = 0.20f
            )
        }
    }
}

private fun extractZoneFromLocation(location: String): String {
    val lower = location.lowercase()

    return when {
        "jp nagar" in lower -> "JP Nagar Zone"
        "jayanagar" in lower -> "Jayanagar Zone"
        "banashankari" in lower -> "Banashankari Zone"
        "btm" in lower -> "BTM Layout Zone"
        "electronic city" in lower -> "Electronic City Zone"
        else -> "Bengaluru South Zone"
    }
}