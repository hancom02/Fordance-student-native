package com.fordance.student.core.domain.model

data class Instructor(
    val id: String,
    val name: String,
    val username: String,
    val avatarUrl: String,
    val role: String,
    val active: Boolean
)
