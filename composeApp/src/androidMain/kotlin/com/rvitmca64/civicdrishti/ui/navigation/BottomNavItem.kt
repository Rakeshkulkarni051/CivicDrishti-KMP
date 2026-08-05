package com.rvitmca64.civicdrishti.ui.navigation

import com.rvitmca64.civicdrishti.R

sealed class BottomNavItem(val route: String, val icon: Int, val label: String) {
    object Home : BottomNavItem("home_main", R.drawable.home, "Home")
    object Reports : BottomNavItem("reports", R.drawable.reported, "Reports")
    object Board : BottomNavItem("leader_board", R.drawable.leaderboard, "Board")
    object Profile : BottomNavItem("profile", R.drawable.user, "User")

}
