package com.rvitmca64.civicdrishti.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rvitmca64.civicdrishti.data.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {

    companion object {
        private const val TAG = "SessionManager"
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val AADHAR_HASH = stringPreferencesKey("aadhar_hash")
        private val SESSION_TIMESTAMP = longPreferencesKey("session_timestamp")
        private val CITY = stringPreferencesKey("city")

        private const val SESSION_DURATION_DAYS = 30L
    }

    /**
     * Save user session
     */
    suspend fun saveSession(user: UserData) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = user.userId
            preferences[USER_NAME] = user.name
            preferences[AADHAR_HASH] = user.aadhar_hash
            preferences[CITY] = user.city
            preferences[SESSION_TIMESTAMP] = System.currentTimeMillis()
        }
        Log.d(TAG, "Session saved for user: ${user.name}")
    }

    /**
     * Get current session if valid (not expired)
     */
    suspend fun getSession(): UserData? {
        val preferences = context.dataStore.data.first()

        val userId = preferences[USER_ID] ?: return null
        val timestamp = preferences[SESSION_TIMESTAMP] ?: return null

        // Check if session is expired (30 days)
        val currentTime = System.currentTimeMillis()
        val sessionDuration = currentTime - timestamp
        val maxDuration = TimeUnit.DAYS.toMillis(SESSION_DURATION_DAYS)

        if (sessionDuration > maxDuration) {
            Log.d(TAG, "Session expired")
            clearSession()
            return null
        }

        // Return minimal user data from session
        return UserData(
            userId = userId,
            name = preferences[USER_NAME] ?: "",
            aadhar_hash = preferences[AADHAR_HASH] ?: "",
            city = preferences[CITY] ?: "Bangalore"
        )
    }

    /**
     * Check if session exists and is valid
     */
    suspend fun isSessionValid(): Boolean {
        return getSession() != null
    }

    /**
     * Clear session (logout)
     */
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
        Log.d(TAG, "Session cleared completely")
    }

    /**
     * Flow to observe session changes
     */
    fun sessionFlow(): Flow<UserData?> {
        return context.dataStore.data.map { preferences ->
            val userId = preferences[USER_ID] ?: return@map null
            val timestamp = preferences[SESSION_TIMESTAMP] ?: return@map null

            val currentTime = System.currentTimeMillis()
            val sessionDuration = currentTime - timestamp
            val maxDuration = TimeUnit.DAYS.toMillis(SESSION_DURATION_DAYS)

            if (sessionDuration > maxDuration) {
                return@map null
            }

            UserData(
                userId = userId,
                name = preferences[USER_NAME] ?: "",
                aadhar_hash = preferences[AADHAR_HASH] ?: "",
                city = preferences[CITY] ?: "Bangalore"
            )
        }
    }
}