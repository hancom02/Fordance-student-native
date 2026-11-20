package com.fordance.student.core.domain.model

data class Session(
    val id: Long,
    val sessionName: String,
    val level: DanceLevel,
    val genre: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val duration: String,
    val classId: Long,
    val price: Double,
    val instructorId: String,
    val instructorName: String? = null,
    val status: String,
    val realDuration: Double? = null,
    val createdAt: String? = null
)
