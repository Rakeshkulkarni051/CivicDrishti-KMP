package com.rvitmca64.civicdrishti.data.model

data class LeaderboardData(
    val uid: String = "",
    val name: String = "",
    val totalReports: Int = 0,
    val civicCoins: Int = 0,
    val impactScore: Int = 0,
    val trustScore: Double = 0.0,
    val rank: Int = 0,
    val location: String = "Bangalore"
)
