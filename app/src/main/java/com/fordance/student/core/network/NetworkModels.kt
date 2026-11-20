package com.fordance.student.core.network

import com.fordance.student.core.data.dto.ClassDto
import com.fordance.student.core.data.dto.SessionDto
import com.fordance.student.features.auth.data.remote.dto.UserDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String? = null,
    val phone: String? = null
)

@Serializable
data class AuthResponseData(
    val token: String = "",
    @SerialName("refresh_token") val refreshToken: String? = null,
    val user: UserDto? = null
)

@Serializable
data class DiscoverFeedsResponseData(
    val featured: List<SessionDto> = emptyList(),
    val joined: List<SessionDto> = emptyList(),
    val classes: List<ClassDto> = emptyList(),
    val favourites: List<SessionDto> = emptyList()
)
