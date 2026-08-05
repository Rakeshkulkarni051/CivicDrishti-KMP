package com.rvitmca64.civicdrishti.data.model

data class UserData(
    val userId: String = "",
    val name: String = "",
    val aadhar_hash: String = "",
    val city: String = "Bangalore",
    val locationLat: Double = 0.0,
    val locationLng: Double = 0.0,
    val badges: List<String> = listOf("Verified", "Urban Vanguard", "City Protector"),
    val civic_coin: Int = 0,
    val impact_score: Int = 0,
    val total_reports: Int = 0,
    val trust_score: Double = 0.5,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis()
) {
    // Convert to Firestore map
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "name" to name,
            "aadhar_hash" to aadhar_hash,
            "city" to city,
            "location" to mapOf(
                "lat" to locationLat,
                "lng" to locationLng
            ),
            "badges" to badges,
            "civic_coin" to civic_coin,
            "impact_score" to impact_score,
            "total_reports" to total_reports,
            "trust_score" to trust_score,
            "createdAt" to createdAt,
            "lastLoginAt" to lastLoginAt
        )
    }

    companion object {
        // Create from Firestore document
        fun fromMap(map: Map<String, Any>): UserData {
            val location = map["location"] as? Map<*, *>
            return UserData(
                userId = map["userId"] as? String ?: "",
                name = map["name"] as? String ?: "",
                aadhar_hash = map["aadhar_hash"] as? String ?: "",
                city = map["city"] as? String ?: "Bangalore",
                locationLat = (location?.get("lat") as? Number)?.toDouble() ?: 0.0,
                locationLng = (location?.get("lng") as? Number)?.toDouble() ?: 0.0,
                badges = (map["badges"] as? List<*>)?.mapNotNull { it as? String }
                    ?: listOf("Verified"),
                civic_coin = (map["civic_coin"] as? Number)?.toInt() ?: 0,
                impact_score = (map["impact_score"] as? Number)?.toInt() ?: 0,
                total_reports = (map["total_reports"] as? Number)?.toInt() ?: 0,
                trust_score = (map["trust_score"] as? Number)?.toDouble() ?: 0.5,
                createdAt = (map["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis(),
                lastLoginAt = (map["lastLoginAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
            )
        }
    }
}