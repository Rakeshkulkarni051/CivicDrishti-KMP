package com.rvitmca64.civicdrishti.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.rvitmca64.civicdrishti.data.model.ReportData
import com.rvitmca64.civicdrishti.data.model.UserData
import com.rvitmca64.civicdrishti.utils.HashUtils
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ReportRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val usersCollection = firestore.collection("users")
    private val reportsCollection = firestore.collection("reports")

    companion object {
        private const val TAG = "ReportRepository"
    }

    // ==========================================
    // AUTHENTICATION METHODS
    // ==========================================

    /**
     * Get user by Aadhaar hash
     */
    suspend fun getUserByAadhaarHash(aadhaarNumber: String): Result<UserData?> {
        return try {
            val hash = HashUtils.hashAadhaar(aadhaarNumber)
            Log.d(TAG, "Searching for user with hash: $hash")

            val querySnapshot = usersCollection
                .whereEqualTo("aadhar_hash", hash)
                .limit(1)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                Log.d(TAG, "No user found with this Aadhaar")
                Result.success(null)
            } else {
                val document = querySnapshot.documents[0]
                val userData = UserData.fromMap(document.data as Map<String, Any>)
                Log.d(TAG, "User found: ${userData.name}")
                Result.success(userData)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching user", e)
            Result.failure(e)
        }
    }

    /**
     * Create new user
     */
    suspend fun createUser(name: String, aadhaarNumber: String): Result<UserData> {
        return try {
            val hash = HashUtils.hashAadhaar(aadhaarNumber)
            val userId = usersCollection.document().id

            val newUser = UserData(
                userId = userId,
                name = name,
                aadhar_hash = hash,
                city = "Bangalore",
                badges = listOf("Verified", "Urban Vanguard", "City Protector"),
                civic_coin = 0,
                impact_score = 0,
                total_reports = 0,
                trust_score = 0.5,
                createdAt = System.currentTimeMillis(),
                lastLoginAt = System.currentTimeMillis()
            )

            usersCollection
                .document(userId)
                .set(newUser.toMap())
                .await()

            Log.d(TAG, "New user created: ${newUser.name}")
            Result.success(newUser)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating user", e)
            Result.failure(e)
        }
    }

    /**
     * Update last login timestamp
     */
    suspend fun updateLastLogin(userId: String): Result<Unit> {
        return try {
            usersCollection
                .document(userId)
                .update("lastLoginAt", System.currentTimeMillis())
                .await()

            Log.d(TAG, "Updated last login for user: $userId")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating last login", e)
            Result.failure(e)
        }
    }

    /**
     * Get full user data by userId
     */
    suspend fun getUserById(userId: String): Result<UserData?> {
        return try {
            val document = usersCollection
                .document(userId)
                .get()
                .await()

            if (document.exists()) {
                val userData = UserData.fromMap(document.data as Map<String, Any>)
                Result.success(userData)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching user by ID", e)
            Result.failure(e)
        }
    }

    // ==========================================
    // IMAGE UPLOAD METHODS
    // ==========================================

    /**
     * Upload image to Firebase Storage and return download URL
     */
    suspend fun uploadImage(imageBytes: ByteArray): Result<String> {
        return try {
            Log.d(TAG, "Starting image upload. Size: ${imageBytes.size} bytes")

            // Generate unique image ID
            val imageId = UUID.randomUUID().toString()
            val fileName = "$imageId.jpg"

            // Get storage reference
            val storageRef = storage.reference
                .child("report_images")
                .child(fileName)

            Log.d(TAG, "Storage path: report_images/$fileName")
            Log.d(TAG, "Storage bucket: ${storage.reference.bucket}")

            // Upload image with metadata
            val metadata = com.google.firebase.storage.StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build()

            val uploadTask = storageRef.putBytes(imageBytes, metadata).await()

            Log.d(TAG, "Upload successful. Bytes transferred: ${uploadTask.bytesTransferred}")

            // Get download URL
            val downloadUrl = storageRef.downloadUrl.await()
            val urlString = downloadUrl.toString()

            Log.d(TAG, "Download URL obtained: $urlString")

            Result.success(urlString)
        } catch (e: Exception) {
            Log.e(TAG, "Image upload failed", e)
            Log.e(TAG, "Error message: ${e.message}")
            Log.e(TAG, "Error class: ${e.javaClass.simpleName}")

            // More detailed error message
            val errorMessage = when {
                e.message?.contains("Object does not exist") == true ->
                    "Firebase Storage is not configured. Please enable Storage in Firebase Console."
                e.message?.contains("permission") == true ->
                    "Storage permission denied. Check Firebase Storage Rules."
                e.message?.contains("network") == true ->
                    "Network error. Check your internet connection."
                else ->
                    "Upload failed: ${e.message}"
            }

            Result.failure(Exception(errorMessage, e))
        }
    }

    // ==========================================
    // REPORT SUBMISSION METHODS
    // ==========================================

    /**
     * Submit report to Firestore
     */
    suspend fun submitReport(reportData: ReportData): Result<String> {
        return try {
            Log.d(TAG, "Submitting report to Firestore")

            val reportId = reportData.reportId.ifEmpty {
                reportsCollection.document().id
            }

            val finalReport = reportData.copy(reportId = reportId)

            Log.d(TAG, "Report ID: $reportId")
            Log.d(TAG, "Report data: $finalReport")

            reportsCollection
                .document(reportId)
                .set(finalReport)
                .await()

            Log.d(TAG, "Report submitted successfully")

            Result.success(reportId)
        } catch (e: Exception) {
            Log.e(TAG, "Report submission failed", e)
            Result.failure(e)
        }
    }

    /**
     * Complete workflow: Upload image then submit report
     */
    suspend fun uploadAndSubmitReport(
        imageBytes: ByteArray,
        reportData: ReportData
    ): Result<String> {
        return try {
            Log.d(TAG, "Starting upload and submit workflow")

            // First upload image
            val imageUrlResult = uploadImage(imageBytes)
            if (imageUrlResult.isFailure) {
                return Result.failure(imageUrlResult.exceptionOrNull()!!)
            }

            val imageUrl = imageUrlResult.getOrNull()!!
            Log.d(TAG, "Image uploaded. URL: $imageUrl")

            // Then submit report with image URL
            val reportWithImage = reportData.copy(imageUrl = imageUrl)
            submitReport(reportWithImage)
        } catch (e: Exception) {
            Log.e(TAG, "Upload and submit workflow failed", e)
            Result.failure(e)
        }
    }

    // ==========================================
    // FETCH USER REPORTS METHODS
    // ==========================================

    /**
     * Fetch all reports created by a specific user
     * @param userId - The unique user ID from authentication
     * @return List of ReportData sorted by creation date (newest first)
     */
    suspend fun getReportsByUser(userId: String): Result<List<ReportData>> {
        return try {
            Log.d(TAG, "Fetching reports for userId: $userId")

            val querySnapshot = reportsCollection
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                Log.d(TAG, "No reports found for user: $userId")
                return Result.success(emptyList())
            }

            val reports = querySnapshot.documents.mapNotNull { document ->
                try {
                    ReportData.fromMap(document.data as Map<String, Any>)
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing report document: ${document.id}", e)
                    null
                }
            }

            Log.d(TAG, "Successfully fetched ${reports.size} reports")
            Result.success(reports)

        } catch (e: Exception) {
            Log.e(TAG, "Error fetching user reports", e)
            Result.failure(e)
        }
    }

    /**
     * Fetch reports by user name (alternative query method)
     * Use this if userId is not available but you have the user's name
     * @param reportedBy - The name of the person who reported
     * @return List of ReportData sorted by creation date
     */
    suspend fun getReportsByReporterName(reportedBy: String): Result<List<ReportData>> {
        return try {
            Log.d(TAG, "Fetching reports for reporter: $reportedBy")

            val querySnapshot = reportsCollection
                .whereEqualTo("reportedBy", reportedBy)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                Log.d(TAG, "No reports found for reporter: $reportedBy")
                return Result.success(emptyList())
            }

            val reports = querySnapshot.documents.mapNotNull { document ->
                try {
                    ReportData.fromMap(document.data as Map<String, Any>)
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing report document: ${document.id}", e)
                    null
                }
            }

            Log.d(TAG, "Successfully fetched ${reports.size} reports for reporter")
            Result.success(reports)

        } catch (e: Exception) {
            Log.e(TAG, "Error fetching reports by reporter name", e)
            Result.failure(e)
        }
    }

    /**
     * Fetch reports with status filter
     * @param userId - User ID
     * @param status - Report status (e.g., "REPORTED", "ACKNOWLEDGED", "RESOLVED")
     * @return Filtered list of reports
     */
    suspend fun getReportsByUserAndStatus(
        userId: String,
        status: String
    ): Result<List<ReportData>> {
        return try {
            Log.d(TAG, "Fetching $status reports for userId: $userId")

            val querySnapshot = reportsCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", status)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val reports = querySnapshot.documents.mapNotNull { document ->
                try {
                    ReportData.fromMap(document.data as Map<String, Any>)
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing report: ${document.id}", e)
                    null
                }
            }

            Log.d(TAG, "Found ${reports.size} $status reports")
            Result.success(reports)

        } catch (e: Exception) {
            Log.e(TAG, "Error fetching reports by status", e)
            Result.failure(e)
        }
    }

    /**
     * Get count of user reports
     * @param userId - User ID
     * @return Total count of reports
     */
    suspend fun getUserReportsCount(userId: String): Result<Int> {
        return try {
            val querySnapshot = reportsCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()

            Result.success(querySnapshot.size())
        } catch (e: Exception) {
            Log.e(TAG, "Error getting reports count", e)
            Result.failure(e)
        }
    }

    /**
     * Get single report by ID
     * @param reportId - Report ID
     * @return ReportData or null
     */
    suspend fun getReportById(reportId: String): Result<ReportData?> {
        return try {
            val document = reportsCollection
                .document(reportId)
                .get()
                .await()

            if (document.exists()) {
                val report = ReportData.fromMap(document.data as Map<String, Any>)
                Result.success(report)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching report by ID", e)
            Result.failure(e)
        }
    }

    /**
     * Delete a report
     * @param reportId - Report ID to delete
     */
    suspend fun deleteReport(reportId: String): Result<Unit> {
        return try {
            reportsCollection
                .document(reportId)
                .delete()
                .await()

            Log.d(TAG, "Report deleted: $reportId")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting report", e)
            Result.failure(e)
        }
    }

    /**
     * Update report status
     * @param reportId - Report ID
     * @param newStatus - New status value
     */
    suspend fun updateReportStatus(reportId: String, newStatus: String): Result<Unit> {
        return try {
            reportsCollection
                .document(reportId)
                .update("status", newStatus)
                .await()

            Log.d(TAG, "Report status updated: $reportId -> $newStatus")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating report status", e)
            Result.failure(e)
        }
    }
}