package com.fordance.student.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ActionResponseDto(
    val success: Boolean = true,
    val message: String? = null
)
