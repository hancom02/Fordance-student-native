package com.fordance.student.features.catalog.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.domain.model.CatalogFilter
import com.fordance.student.features.catalog.domain.repository.CatalogRepository
import com.fordance.student.features.catalog.domain.usecase.CatalogResult
import com.fordance.student.features.catalog.domain.usecase.CatalogTab
import com.fordance.student.features.catalog.domain.usecase.SearchCatalogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val searchCatalogUseCase: SearchCatalogUseCase,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")

    init {
        loadInstructors()
        observeSearch()
        executeSearch()
    }

    private fun observeSearch() {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(300L)
                .distinctUntilChanged()
                .collectLatest { query ->
                    executeSearch(query = query)
                }
        }
    }

    private fun loadInstructors() {
        viewModelScope.launch {
            when (val result = catalogRepository.getInstructors()) {
                is ResultWrapper.Success -> {
                    _uiState.update { it.copy(availableInstructors = result.data) }
                }
                else -> Unit
            }
        }
    }

    fun onEvent(event: CatalogUiEvent) {
        when (event) {
            is CatalogUiEvent.TabSelected -> {
                if (_uiState.value.selectedTab != event.tab) {
                    _uiState.update {
                        it.copy(
                            selectedTab = event.tab,
                            searchQuery = "",
                            filter = CatalogFilter()
                        )
                    }
                    searchQueryFlow.value = ""
                    executeSearch(tab = event.tab, query = "", filter = CatalogFilter())
                }
            }
            is CatalogUiEvent.SearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                searchQueryFlow.value = event.query
            }
            is CatalogUiEvent.FilterApplied -> {
                _uiState.update { it.copy(filter = event.filter) }
                executeSearch(filter = event.filter)
            }
            is CatalogUiEvent.FilterSheetToggled -> {
                _uiState.update { it.copy(isFilterSheetOpen = !it.isFilterSheetOpen) }
            }
            is CatalogUiEvent.ResetFilter -> {
                val emptyFilter = CatalogFilter()
                _uiState.update { it.copy(filter = emptyFilter) }
                executeSearch(filter = emptyFilter)
            }
            is CatalogUiEvent.Retry -> {
                executeSearch()
            }
        }
    }

    private fun executeSearch(
        tab: CatalogTab = _uiState.value.selectedTab,
        query: String = _uiState.value.searchQuery,
        filter: CatalogFilter = _uiState.value.filter
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = searchCatalogUseCase(tab, query, filter)) {
                is ResultWrapper.Success -> {
                    _uiState.update { state ->
                        when (val data = result.data) {
                            is CatalogResult.SessionsResult -> state.copy(
                                isLoading = false,
                                sessions = data.sessions,
                                errorMessage = null
                            )
                            is CatalogResult.ClassesResult -> state.copy(
                                isLoading = false,
                                classes = data.classes,
                                errorMessage = null
                            )
                            is CatalogResult.InstructorsResult -> state.copy(
                                isLoading = false,
                                instructors = data.instructors,
                                errorMessage = null
                            )
                        }
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
}
