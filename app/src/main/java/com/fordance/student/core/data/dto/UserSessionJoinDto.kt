package com.fordance.student.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSessionJoinDto(
    @SerialName("user_id") val userId: String,
    @SerialName("session_id") val sessionId: Long,
    @SerialName("progress") val progress: Double? = null,
    @SerialName("result_video_url") val resultVideoUrl: String? = null,
    @SerialName("rating") val rating: Double? = null,
    @SerialName("review") val review: String? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("teacher_feedback") val teacherFeedback: String? = null,
    @SerialName("last_watch_time") val lastWatchTime: Double? = null,
    @SerialName("longest_watch_time") val longestWatchTime: Double? = null,
    @SerialName("created_at") val createdAt: String? = null
)
