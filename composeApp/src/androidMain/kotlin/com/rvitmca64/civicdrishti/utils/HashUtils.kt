package com.rvitmca64.civicdrishti.utils

import java.security.MessageDigest

object HashUtils {
    /**
     * Generate SHA-256 hash of input string
     */
    fun sha256(input: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * Hash Aadhaar number for secure storage
     */
    fun hashAadhaar(aadhaarNumber: String): String {
        // Remove any spaces or special characters
        val cleaned = aadhaarNumber.replace(Regex("[^0-9]"), "")
        return sha256(cleaned)
    }
}