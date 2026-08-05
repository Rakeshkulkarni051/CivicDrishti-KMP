package com.rvitmca64.civicdrishti.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.rvitmca64.civicdrishti.data.model.UserData
import com.rvitmca64.civicdrishti.utils.HashUtils
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    companion object {
        private const val TAG = "AuthRepository"
    }

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
}