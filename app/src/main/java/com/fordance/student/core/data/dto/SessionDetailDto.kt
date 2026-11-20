package com.fordance.student.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionDetailDto(
    @SerialName("id") val id: Long,
    @SerialName("session_name") val sessionName: String = "",
    @SerialName("level") val level: String = "beginner",
    @SerialName("genre") val genre: String = "",
    @SerialName("video_url") val videoUrl: String? = null,
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,
    @SerialName("duration") val duration: String? = null,
    @SerialName("class_id") val classId: Long? = null,
    @SerialName("price") val price: Double? = null,
    @SerialName("instructor_id") val instructorId: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("what_learn") val whatLearn: String? = null,
    @SerialName("what_prepare") val whatPrepare: String? = null,
    @SerialName("instructor") val instructor: InstructorDto? = null,
    @SerialName("dance_class") val danceClass: ClassDto? = null,
    @SerialName("is_joined") val isJoined: Boolean = false,
    @SerialName("is_favourited") val isFavourited: Boolean = false
)
