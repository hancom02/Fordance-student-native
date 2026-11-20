package com.fordance.student.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSessionFavDto(
    @SerialName("user_id") val userId: String,
    @SerialName("session_id") val sessionId: Long,
    @SerialName("is_favourite") val isFavourite: Boolean? = true
)
