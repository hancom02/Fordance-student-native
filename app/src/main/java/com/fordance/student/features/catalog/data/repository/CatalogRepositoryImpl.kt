package com.fordance.student.features.catalog.data.repository

import com.fordance.student.core.common.IoDispatcher
import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.data.mapper.toDomain
import com.fordance.student.core.domain.model.CatalogFilter
import com.fordance.student.core.domain.model.DanceClass
import com.fordance.student.core.domain.model.Instructor
import com.fordance.student.core.domain.model.Session
import com.fordance.student.core.network.ForDanceApiService
import com.fordance.student.features.catalog.domain.repository.CatalogRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogRepositoryImpl @Inject constructor(
    private val apiService: ForDanceApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CatalogRepository {

    override suspend fun searchSessions(
        query: String?,
        filter: CatalogFilter?
    ): ResultWrapper<List<Session>> = withContext(ioDispatcher) {
        try {
            val levelParam = filter?.levels?.firstOrNull()?.rawValue
            val genreParam = filter?.genres?.firstOrNull()
            val instructorIdParam = filter?.selectedInstructorIds?.firstOrNull()

            val response = apiService.getCatalogSessions(
                query = query?.takeIf { it.isNotBlank() },
                level = levelParam,
                genre = genreParam,
                instructorId = instructorIdParam
            )

            if (response.success && response.data != null) {
                var list = response.data.map { it.toDomain() }
                // Apply multi-selection filters in memory if multiple are selected
                if (filter != null) {
                    if (filter.levels.isNotEmpty()) {
                        list = list.filter { it.level in filter.levels }
                    }
                    if (filter.genres.isNotEmpty()) {
                        list = list.filter { it.genre in filter.genres }
                    }
                    if (filter.selectedInstructorIds.isNotEmpty()) {
                        list = list.filter { it.instructorId in filter.selectedInstructorIds }
                    }
                }
                ResultWrapper.Success(list)
            } else {
                ResultWrapper.Error(response.message ?: "Failed to search sessions")
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to search sessions", e)
        }
    }

    override suspend fun searchClasses(
        query: String?,
        filter: CatalogFilter?
    ): ResultWrapper<List<DanceClass>> = withContext(ioDispatcher) {
        try {
            val levelParam = filter?.levels?.firstOrNull()?.rawValue
            val genreParam = filter?.genres?.firstOrNull()
            val instructorIdParam = filter?.selectedInstructorIds?.firstOrNull()

            val response = apiService.getCatalogClasses(
                query = query?.takeIf { it.isNotBlank() },
                level = levelParam,
                genre = genreParam,
                instructorId = instructorIdParam
            )

            if (response.success && response.data != null) {
                var list = response.data.map { it.toDomain() }
                if (filter != null) {
                    if (filter.levels.isNotEmpty()) {
                        list = list.filter { it.level in filter.levels }
                    }
                    if (filter.genres.isNotEmpty()) {
                        list = list.filter { it.genre in filter.genres }
                    }
                    if (filter.selectedInstructorIds.isNotEmpty()) {
                        list = list.filter { it.instructorId in filter.selectedInstructorIds }
                    }
                }
                ResultWrapper.Success(list)
            } else {
                ResultWrapper.Error(response.message ?: "Failed to search classes")
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to search classes", e)
        }
    }

    override suspend fun searchInstructors(query: String?): ResultWrapper<List<Instructor>> = withContext(ioDispatcher) {
        try {
            val response = apiService.getCatalogInstructors()
            if (response.success && response.data != null) {
                var list = response.data.map { it.toDomain() }
                if (!query.isNullOrBlank()) {
                    val q = query.trim().lowercase()
                    list = list.filter { it.name.lowercase().contains(q) || it.username.lowercase().contains(q) }
                }
                ResultWrapper.Success(list)
            } else {
                ResultWrapper.Error(response.message ?: "Failed to search instructors")
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to search instructors", e)
        }
    }

    override suspend fun getInstructors(): ResultWrapper<List<Instructor>> = withContext(ioDispatcher) {
        try {
            val response = apiService.getCatalogInstructors()
            if (response.success && response.data != null) {
                ResultWrapper.Success(response.data.map { it.toDomain() })
            } else {
                ResultWrapper.Error(response.message ?: "Failed to fetch instructors")
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to fetch instructors", e)
        }
    }
}
