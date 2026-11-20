package com.fordance.student.features.catalog.domain.repository

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.domain.model.CatalogFilter
import com.fordance.student.core.domain.model.DanceClass
import com.fordance.student.core.domain.model.Instructor
import com.fordance.student.core.domain.model.Session

interface CatalogRepository {
    suspend fun searchSessions(query: String?, filter: CatalogFilter? = null): ResultWrapper<List<Session>>
    suspend fun searchClasses(query: String?, filter: CatalogFilter? = null): ResultWrapper<List<DanceClass>>
    suspend fun searchInstructors(query: String?): ResultWrapper<List<Instructor>>
    suspend fun getInstructors(): ResultWrapper<List<Instructor>>
}
