package com.fordance.student.features.catalog.presentation

import com.fordance.student.core.domain.model.CatalogFilter
import com.fordance.student.core.domain.model.DanceClass
import com.fordance.student.core.domain.model.Instructor
import com.fordance.student.core.domain.model.Session
import com.fordance.student.features.catalog.domain.usecase.CatalogTab

data class CatalogUiState(
    val selectedTab: CatalogTab = CatalogTab.SESSIONS,
    val searchQuery: String = "",
    val filter: CatalogFilter = CatalogFilter(),
    val availableInstructors: List<Instructor> = emptyList(),
    val sessions: List<Session> = emptyList(),
    val classes: List<DanceClass> = emptyList(),
    val instructors: List<Instructor> = emptyList(),
    val isLoading: Boolean = false,
    val isFilterSheetOpen: Boolean = false,
    val errorMessage: String? = null
)

sealed interface CatalogUiEvent {
    data class TabSelected(val tab: CatalogTab) : CatalogUiEvent
    data class SearchQueryChanged(val query: String) : CatalogUiEvent
    data class FilterApplied(val filter: CatalogFilter) : CatalogUiEvent
    data object FilterSheetToggled : CatalogUiEvent
    data object ResetFilter : CatalogUiEvent
    data object Retry : CatalogUiEvent
}
