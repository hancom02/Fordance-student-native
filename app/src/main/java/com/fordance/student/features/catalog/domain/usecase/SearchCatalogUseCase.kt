package com.fordance.student.features.catalog.domain.usecase

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.domain.model.CatalogFilter
import com.fordance.student.core.domain.model.DanceClass
import com.fordance.student.core.domain.model.Instructor
import com.fordance.student.core.domain.model.Session
import com.fordance.student.features.catalog.domain.repository.CatalogRepository
import javax.inject.Inject

enum class CatalogTab {
    SESSIONS,
    CLASSES,
    INSTRUCTORS
}

sealed interface CatalogResult {
    data class SessionsResult(val sessions: List<Session>) : CatalogResult
    data class ClassesResult(val classes: List<DanceClass>) : CatalogResult
    data class InstructorsResult(val instructors: List<Instructor>) : CatalogResult
}

class SearchCatalogUseCase @Inject constructor(
    private val repository: CatalogRepository
) {
    suspend operator fun invoke(
        tab: CatalogTab,
        query: String?,
        filter: CatalogFilter?
    ): ResultWrapper<CatalogResult> {
        return when (tab) {
            CatalogTab.SESSIONS -> {
                when (val result = repository.searchSessions(query, filter)) {
                    is ResultWrapper.Success -> ResultWrapper.Success(CatalogResult.SessionsResult(result.data))
                    is ResultWrapper.Error -> ResultWrapper.Error(result.message, result.throwable)
                    is ResultWrapper.Loading -> ResultWrapper.Loading
                }
            }
            CatalogTab.CLASSES -> {
                when (val result = repository.searchClasses(query, filter)) {
                    is ResultWrapper.Success -> ResultWrapper.Success(CatalogResult.ClassesResult(result.data))
                    is ResultWrapper.Error -> ResultWrapper.Error(result.message, result.throwable)
                    is ResultWrapper.Loading -> ResultWrapper.Loading
                }
            }
            CatalogTab.INSTRUCTORS -> {
                when (val result = repository.searchInstructors(query)) {
                    is ResultWrapper.Success -> ResultWrapper.Success(CatalogResult.InstructorsResult(result.data))
                    is ResultWrapper.Error -> ResultWrapper.Error(result.message, result.throwable)
                    is ResultWrapper.Loading -> ResultWrapper.Loading
                }
            }
        }
    }
}
