package com.fordance.student.core.domain.model

data class DanceClass(
    val id: Long,
    val className: String,
    val whatLearn: String,
    val whatPrepare: String,
    val level: DanceLevel,
    val genre: String,
    val imageCoverUrl: String,
    val instructorId: String?,
    val instructorUsername: String,
    val introduce: String,
    val sessionCount: Int
)
