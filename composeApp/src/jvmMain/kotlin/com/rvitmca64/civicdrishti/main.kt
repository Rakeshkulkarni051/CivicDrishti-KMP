package com.rvitmca64.civicdrishti

import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.rvitmca64.civicdrishti.ui.splash.SplashScreen
import com.rvitmca64.civicdrishti.ui.auth.LoginScreen
import com.rvitmca64.civicdrishti.ui.dashboard.DashboardRoot
import org.jetbrains.compose.resources.painterResource

/**
 * Main entry point for Civic Drishti Desktop Application
 *
 * Navigation Flow:
 * 1. SPLASH → Shows for 3 seconds with fade-in animation
 * 2. LOGIN → User authentication screen
 * 3. DASHBOARD → Main application (to be implemented)
 */
fun main() = application {
    // Navigation state - controls which screen is displayed
    var currentScreen by remember { mutableStateOf(Screen.SPLASH) }

    // Window configuration
    val windowState = rememberWindowState()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Civic Drishti - GOV Dashboard",
        state = windowState,
        icon = painterResource("images/logo_transperent.png")

    ) {
        // Screen router - displays current screen based on navigation state
        when (currentScreen) {
            Screen.SPLASH -> {
                SplashScreen(
                    onNavigateToLogin = {
                        currentScreen = Screen.LOGIN
                    }
                )
            }

            Screen.LOGIN -> {
                LoginScreen(
                    onLoginSuccess = {
                        currentScreen = Screen.DASHBOARD
                    }
                )
            }

            Screen.DASHBOARD -> {
                // TODO: Implement DashboardRoot
                DashboardRoot()
            }
        }
    }
}

/**
 * Screen navigation enum
 * Defines all possible screens in the application
 */
enum class Screen {
    SPLASH,
    LOGIN,
    DASHBOARD
}