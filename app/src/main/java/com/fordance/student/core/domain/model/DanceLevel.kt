package com.fordance.student.core.domain.model

enum class DanceLevel(val rawValue: String, val displayName: String) {
    BEGINNER("beginner", "Beginner"),
    INTERMEDIATE("intermediate", "Intermediate"),
    ADVANCED("advanced", "Advanced");

    companion object {
        fun fromRaw(value: String?): DanceLevel {
            return when (value?.trim()?.lowercase()) {
                "intermediate" -> INTERMEDIATE
                "advanced" -> ADVANCED
                else -> BEGINNER
            }
        }
    }
}
