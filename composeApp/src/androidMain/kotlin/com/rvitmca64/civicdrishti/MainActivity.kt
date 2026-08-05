package com.rvitmca64.civicdrishti

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.rvitmca64.civicdrishti.navigation.AppNavigation
import com.rvitmca64.civicdrishti.ui.navigation.BottomNavItem
import com.rvitmca64.civicdrishti.ui.screens.board.Leader_Board
import com.rvitmca64.civicdrishti.ui.screens.home.HomeMainScreen
import com.rvitmca64.civicdrishti.ui.screens.issues.IssuesReported
import com.rvitmca64.civicdrishti.ui.screens.report.ReportIssueScreen
import com.rvitmca64.civicdrishti.ui.screens.report.SuccessScreen
import com.rvitmca64.civicdrishti.ui.screens.user.User_profile
import com.rvitmca64.civicdrishti.ui.theme.CivicDrishtiTheme
import com.rvitmca64.civicdrishti.ui.viewmodels.AuthViewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        initFirebase()

        val db = FirebaseFirestore.getInstance()
        db.collection("test").get()
            .addOnSuccessListener {
                Log.d("FirebaseTest", "Firestore is working!")
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseTest", "Firestore FAILED", e)
            }

        setContent {
            CivicDrishtiTheme {
                AppNavigation(authViewModel = authViewModel)
            }
        }
    }

    private fun initFirebase() {
        val existingApps = FirebaseApp.getApps(this)
        if (existingApps.isNotEmpty()) return

        val options = FirebaseOptions.Builder()
            .setApplicationId("1:291064008952:android:eeb0e25af563ac11c9ab0d")
            .setApiKey("AIzaSyCH0Xy0iWOCsihg7UYhxnQzZYd1nOp0q7A")
            .setProjectId("civic-drishti")
            .setStorageBucket("civic-drishti.firebasestorage.app")
            .build()

        FirebaseApp.initializeApp(this, options)
    }
}

@Composable
fun MainScreen(
    onLogout: () -> Unit = {}, // ✅ ADD LOGOUT CALLBACK
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Reports,
        BottomNavItem.Board,
        BottomNavItem.Profile
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            BottomNavigationBar(items, navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeMainScreen(navController = navController, authViewModel = authViewModel)
            }
            composable(BottomNavItem.Reports.route) {
                IssuesReported()
            }
            composable(BottomNavItem.Board.route) {
                Leader_Board()
            }
            composable(BottomNavItem.Profile.route) {
                // ✅ PASS LOGOUT CALLBACK
                User_profile(onLogout = onLogout)
            }

            composable("ReportIssueScreen") {
                ReportIssueScreen(
                    authViewModel = authViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToSuccess = { reportId ->
                        navController.navigate("success/$reportId") {
                            popUpTo("ReportIssueScreen") { inclusive = true }
                        }
                    }
                )
            }

            composable("success/{reportId}") { backStackEntry ->
                val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
                SuccessScreen(
                    reportId = reportId,
                    onNavigateHome = {
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo(BottomNavItem.Home.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: androidx.navigation.NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = Color(0xFFD9D9D9),
        tonalElevation = 0.dp,
        modifier = Modifier
            .height(100.dp)
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 4.dp))
    ) {
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            val interactionSource = remember { MutableInteractionSource() }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .indication(
                        interactionSource,
                        LocalIndication.current
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    .padding(vertical = 2.dp)
            ) {

                if (selected) {
                    Box(
                        modifier = Modifier
                            .width(58.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF0A6E53))
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                } else {
                    Spacer(modifier = Modifier.height(9.dp))
                }

                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = item.label,
                    modifier = Modifier.size(if (selected) 34.dp else 28.dp),
                    tint = Color(0xFF0A6E53)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = item.label.uppercase(),
                    fontSize = if (selected) 11.sp else 9.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    fontFamily = FontFamily(Font(R.font.inter_18pt_medium)),
                    color = Color(0xFF000000)
                )
            }
        }
    }
}