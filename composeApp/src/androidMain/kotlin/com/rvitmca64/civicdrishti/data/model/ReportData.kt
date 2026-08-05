package com.rvitmca64.civicdrishti.data.model

data class ReportData(
    val reportId: String = "",
    val userId: String = "",
    val reportedBy: String = "",
    val location: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val issueType: String = "",
    val detectedIssue: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val priority: Int = 0,
    val status: String = "REPORTED",
    val createdAt: Long = System.currentTimeMillis(),
    val acknowledgedAt: Long? = null,
    val actionAssignedAt: Long? = null,
    val resolvedAt: Long? = null,
    val coinRewardedAt: Long? = null,
    val civicCoinReward: Int = 0
) {
    /**
     * Convert ReportData to Firestore Map
     */
    fun toMap(): Map<String, Any> {
        return mapOf(
            "reportId" to reportId,
            "userId" to userId,
            "reportedBy" to reportedBy,
            "location" to location,
            "latitude" to latitude,
            "longitude" to longitude,
            "issueType" to issueType,
            "detectedIssue" to detectedIssue,
            "description" to description,
            "imageUrl" to imageUrl,
            "priority" to priority,
            "status" to status,
            "createdAt" to createdAt,
            "acknowledgedAt" to acknowledgedAt,
            "actionAssignedAt" to actionAssignedAt,
            "resolvedAt" to resolvedAt,
            "coinRewardedAt" to coinRewardedAt,
            "civicCoinReward" to civicCoinReward
        ) as Map<String, Any>
    }

    companion object {
        /**
         * Create ReportData from Firestore document Map
         */
        fun fromMap(map: Map<String, Any>): ReportData {
            return ReportData(
                reportId = map["reportId"] as? String ?: "",
                userId = map["userId"] as? String ?: "",
                reportedBy = map["reportedBy"] as? String ?: "",
                location = map["location"] as? String ?: "",
                latitude = (map["latitude"] as? Number)?.toDouble() ?: 0.0,
                longitude = (map["longitude"] as? Number)?.toDouble() ?: 0.0,
                issueType = map["issueType"] as? String ?: "",
                detectedIssue = map["detectedIssue"] as? String ?: "",
                description = map["description"] as? String ?: "",
                imageUrl = map["imageUrl"] as? String ?: "",
                priority = (map["priority"] as? Number)?.toInt() ?: 0,
                status = map["status"] as? String ?: "REPORTED",
                createdAt = (map["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis(),
                acknowledgedAt = (map["acknowledgedAt"] as? Number)?.toLong(),
                actionAssignedAt = (map["actionAssignedAt"] as? Number)?.toLong(),
                resolvedAt = (map["resolvedAt"] as? Number)?.toLong(),
                coinRewardedAt = (map["coinRewardedAt"] as? Number)?.toLong(),
                civicCoinReward = (map["civicCoinReward"] as? Number)?.toInt() ?: 0
            )
        }
    }
}