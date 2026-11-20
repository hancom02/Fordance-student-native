package com.fordance.student.core.domain.model

data class SessionDetail(
    val id: Long,
    val title: String,
    val level: DanceLevel,
    val genre: DanceGenre,
    val videoUrl: String,
    val thumbnailUrl: String,
    val durationText: String,
    val classId: Long?,
    val price: Double,
    val description: String,
    val whatLearn: String,
    val whatPrepare: String,
    val instructor: Instructor?,
    val danceClass: DanceClass?,
    val isJoined: Boolean,
    val isFavorited: Boolean
)
