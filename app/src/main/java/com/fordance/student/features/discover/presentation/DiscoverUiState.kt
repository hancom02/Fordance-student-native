package com.fordance.student.features.discover.presentation

import com.fordance.student.core.domain.model.DanceClass
import com.fordance.student.core.domain.model.Session

data class DiscoverUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val featuredSessions: List<Session> = emptyList(),
    val joinedSessions: List<Session> = emptyList(),
    val classes: List<DanceClass> = emptyList(),
    val favouriteSessions: List<Session> = emptyList(),
    val errorMessage: String? = null
)

sealed interface DiscoverUiEvent {
    data object Refresh : DiscoverUiEvent
    data object Retry : DiscoverUiEvent
}
