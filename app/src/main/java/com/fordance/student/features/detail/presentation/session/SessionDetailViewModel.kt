package com.fordance.student.features.detail.presentation.session

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.features.detail.domain.usecase.GetSessionDetailUseCase
import com.fordance.student.features.detail.domain.usecase.ToggleFavoriteSessionUseCase
import com.fordance.student.features.detail.domain.usecase.ToggleJoinSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionDetailViewModel @Inject constructor(
    private val getSessionDetailUseCase: GetSessionDetailUseCase,
    private val toggleJoinSessionUseCase: ToggleJoinSessionUseCase,
    private val toggleFavoriteSessionUseCase: ToggleFavoriteSessionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sessionId: String = checkNotNull(savedStateHandle["sessionId"])

    private val _uiState = MutableStateFlow<SessionDetailUiState>(SessionDetailUiState.Loading)
    val uiState: StateFlow<SessionDetailUiState> = _uiState.asStateFlow()

    init {
        loadSessionDetail()
    }

    fun loadSessionDetail() {
        viewModelScope.launch {
            _uiState.value = SessionDetailUiState.Loading
            when (val result = getSessionDetailUseCase(sessionId)) {
                is ResultWrapper.Success -> {
                    _uiState.value = SessionDetailUiState.Success(session = result.data)
                }
                is ResultWrapper.Error -> {
                    _uiState.value = SessionDetailUiState.Error(result.message)
                }
                is ResultWrapper.Loading -> Unit
            }
        }
    }

    fun toggleJoin() {
        val currentState = _uiState.value as? SessionDetailUiState.Success ?: return
        val currentlyJoined = currentState.session.isJoined

        // Optimistic UI update
        _uiState.update {
            currentState.copy(
                session = currentState.session.copy(isJoined = !currentlyJoined),
                isActionLoading = true
            )
        }

        viewModelScope.launch {
            when (val result = toggleJoinSessionUseCase(sessionId, currentlyJoined)) {
                is ResultWrapper.Success -> {
                    _uiState.update { state ->
                        (state as? SessionDetailUiState.Success)?.copy(
                            session = (state as SessionDetailUiState.Success).session.copy(isJoined = result.data),
                            isActionLoading = false,
                            actionMessage = if (result.data) "Đã tham gia buổi học!" else "Đã hủy tham gia."
                        ) ?: state
                    }
                }
                is ResultWrapper.Error -> {
                    // Rollback on error
                    _uiState.update { state ->
                        (state as? SessionDetailUiState.Success)?.copy(
                            session = (state as SessionDetailUiState.Success).session.copy(isJoined = currentlyJoined),
                            isActionLoading = false,
                            actionMessage = result.message
                        ) ?: state
                    }
                }
                is ResultWrapper.Loading -> Unit
            }
        }
    }

    fun toggleFavorite() {
        val currentState = _uiState.value as? SessionDetailUiState.Success ?: return
        val currentlyFavorited = currentState.session.isFavorited

        // Optimistic UI update
        _uiState.update {
            currentState.copy(
                session = currentState.session.copy(isFavorited = !currentlyFavorited),
                isActionLoading = true
            )
        }

        viewModelScope.launch {
            when (val result = toggleFavoriteSessionUseCase(sessionId, currentlyFavorited)) {
                is ResultWrapper.Success -> {
                    _uiState.update { state ->
                        (state as? SessionDetailUiState.Success)?.copy(
                            session = (state as SessionDetailUiState.Success).session.copy(isFavorited = result.data),
                            isActionLoading = false,
                            actionMessage = if (result.data) "Đã thêm vào mục yêu thích!" else "Đã xóa khỏi mục yêu thích."
                        ) ?: state
                    }
                }
                is ResultWrapper.Error -> {
                    // Rollback
                    _uiState.update { state ->
                        (state as? SessionDetailUiState.Success)?.copy(
                            session = (state as SessionDetailUiState.Success).session.copy(isFavorited = currentlyFavorited),
                            isActionLoading = false,
                            actionMessage = result.message
                        ) ?: state
                    }
                }
                is ResultWrapper.Loading -> Unit
            }
        }
    }

    fun clearActionMessage() {
        _uiState.update { state ->
            (state as? SessionDetailUiState.Success)?.copy(actionMessage = null) ?: state
        }
    }
}
