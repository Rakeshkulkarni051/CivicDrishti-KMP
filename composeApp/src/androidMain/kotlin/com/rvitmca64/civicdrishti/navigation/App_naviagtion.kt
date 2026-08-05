package com.rvitmca64.civicdrishti.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rvitmca64.civicdrishti.MainScreen
import kotlinx.coroutines.delay
import com.rvitmca64.civicdrishti.ui.screens.SplashScreen
import com.rvitmca64.civicdrishti.ui.screens.auth.WelcomeScreen
import com.rvitmca64.civicdrishti.ui.screens.auth.AadharAuthScreen
import com.rvitmca64.civicdrishti.ui.screens.home.HomeMainScreen
import com.rvitmca64.civicdrishti.ui.screens.report.ReportIssueScreen
import com.rvitmca64.civicdrishti.ui.screens.report.SuccessScreen
import com.rvitmca64.civicdrishti.ui.viewmodels.AuthUiState
import com.rvitmca64.civicdrishti.ui.viewmodels.AuthViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val authState by authViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // --- Splash Screen ---
        composable("splash") {
            SplashScreen()

            LaunchedEffect(Unit) {
                delay(5000)

                var attempts = 0
                while (authState is AuthUiState.Idle && attempts < 10) {
                    delay(300)
                    attempts++
                }

                when (authState) {
                    is AuthUiState.Success -> {
                        navController.navigate("main_activity") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                    else -> {
                        navController.navigate("welcome") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            }
        }

        // --- Welcome Screen ---
        composable("welcome") {
            WelcomeScreen(
                onCivilianSelected = {
                    navController.navigate("aadhar_auth")
                }
            )
        }

        // --- Aadhaar Authentication Screen ---
        composable("aadhar_auth") {
            AadharAuthScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        // --- Main Activity (Bottom Nav) ---
        composable("main_activity") {
            MainScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("welcome") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                authViewModel = authViewModel // ✅ PASS AUTHVIEWMODEL
            )
        }

        // --- Home Screen ---
        composable("home_main") {
            HomeMainScreen(navController = navController)
        }

        // --- Report Issue Screen ---
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

        // --- Success Screen ---
        composable("success/{reportId}") { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
            SuccessScreen(
                reportId = reportId,
                onNavigateHome = {
                    navController.navigate("main_activity") {
                        popUpTo("main_activity") { inclusive = true }
                    }
                }
            )
        }
    }
}