package com.fordance.student.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionDto(
    @SerialName("id") val id: Long,
    @SerialName("session_name") val sessionName: String = "",
    @SerialName("level") val level: String = "beginner",
    @SerialName("genre") val genre: String = "",
    @SerialName("video_url") val videoUrl: String = "",
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,
    @SerialName("duration") val duration: String? = null,
    @SerialName("class_id") val classId: Long = 0L,
    @SerialName("price") val price: Double? = null,
    @SerialName("instructor_id") val instructorId: String = "",
    @SerialName("status") val status: String? = null,
    @SerialName("real_duration") val realDuration: Double? = null,
    @SerialName("created_at") val createdAt: String? = null
)
