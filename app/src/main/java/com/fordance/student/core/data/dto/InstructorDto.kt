package com.fordance.student.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InstructorDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("role") val role: String? = "instructor",
    @SerialName("active") val active: Boolean = true
)
