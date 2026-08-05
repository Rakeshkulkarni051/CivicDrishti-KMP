package com.rvitmca64.civicdrishti.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rvitmca64.civicdrishti.data.model.ReportData
import com.rvitmca64.civicdrishti.data.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI State for Reported Issues Screen
 */
data class ReportedIssuesUiState(
    val reports: List<ReportData> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEmpty: Boolean = false
)

/**
 * Filter options for reports
 */
enum class ReportStatusFilter(val displayName: String, val firestoreValue: String) {
    ALL("All Reports", ""),
    REPORTED("Reported", "REPORTED"),
    ACKNOWLEDGED("Acknowledged", "ACKNOWLEDGED"),
    IN_PROGRESS("In Progress", "IN_PROGRESS"),
    RESOLVED("Resolved", "RESOLVED")
}

/**
 * ViewModel for managing user's reported issues
 */
class ReportedIssuesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ReportRepository()

    private val _uiState = MutableStateFlow(ReportedIssuesUiState())
    val uiState: StateFlow<ReportedIssuesUiState> = _uiState.asStateFlow()

    private val _selectedFilter = MutableStateFlow(ReportStatusFilter.ALL)
    val selectedFilter: StateFlow<ReportStatusFilter> = _selectedFilter.asStateFlow()

    companion object {
        private const val TAG = "ReportedIssuesViewModel"
    }

    /**
     * Load all reports for a specific user
     * @param userId - The current logged-in user's ID
     */
    fun loadUserReports(userId: String) {
        if (userId.isBlank()) {
            _uiState.value = ReportedIssuesUiState(
                isLoading = false,
                errorMessage = "User ID is required",
                isEmpty = true
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                Log.d(TAG, "Loading reports for user: $userId")

                val result = repository.getReportsByUser(userId)

                if (result.isSuccess) {
                    val reports = result.getOrNull() ?: emptyList()

                    _uiState.value = ReportedIssuesUiState(
                        reports = reports,
                        isLoading = false,
                        errorMessage = null,
                        isEmpty = reports.isEmpty()
                    )

                    Log.d(TAG, "Successfully loaded ${reports.size} reports")
                } else {
                    val error = result.exceptionOrNull()
                    val errorMsg = error?.message ?: "Failed to load reports"

                    _uiState.value = ReportedIssuesUiState(
                        reports = emptyList(),
                        isLoading = false,
                        errorMessage = errorMsg,
                        isEmpty = true
                    )

                    Log.e(TAG, "Error loading reports", error)
                }
            } catch (e: Exception) {
                _uiState.value = ReportedIssuesUiState(
                    reports = emptyList(),
                    isLoading = false,
                    errorMessage = "An unexpected error occurred: ${e.message}",
                    isEmpty = true
                )

                Log.e(TAG, "Exception loading reports", e)
            }
        }
    }

    /**
     * Load reports filtered by status
     * @param userId - User ID
     * @param filter - Status filter
     */
    fun loadReportsByStatus(userId: String, filter: ReportStatusFilter) {
        if (userId.isBlank()) return

        _selectedFilter.value = filter

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val result = if (filter == ReportStatusFilter.ALL) {
                    repository.getReportsByUser(userId)
                } else {
                    repository.getReportsByUserAndStatus(userId, filter.firestoreValue)
                }

                if (result.isSuccess) {
                    val reports = result.getOrNull() ?: emptyList()

                    _uiState.value = ReportedIssuesUiState(
                        reports = reports,
                        isLoading = false,
                        errorMessage = null,
                        isEmpty = reports.isEmpty()
                    )

                    Log.d(TAG, "Loaded ${reports.size} ${filter.displayName}")
                } else {
                    val errorMsg = result.exceptionOrNull()?.message ?: "Failed to load reports"

                    _uiState.value = ReportedIssuesUiState(
                        reports = emptyList(),
                        isLoading = false,
                        errorMessage = errorMsg,
                        isEmpty = true
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ReportedIssuesUiState(
                    reports = emptyList(),
                    isLoading = false,
                    errorMessage = e.message ?: "Unknown error",
                    isEmpty = true
                )
            }
        }
    }

    /**
     * Refresh reports
     */
    fun refreshReports(userId: String) {
        loadUserReports(userId)
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    /**
     * Get reports by status
     */
    fun getReportsByStatus(status: String): List<ReportData> {
        return _uiState.value.reports.filter { it.status == status }
    }

    /**
     * Get statistics
     */
    fun getReportStats(): ReportStats {
        val reports = _uiState.value.reports
        return ReportStats(
            total = reports.size,
            reported = reports.count { it.status == "REPORTED" },
            acknowledged = reports.count { it.status == "ACKNOWLEDGED" },
            inProgress = reports.count { it.status == "IN_PROGRESS" },
            resolved = reports.count { it.status == "RESOLVED" }
        )
    }
}

/**
 * Report statistics data class
 */
data class ReportStats(
    val total: Int = 0,
    val reported: Int = 0,
    val acknowledged: Int = 0,
    val inProgress: Int = 0,
    val resolved: Int = 0
)