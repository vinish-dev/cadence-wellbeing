package com.vinish.cadence

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform