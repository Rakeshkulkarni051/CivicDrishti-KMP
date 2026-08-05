package com.rvitmca64.civicdrishti.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

// ========================================
// COLOR CONSTANTS
// ========================================
private val PrimaryDarkGreen = Color(0xFF20605F)
private val AccentMintGreen = Color(0xFF54B39C)
private val LightGray = Color(0xFFE6E6E6)
private val TextGray = Color(0xFF666666)
private val StatusRed = Color(0xFFE74C3C)
private val StatusAmber = Color(0xFFF39C12)
private val BackgroundWhite = Color.White

// ========================================
// DATA MODEL
// ========================================
data class Report(
    val reportId: String = "",
    val description: String = "",
    val detectedIssue: String = "",
    val issueType: String = "",
    val location: String = "",
    val reportedBy: String = "",
    val status: String = "",
    val priority: Int = 0,
    val createdAt: Long = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val imageUrl: String = "",
    val userId: String = ""
)

// ========================================
// IMAGE LOADER
// ========================================
object ImageLoader {
    private val imageCache = mutableMapOf<String, ImageBitmap>()

    suspend fun loadImage(url: String): ImageBitmap? = withContext(Dispatchers.IO) {
        if (url.isEmpty()) return@withContext null

        imageCache[url]?.let { return@withContext it }

        try {
            println("📥 Loading image: ${url.take(80)}...")
            val imageUrl = URL(url)
            val connection = imageUrl.openConnection() as HttpURLConnection
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val imageBytes = connection.inputStream.readBytes()
            val imageBitmap = org.jetbrains.skia.Image.makeFromEncoded(imageBytes).toComposeImageBitmap()

            imageCache[url] = imageBitmap
            println("✅ Image loaded successfully")
            imageBitmap
        } catch (e: Exception) {
            println("❌ Failed to load image: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}

// ========================================
// FIRESTORE REST API CLIENT
// ========================================
object FirestoreClient {
    private const val PROJECT_ID = "civic-drishti"
    private const val API_KEY = "AIzaSyCH0Xy0iWOCsihg7UYhxnQzZYd1nOp0q7A"
    private const val BASE_URL =
        "https://firestore.googleapis.com/v1/projects/$PROJECT_ID/databases/(default)/documents"

    suspend fun fetchReports(): List<Report> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL/reports?key=$API_KEY")
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.connectTimeout = 15000
            connection.readTimeout = 15000

            val responseCode = connection.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = reader.use { it.readText() }

                println("✅ Firestore Response received (${response.length} chars)")
                parseReports(response)
            } else {
                println("❌ HTTP Error: $responseCode")
                emptyList()
            }
        } catch (e: Exception) {
            println("❌ Fetch error: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    private fun parseReports(jsonResponse: String): List<Report> {
        val reports = mutableListOf<Report>()

        try {
            val docPattern =
                "\"name\":\\s*\"projects/civic-drishti/databases/\\(default\\)/documents/reports/([^\"]+)\"".toRegex()
            val matches = docPattern.findAll(jsonResponse)

            matches.forEach { match ->
                try {
                    val reportId = match.groupValues[1]
                    val startIndex = match.range.first

                    val nextMatch = docPattern.find(jsonResponse, startIndex + 1)
                    val endIndex = nextMatch?.range?.first ?: jsonResponse.lastIndexOf("}")

                    val docJson = jsonResponse.substring(startIndex, endIndex)

                    if (reportId == "reportId") {
                        println("⏭️ Skipping test document")
                        return@forEach
                    }

                    val report = Report(
                        reportId = reportId,
                        description = extractStringValue(docJson, "description"),
                        detectedIssue = extractStringValue(docJson, "detectedIssue"),
                        issueType = extractStringValue(docJson, "issueType"),
                        location = extractStringValue(docJson, "location"),
                        reportedBy = extractStringValue(docJson, "reportedBy").ifEmpty { "Anonymous" },
                        status = extractStringValue(docJson, "status").ifEmpty { "REPORTED" },
                        priority = extractIntValue(docJson, "priority"),
                        createdAt = extractIntValue(docJson, "createdAt").toLong(),
                        latitude = extractDoubleValue(docJson, "latitude"),
                        longitude = extractDoubleValue(docJson, "longitude"),
                        imageUrl = extractStringValue(docJson, "imageUrl"),
                        userId = extractStringValue(docJson, "userId")
                    )

                    reports.add(report)
                    println("✅ Parsed report: $reportId - ${report.reportedBy}")
                } catch (e: Exception) {
                    println("❌ Error parsing document: ${e.message}")
                }
            }

            println("✅ Successfully parsed ${reports.size} reports")
        } catch (e: Exception) {
            println("❌ Parse error: ${e.message}")
            e.printStackTrace()
        }

        return reports
    }

    private fun extractStringValue(json: String, fieldName: String): String {
        val pattern = "\"$fieldName\":\\s*\\{\\s*\"stringValue\":\\s*\"([^\"]*)\"".toRegex()
        val match = pattern.find(json)
        return match?.groupValues?.get(1) ?: ""
    }

    private fun extractIntValue(json: String, fieldName: String): Int {
        val pattern = "\"$fieldName\":\\s*\\{\\s*\"integerValue\":\\s*\"([^\"]*)\"".toRegex()
        val match = pattern.find(json)
        return match?.groupValues?.get(1)?.toIntOrNull() ?: 0
    }

    private fun extractDoubleValue(json: String, fieldName: String): Double {
        val pattern = "\"$fieldName\":\\s*\\{\\s*\"doubleValue\":\\s*([\\d.]+)".toRegex()
        val match = pattern.find(json)
        return match?.groupValues?.get(1)?.toDoubleOrNull() ?: 0.0
    }

    suspend fun getStatusCounts(reports: List<Report>): Map<String, Int> {
        val counts = mutableMapOf(
            "REPORTED" to 0,
            "ACKNOWLEDGED" to 0,
            "ROUTED" to 0,
            "RESOLVED" to 0
        )

        reports.forEach { report ->
            counts[report.status] = (counts[report.status] ?: 0) + 1
        }

        return counts
    }
}

// ========================================
// MAIN HOME SCREEN
// ========================================
@Composable
fun HomeScreen() {
    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }
    var statusCounts by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var toastMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun refreshData() {
        scope.launch {
            try {
                val fetchedReports = FirestoreClient.fetchReports()
                val fetchedCounts = FirestoreClient.getStatusCounts(fetchedReports)

                reports = fetchedReports
                statusCounts = fetchedCounts
                isLoading = false
            } catch (e: Exception) {
                errorMessage = "Failed to load data: ${e.message}"
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        println("🔄 Fetching reports from Firestore REST API...")
        refreshData()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = PrimaryDarkGreen)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading reports...", color = TextGray)
                    }
                }
            }

            errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "⚠️ Error",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusRed
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage ?: "Unknown error",
                            color = TextGray,
                            modifier = Modifier.padding(16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                isLoading = true
                                errorMessage = null
                                refreshData()
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryDarkGreen)
                        ) {
                            Text("Retry", color = Color.White)
                        }
                    }
                }
            }

            else -> {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackgroundWhite)
                        .padding(32.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            HeaderSection()
                            Spacer(modifier = Modifier.width(32.dp))
                            RegionStatsCard(statusCounts)
                        }

                        Spacer(modifier = Modifier.height(40.dp))
                        LiveReportsSection(
                            reports = reports,
                            onAcknowledge = { report ->
                                toastMessage = "Acknowledged: ${report.reportId}"
                            },
                            onRoute = { report ->
                                if (!RoutingState.routedReports.any { it.reportId == report.reportId }) {
                                    RoutingState.routedReports.add(report)
                                    toastMessage = "Report routed: ${report.reportId}"
                                } else {
                                    toastMessage = "Already routed: ${report.reportId}"
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(32.dp))
                    SidePanel()
                }
            }
        }

        toastMessage?.let { message ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.85f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = message,
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }
            }

            LaunchedEffect(message) {
                delay(2000)
                toastMessage = null
            }
        }
    }
}

// ========================================
// HEADER SECTION
// ========================================
@Composable
private fun HeaderSection() {
    Column {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color(0xFF20605F),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append("Civic ")
                }
                withStyle(
                    SpanStyle(
                        color = Color(0xFF54B39C),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append("Drishti")
                }
            },
            letterSpacing = (-1).sp
        )

        Text(
            text = "Dashboard",
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50),
            letterSpacing = (-2).sp
        )
    }
}

// ========================================
// REGION & STATS CARD
// ========================================
@Composable
private fun RegionStatsCard(statusCounts: Map<String, Int>) {
    Box(
        modifier = Modifier
            .width(480.dp)
            .border(2.dp, LightGray, RoundedCornerShape(16.dp))
            .background(BackgroundWhite, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = PrimaryDarkGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Bengaluru South Region",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2C3E50)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusMetric("PENDING", (statusCounts["REPORTED"] ?: 0).toString(), StatusRed)
                StatusMetric("ACKNOWLEDGED", (statusCounts["ACKNOWLEDGED"] ?: 0).toString(), StatusAmber)
                StatusMetric("ROUTED", (statusCounts["ROUTED"] ?: 0).toString(), AccentMintGreen)
                StatusMetric("RESOLVED", (statusCounts["RESOLVED"] ?: 0).toString(), PrimaryDarkGreen)
            }
        }
    }
}

@Composable
private fun StatusMetric(label: String, count: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = TextGray,
            letterSpacing = 0.8.sp
        )
    }
}

// ========================================
// LIVE REPORTS SECTION
// ========================================
@Composable
private fun LiveReportsSection(
    reports: List<Report>,
    onAcknowledge: (Report) -> Unit,
    onRoute: (Report) -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .border(1.dp, Color(0xFFBDC3C7), RoundedCornerShape(4.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "LIVE REPORTS (${reports.size})",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextGray,
                letterSpacing = 1.5.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (reports.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No reports available", fontSize = 16.sp, color = TextGray)
            }
        } else {
            reports.forEach { report ->
                ReportCard(
                    report = report,
                    onAcknowledge = { onAcknowledge(report) },
                    onRoute = { onRoute(report) }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

// ========================================
// REPORT CARD WITH IMAGE
// ========================================
@Composable
private fun ReportCard(
    report: Report,
    onAcknowledge: () -> Unit,
    onRoute: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm 'IST'", Locale.getDefault())
    val timestamp = if (report.createdAt > 0) {
        dateFormat.format(Date(report.createdAt))
    } else "Unknown time"

    val priorityLabel = when (report.priority) {
        2 -> "HIGH"
        1 -> "MEDIUM"
        else -> "LOW"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGray, RoundedCornerShape(12.dp))
            .padding(24.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = report.reportId,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryDarkGreen
                )

                Spacer(modifier = Modifier.height(8.dp))

                val priorityColor = when (report.priority) {
                    2 -> StatusRed
                    1 -> StatusAmber
                    else -> AccentMintGreen
                }

                Box(
                    modifier = Modifier
                        .background(priorityColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = priorityLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = priorityColor,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                InfoRow("Reported By:", report.reportedBy)
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow("Issue Type:", report.issueType)
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Description:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF34495E)
                )
                Text(
                    text = report.description.ifEmpty { "No description" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF2C3E50),
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Location:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF34495E)
                )
                Text(
                    text = report.location.ifEmpty { "Location unavailable" },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF2C3E50),
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
                InfoRow("Reported At:", timestamp)
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow("Status:", report.status)
            }

            Spacer(modifier = Modifier.width(24.dp))

            PlaceholderBox(
                label = "Location\nLat: ${"%.4f".format(report.latitude)}\nLon: ${"%.4f".format(report.longitude)}",
                width = 220.dp,
                height = 180.dp
            )

            Spacer(modifier = Modifier.width(20.dp))

            ImageBox(
                imageUrl = report.imageUrl,
                width = 200.dp,
                height = 180.dp
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionButton("ACKNOWLEDGE", StatusAmber, onClick = onAcknowledge)
            ActionButton("ROUTE", AccentMintGreen, onClick = onRoute)
        }
    }
}

// ========================================
// IMAGE BOX COMPOSABLE
// ========================================
@Composable
private fun ImageBox(
    imageUrl: String,
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp
) {
    var image by remember(imageUrl) { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember(imageUrl) { mutableStateOf(false) }
    var hasError by remember(imageUrl) { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(imageUrl) {
        if (imageUrl.isNotEmpty()) {
            isLoading = true
            hasError = false
            scope.launch {
                val loadedImage = ImageLoader.loadImage(imageUrl)
                image = loadedImage
                isLoading = false
                if (loadedImage == null) {
                    hasError = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(Color(0xFFD5D8DC), RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFBDC3C7), RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = PrimaryDarkGreen,
                        modifier = Modifier.size(32.dp),
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Loading...",
                        fontSize = 11.sp,
                        color = TextGray
                    )
                }
            }

            hasError || imageUrl.isEmpty() -> {
                Text(
                    text = if (imageUrl.isEmpty()) "No Image" else "Failed to load",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextGray
                )
            }

            image != null -> {
                Image(
                    bitmap = image!!,
                    contentDescription = "Issue Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row {
        Text(
            label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF34495E)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF2C3E50)
        )
    }
}

@Composable
private fun PlaceholderBox(
    label: String,
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(Color(0xFFD5D8DC), RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFBDC3C7), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextGray)
    }
}

@Composable
private fun ActionButton(text: String, backgroundColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp),
        elevation = ButtonDefaults.elevation(0.dp)
    ) {
        Text(
            text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 0.5.sp
        )
    }
}

// ========================================
// SIDE PANEL
// ========================================
@Composable
private fun SidePanel() {
    Column(
        modifier = Modifier
            .width(280.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TrustScoreCard()
        FlaggedDelaysCard()
    }
}

@Composable
private fun TrustScoreCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGray, RoundedCornerShape(12.dp))
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .border(12.dp, PrimaryDarkGreen, CircleShape)
                    .clip(CircleShape)
                    .background(BackgroundWhite),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "89.09%",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDarkGreen
                    )
                    Text(
                        "TRUST SCORE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextGray,
                        letterSpacing = 1.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Dept Trust", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextGray)
        }
    }
}

@Composable
private fun FlaggedDelaysCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGray, RoundedCornerShape(12.dp))
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Flagged Delays",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2C3E50),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                BarItem("Mon", 0.6f)
                BarItem("Tue", 0.8f)
                BarItem("Wed", 0.4f)
                BarItem("Thu", 0.9f)
                BarItem("Fri", 0.5f)
            }
        }
    }
}

@Composable
private fun BarItem(day: String, progress: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            day,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextGray,
            modifier = Modifier.width(40.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(20.dp)
                .background(Color(0xFFD5D8DC), RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(StatusRed, RoundedCornerShape(4.dp))
            )
        }
    }
}