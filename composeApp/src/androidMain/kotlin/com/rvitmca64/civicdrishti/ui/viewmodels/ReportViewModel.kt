package com.rvitmca64.civicdrishti.ui.viewmodels

import android.app.Application
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.rvitmca64.civicdrishti.data.model.ReportData
import com.rvitmca64.civicdrishti.data.repository.ReportRepository
import com.rvitmca64.civicdrishti.utils.LocationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ReportUiState {
    object Idle : ReportUiState()
    object Loading : ReportUiState()
    data class Success(val reportId: String) : ReportUiState()
    data class Error(val message: String) : ReportUiState()
}

data class CapturedImage(
    val bytes: ByteArray,
    val filePath: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CapturedImage
        return filePath == other.filePath
    }

    override fun hashCode(): Int = filePath.hashCode()
}

class ReportViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ReportRepository()
    private val locationHelper = LocationHelper(application)

    private val _uiState = MutableStateFlow<ReportUiState>(ReportUiState.Idle)
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    private val _capturedImage = MutableStateFlow<CapturedImage?>(null)
    val capturedImage: StateFlow<CapturedImage?> = _capturedImage.asStateFlow()

    private val _detectedLocation = MutableStateFlow<LatLng?>(null)
    val detectedLocation: StateFlow<LatLng?> = _detectedLocation.asStateFlow()

    private val _selectedLocation = MutableStateFlow<LatLng?>(null)
    val selectedLocation: StateFlow<LatLng?> = _selectedLocation.asStateFlow()

    private val _locationAddress = MutableStateFlow("Detecting location...")
    val locationAddress: StateFlow<String> = _locationAddress.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _issueType = MutableStateFlow("Pothole")
    val issueType: StateFlow<String> = _issueType.asStateFlow()

    fun setCapturedImage(bytes: ByteArray, filePath: String) {
        _capturedImage.value = CapturedImage(bytes, filePath)
    }

    fun clearCapturedImage() {
        _capturedImage.value = null
    }

    /**
     * Detect current location — FIXED WITH PERMISSION CHECK + TRY/CATCH
     */
    fun detectCurrentLocation() {
        viewModelScope.launch {

            val context = getApplication<Application>()

            val hasFine = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val hasCoarse = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasFine && !hasCoarse) {
                _locationAddress.value = "Location permission not granted"
                return@launch
            }

            _locationAddress.value = "Detecting location..."

            try {
                val result = locationHelper.getCurrentLocation()

                result.onSuccess { location ->
                    val latLng = LatLng(location.latitude, location.longitude)
                    _detectedLocation.value = latLng
                    _selectedLocation.value = latLng

                    val address = locationHelper.getAddressFromCoordinates(
                        location.latitude,
                        location.longitude
                    )
                    _locationAddress.value = address
                }.onFailure { error ->
                    _locationAddress.value = "Failed to detect location: ${error.message}"
                }

            } catch (e: SecurityException) {
                _locationAddress.value = "Permission error: ${e.message}"
            }
        }
    }

    fun updateSelectedLocation(latLng: LatLng) {
        _selectedLocation.value = latLng
        val address = locationHelper.getAddressFromCoordinates(
            latLng.latitude,
            latLng.longitude
        )
        _locationAddress.value = address
    }

    fun updateDescription(text: String) {
        if (text.length <= 100) {
            _description.value = text
        }
    }

    fun updateIssueType(type: String) {
        _issueType.value = type
    }

    fun isFormValid(): Boolean {
        return _capturedImage.value != null &&
                _selectedLocation.value != null &&
                _description.value.isNotBlank()
    }

    fun submitReport(userId: String, userName: String) {
        viewModelScope.launch {
            _uiState.value = ReportUiState.Loading

            val image = _capturedImage.value
            val location = _selectedLocation.value
            val desc = _description.value
            val type = _issueType.value

            if (image == null || location == null || desc.isBlank()) {
                _uiState.value = ReportUiState.Error("Missing required fields")
                return@launch
            }

            val reportData = ReportData(
                userId = userId,
                reportedBy = userName,
                location = _locationAddress.value,
                latitude = location.latitude,
                longitude = location.longitude,
                issueType = type,
                detectedIssue = type,
                description = desc,
                priority = 1,
                status = "REPORTED",
                createdAt = System.currentTimeMillis()
            )

            val result = repository.uploadAndSubmitReport(image.bytes, reportData)

            result.onSuccess { reportId ->
                _uiState.value = ReportUiState.Success(reportId)
                resetForm()
            }.onFailure { error ->
                _uiState.value = ReportUiState.Error(error.message ?: "Unknown error")
            }
        }
    }

    private fun resetForm() {
        _capturedImage.value = null
        _detectedLocation.value = null
        _selectedLocation.value = null
        _locationAddress.value = "Detecting location..."
        _description.value = ""
    }

    fun resetUiState() {
        _uiState.value = ReportUiState.Idle
    }
}
