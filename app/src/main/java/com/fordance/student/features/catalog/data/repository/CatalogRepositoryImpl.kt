package com.fordance.student.features.catalog.data.repository

import com.fordance.student.core.common.IoDispatcher
import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.data.dto.ClassDto
import com.fordance.student.core.data.dto.InstructorDto
import com.fordance.student.core.data.dto.SessionDto
import com.fordance.student.core.data.mapper.toDomain
import com.fordance.student.core.domain.model.CatalogFilter
import com.fordance.student.core.domain.model.DanceClass
import com.fordance.student.core.domain.model.Instructor
import com.fordance.student.core.domain.model.Session
import com.fordance.student.features.catalog.domain.repository.CatalogRepository
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CatalogRepository {

    override suspend fun searchSessions(
        query: String?,
        filter: CatalogFilter?
    ): ResultWrapper<List<Session>> = withContext(ioDispatcher) {
        try {
            val sessions = postgrest.from("sessions")
                .select {
                    filter {
                        neq("status", "rejected")
                        neq("status", "waiting")
                        neq("status", "deleted")

                        if (!query.isNullOrBlank()) {
                            ilike("session_name", "%${query.trim()}%")
                        }

                        if (filter != null) {
                            if (filter.levels.isNotEmpty()) {
                                isIn("level", filter.levels.map { it.rawValue })
                            }
                            if (filter.genres.isNotEmpty()) {
                                isIn("genre", filter.genres.toList())
                            }
                            if (filter.selectedInstructorIds.isNotEmpty()) {
                                isIn("instructor_id", filter.selectedInstructorIds.toList())
                            }
                        }
                    }
                    order(column = "id", order = Order.ASCENDING)
                }
                .decodeList<SessionDto>()

            ResultWrapper.Success(sessions.map { it.toDomain() })
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to search sessions", e)
        }
    }

    override suspend fun searchClasses(
        query: String?,
        filter: CatalogFilter?
    ): ResultWrapper<List<DanceClass>> = withContext(ioDispatcher) {
        try {
            val classes = postgrest.from("classes")
                .select {
                    filter {
                        if (!query.isNullOrBlank()) {
                            ilike("class_name", "%${query.trim()}%")
                        }

                        if (filter != null) {
                            if (filter.levels.isNotEmpty()) {
                                isIn("level", filter.levels.map { it.rawValue })
                            }
                            if (filter.genres.isNotEmpty()) {
                                isIn("genre", filter.genres.toList())
                            }
                            if (filter.selectedInstructorIds.isNotEmpty()) {
                                isIn("instructor_id", filter.selectedInstructorIds.toList())
                            }
                        }
                    }
                    order(column = "id", order = Order.ASCENDING)
                }
                .decodeList<ClassDto>()

            ResultWrapper.Success(classes.map { it.toDomain() })
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to search classes", e)
        }
    }

    override suspend fun searchInstructors(query: String?): ResultWrapper<List<Instructor>> = withContext(ioDispatcher) {
        try {
            val instructors = postgrest.from("users")
                .select {
                    filter {
                        eq("role", "instructor")
                        neq("active", false)
                        if (!query.isNullOrBlank()) {
                            ilike("name", "%${query.trim()}%")
                        }
                    }
                }
                .decodeList<InstructorDto>()

            ResultWrapper.Success(instructors.map { it.toDomain() })
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to search instructors", e)
        }
    }

    override suspend fun getInstructors(): ResultWrapper<List<Instructor>> = withContext(ioDispatcher) {
        try {
            val instructors = postgrest.from("users")
                .select {
                    filter {
                        eq("role", "instructor")
                        neq("active", false)
                    }
                }
                .decodeList<InstructorDto>()

            ResultWrapper.Success(instructors.map { it.toDomain() })
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to fetch instructors", e)
        }
    }
}
