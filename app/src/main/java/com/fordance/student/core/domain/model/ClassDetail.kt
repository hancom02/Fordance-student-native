package com.fordance.student.core.domain.model

data class ClassDetail(
    val id: Long,
    val className: String,
    val level: DanceLevel,
    val genre: DanceGenre,
    val imageCoverUrl: String,
    val instructor: Instructor?,
    val introduce: String,
    val whatLearn: String,
    val whatPrepare: String,
    val sessions: List<Session>,
    val totalSessions: Int
)
