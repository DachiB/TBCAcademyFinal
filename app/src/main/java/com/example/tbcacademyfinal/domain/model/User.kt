package com.example.tbcacademyfinal.domain.model

import androidx.annotation.Keep

@Keep // Helps ensure R8/ProGuard doesn't remove this class or its constructor in release builds
data class User(
    val uid: String = "", // Provide default value
    val email: String = "", // Provide default value
    val displayName: String? = null, // Already has default
    val createdAt: Long = 0L // Provide a sensible default (0 or System.currentTimeMillis())
)