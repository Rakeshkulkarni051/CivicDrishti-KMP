package com.rvitmca64.civicdrishti

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform