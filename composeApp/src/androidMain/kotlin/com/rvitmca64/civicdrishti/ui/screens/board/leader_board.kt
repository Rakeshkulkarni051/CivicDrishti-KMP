package com.rvitmca64.civicdrishti.ui.screens.board

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rvitmca64.civicdrishti.R

@Composable
fun Leader_Board() {
    LeaderboardScreen(
        userRank = 15,
        totalCitizens = 200
    )
}

@Composable
fun LeaderboardScreen(
    userRank: Int = 15,
    totalCitizens: Int = 200
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Top Abstract Section with Text Overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        ) {
            // Background Abstract Image
            Image(
                painter = painterResource(id = R.drawable.leaderboard_abstr),
                contentDescription = "Leaderboard Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(20.dp))
            )

            // Content Overlay
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 14.dp, end = 24.dp, top = 45.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                // Main Title
                Text(
                    text = "Leaderboard",
                    fontFamily = FontFamily(Font(R.font.manrope_bold)),
                    fontSize = 28.sp,
                    color = Color.White.copy(alpha = 0.75f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle
                Text(
                    text = "Recognizing Citizens Who Make Cities Better",
                    fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.5f),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Dotted Box with Rank Info
                DottedBox(
                    userRank = userRank,
                    totalCitizens = totalCitizens
                )
            }
        }

        // Citizen Impact Board Section
      //  Spacer(modifier = Modifier.height(4.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y= (-24).dp)
        ) {
            // Section Title
            Text(
                text = "Citizen Impact Board",
                fontFamily = FontFamily(Font(R.font.manrope_bold)),
                fontSize = 20.sp,
                color = Color(0xFF726A6A)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Two Impact Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Active Reporter Card
                CitizenImpactCard(
                    avatarRes = R.drawable.women_avatar,
                    name = "Nisha Sharma",
                    metric = "114 Reports",
                    badge = "Active Reporter",
                    modifier = Modifier.weight(1f)
                )

                // City Hero Card
                CitizenImpactCard(
                    avatarRes = R.drawable.men_avatar,
                    name = "Anil Kumar",
                    metric = "543k Impact Score",
                    badge = "City Hero",
                    modifier = Modifier.weight(1f)
                )
            }
        }

       // Spacer(modifier = Modifier.height(4.dp))

        // Top Contributors Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-8).dp)
        ) {
            // Section Title
            Text(
                text = "Top Contributors",
                fontFamily = FontFamily(Font(R.font.manrope_bold)),
                fontSize = 20.sp,
                color = Color(0xFF726A6A)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Contributors Table
            TopContributorsTable(
                contributors = listOf(
                    Contributor("#001", "AMAR\nPATEL", 2350, "2,45,000"),
                    Contributor("#002", "KARAN\nSAHA", 2016, "1,72,230"),
                    Contributor("#003", "ANITA\nKUMARI", 1822, "1,03,110"),
                 //   Contributor("#004", "RAHUL\nROY", 1450, "97,540")
                )
            )
        }
    }
}

data class Contributor(
    val rank: String,
    val name: String,
    val civicCoins: Int,
    val impactScore: String
)

@Composable
fun DottedBox(
    userRank: Int,
    totalCitizens: Int
) {
    Box(
        modifier = Modifier
            .width(172.dp)
            .height(84.dp)
    ) {
        // Background dotted box image
        Image(
            painter = painterResource(id = R.drawable.doted_box),
            contentDescription = "Dotted Box",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

        // Text content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    append("You Ranked ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("#$userRank")
                    }
                    append(" Out Of ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$totalCitizens")
                    }
                    append(" Citizens In Your Area")
                },
                fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                fontSize = 14.sp,
                color = Color.White,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun CitizenImpactCard(
    avatarRes: Int,
    name: String,
    metric: String,
    badge: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(170.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        // Card Background (positioned lower to allow avatar overlap)
        Image(
            painter = painterResource(id = R.drawable.impact_card),
            contentDescription = "Impact Card",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(158.dp)
                .align(Alignment.BottomCenter)
        )

        // Avatar (overlapping on top)
        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = "Citizen Avatar",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(132.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-18).dp)
        )

        // Text Content (name and metric)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = name,
                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                fontSize = 14.sp,
                color = Color(0xFF20605F), lineHeight = 6.sp
            )

            Text(
                text = metric,
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                fontSize = 12.sp,
                color = Color(0xFF20605F),  lineHeight = 4.sp
            )
        }

        Text(
            text = badge,
            fontFamily = FontFamily(Font(R.font.manrope_bold)),
            fontSize = 14.sp,
            color = Color(0xFFFFAE34),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 0.dp)
        )
    }
}
@Composable
fun TopContributorsTable(
    contributors: List<Contributor>
) {
    Box(
        modifier = Modifier
            .width(401.dp)
            .height(241.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFD9D9D9))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RANK",
                    fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier.width(50.dp)
                )
                Text(
                    text = "NAME",
                    fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier.width(80.dp)
                )
                Text(
                    text = "CIVIC COINS",
                    fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier.width(100.dp)
                )
                Text(
                    text = "IMPACT SCORE",
                    fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f)
                )
            }

            // Dotted Divider
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            ) {
                drawLine(
                    color = Color.Gray,
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                        floatArrayOf(10f, 10f), 0f
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Contributors List
            contributors.forEach { contributor ->
                ContributorRow(contributor)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ContributorRow(contributor: Contributor) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank
        Text(
            text = contributor.rank,
            fontFamily = FontFamily(Font(R.font.manrope_medium)),
            fontSize = 12.sp,
            color = Color.Black,
            modifier = Modifier.width(50.dp)
        )

        // Name
        Text(
            text = contributor.name,
            fontFamily = FontFamily(Font(R.font.manrope_medium)),
            fontSize = 12.sp,
            color = Color.Black,
            lineHeight = 14.sp,
            modifier = Modifier.width(80.dp)
        )

        // Civic Coins (with icon)
        Row(
            modifier = Modifier.width(70.dp).wrapContentWidth(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.coin),
                contentDescription = "Civic Coin",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = contributor.civicCoins.toString(),
                fontFamily = FontFamily(Font(R.font.manrope_medium)),
                fontSize = 12.sp,
                color = Color.Black
            )
        }

        // Impact Score
        Text(
            text = contributor.impactScore,
            fontFamily = FontFamily(Font(R.font.manrope_medium)),
            fontSize = 12.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}