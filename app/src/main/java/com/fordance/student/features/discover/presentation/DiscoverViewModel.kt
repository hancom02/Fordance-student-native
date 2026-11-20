package com.fordance.student.features.discover.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.datastore.UserPreferencesDataStore
import com.fordance.student.features.discover.domain.usecase.GetDiscoverFeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val getDiscoverFeedUseCase: GetDiscoverFeedUseCase,
    private val dataStore: UserPreferencesDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    init {
        loadFeed()
    }

    fun onEvent(event: DiscoverUiEvent) {
        when (event) {
            DiscoverUiEvent.Refresh -> refreshFeed()
            DiscoverUiEvent.Retry -> loadFeed()
        }
    }

    private fun loadFeed() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val userId = dataStore.userIdFlow.firstOrNull().orEmpty()
            when (val result = getDiscoverFeedUseCase(userId)) {
                is ResultWrapper.Success -> {
                    val feed = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            featuredSessions = feed.featuredSessions,
                            joinedSessions = feed.joinedSessions,
                            classes = feed.classes,
                            favouriteSessions = feed.favouriteSessions,
                            errorMessage = null
                        )
                    }
                }
                is ResultWrapper.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
                is ResultWrapper.Loading -> Unit
            }
        }
    }

    private fun refreshFeed() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            val userId = dataStore.userIdFlow.firstOrNull().orEmpty()
            when (val result = getDiscoverFeedUseCase(userId)) {
                is ResultWrapper.Success -> {
                    val feed = result.data
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            featuredSessions = feed.featuredSessions,
                            joinedSessions = feed.joinedSessions,
                            classes = feed.classes,
                            favouriteSessions = feed.favouriteSessions,
                            errorMessage = null
                        )
                    }
                }
                is ResultWrapper.Error -> {
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            errorMessage = result.message
                        )
                    }
                }
                is ResultWrapper.Loading -> Unit
            }
        }
    }
}
