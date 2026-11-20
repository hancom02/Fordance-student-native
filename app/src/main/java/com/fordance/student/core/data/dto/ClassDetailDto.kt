package com.fordance.student.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClassDetailDto(
    @SerialName("id") val id: Long,
    @SerialName("class_name") val className: String = "",
    @SerialName("level") val level: String = "beginner",
    @SerialName("genre") val genre: String = "",
    @SerialName("image_cover_url") val imageCoverUrl: String? = null,
    @SerialName("instructor_id") val instructorId: String? = null,
    @SerialName("instructor_username") val instructorUsername: String? = null,
    @SerialName("introduce") val introduce: String? = null,
    @SerialName("what_learn") val whatLearn: String? = null,
    @SerialName("what_prepare") val whatPrepare: String? = null,
    @SerialName("instructor") val instructor: InstructorDto? = null,
    @SerialName("sessions") val sessions: List<SessionDto> = emptyList(),
    @SerialName("total_sessions") val totalSessions: Int = 0
)
