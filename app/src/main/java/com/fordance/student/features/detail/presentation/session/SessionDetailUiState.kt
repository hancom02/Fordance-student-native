package com.fordance.student.features.detail.presentation.session

import com.fordance.student.core.domain.model.SessionDetail

sealed interface SessionDetailUiState {
    data object Loading : SessionDetailUiState
    data class Success(
        val session: SessionDetail,
        val isActionLoading: Boolean = false,
        val actionMessage: String? = null
    ) : SessionDetailUiState
    data class Error(val message: String) : SessionDetailUiState
}
