package com.fordance.student.features.detail.presentation.courseclass

import com.fordance.student.core.domain.model.ClassDetail

sealed interface ClassDetailUiState {
    data object Loading : ClassDetailUiState
    data class Success(
        val danceClass: ClassDetail,
        val isEnrolled: Boolean = false,
        val isEnrolling: Boolean = false,
        val message: String? = null
    ) : ClassDetailUiState
    data class Error(val message: String) : ClassDetailUiState
}
