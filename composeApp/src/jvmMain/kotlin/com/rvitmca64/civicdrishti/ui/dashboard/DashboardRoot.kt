package com.rvitmca64.civicdrishti.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DashboardRoot() {

    var selectedTab by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // ================= CONTENT =================
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 120.dp)
        ) {
            when (selectedTab) {
                0 -> HomeScreen()
                1 -> HeatMapScreen()
                2 -> RoutingScreen()
            }
        }

        // ================= BOTTOM NAV =================
        BottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )
    }
}

/* -------------------------------------------------------------------------- */
/*                                BOTTOM NAV                                  */
/* -------------------------------------------------------------------------- */

@Composable
private fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val navItems = listOf(
        NavItem(0, "Home", "images/reports_dash.png", 62.dp, 72.dp),
        NavItem(1, "Heat Map", "images/heat_map.png", 91.dp, 68.dp),
        NavItem(2, "Routing", "images/route_icn.png", 85.dp, 53.dp)
    )

    Box(
        modifier = modifier
            .width(600.dp)
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource("images/nav_card.png"),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                NavItemView(
                    navItem = item,
                    selected = selectedTab == item.index,
                    onClick = { onTabSelected(item.index) }
                )
            }
        }
    }
}

@Composable
private fun NavItemView(
    navItem: NavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val alpha = if (selected) 1f else 0.5f

    Box(
        modifier = Modifier
            .size(100.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(navItem.iconPath),
            contentDescription = navItem.label,
            modifier = Modifier
                .width(navItem.iconWidth)
                .height(navItem.iconHeight)
                .graphicsLayer { this.alpha = alpha },
            contentScale = ContentScale.Fit
        )
    }
}

/* -------------------------------------------------------------------------- */
/*                                  MODEL                                     */
/* -------------------------------------------------------------------------- */

private data class NavItem(
    val index: Int,
    val label: String,
    val iconPath: String,
    val iconWidth: Dp,
    val iconHeight: Dp
)
