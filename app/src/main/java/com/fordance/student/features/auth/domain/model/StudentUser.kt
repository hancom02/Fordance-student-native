package com.fordance.student.features.auth.domain.model

data class StudentUser(
    val id: String,
    val email: String,
    val username: String,
    val avatarUrl: String? = null,
    val role: String = "student"
)

data class UserSession(
    val user: StudentUser,
    val accessToken: String?,
    val refreshToken: String?
)
