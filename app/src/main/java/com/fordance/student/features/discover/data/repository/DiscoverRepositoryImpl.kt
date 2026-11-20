package com.fordance.student.features.discover.data.repository

import com.fordance.student.core.common.IoDispatcher
import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.data.dto.ClassDto
import com.fordance.student.core.data.dto.SessionDto
import com.fordance.student.core.data.dto.UserSessionFavDto
import com.fordance.student.core.data.dto.UserSessionJoinDto
import com.fordance.student.core.data.mapper.toDomain
import com.fordance.student.core.domain.model.DanceClass
import com.fordance.student.core.domain.model.Session
import com.fordance.student.features.discover.domain.repository.DiscoverRepository
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoverRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DiscoverRepository {

    override suspend fun getFeaturedSessions(): ResultWrapper<List<Session>> = withContext(ioDispatcher) {
        try {
            val dtos = postgrest.from("sessions")
                .select {
                    filter {
                        neq("status", "rejected")
                        neq("status", "waiting")
                        neq("status", "deleted")
                    }
                }
                .decodeList<SessionDto>()
            ResultWrapper.Success(dtos.map { it.toDomain() })
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to load featured sessions", e)
        }
    }

    override suspend fun getJoinedSessions(userId: String): ResultWrapper<List<Session>> = withContext(ioDispatcher) {
        try {
            if (userId.isBlank()) {
                return@withContext ResultWrapper.Success(emptyList())
            }

            val joinedPivot = postgrest.from("users_sessions_joined")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                }
                .decodeList<UserSessionJoinDto>()

            if (joinedPivot.isEmpty()) {
                return@withContext ResultWrapper.Success(emptyList())
            }

            val sessionIds = joinedPivot.map { it.sessionId }
            val sessions = postgrest.from("sessions")
                .select {
                    filter {
                        isIn("id", sessionIds)
                    }
                }
                .decodeList<SessionDto>()

            ResultWrapper.Success(sessions.map { it.toDomain() })
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to load joined sessions", e)
        }
    }

    override suspend fun getClasses(): ResultWrapper<List<DanceClass>> = withContext(ioDispatcher) {
        try {
            val dtos = postgrest.from("classes")
                .select()
                .decodeList<ClassDto>()
            ResultWrapper.Success(dtos.map { it.toDomain() })
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to load classes", e)
        }
    }

    override suspend fun getFavouriteSessions(userId: String): ResultWrapper<List<Session>> = withContext(ioDispatcher) {
        try {
            if (userId.isBlank()) {
                return@withContext ResultWrapper.Success(emptyList())
            }

            val favPivot = postgrest.from("users_sessions_favourite")
                .select {
                    filter {
                        eq("user_id", userId)
                        eq("is_favourite", true)
                    }
                }
                .decodeList<UserSessionFavDto>()

            if (favPivot.isEmpty()) {
                return@withContext ResultWrapper.Success(emptyList())
            }

            val sessionIds = favPivot.map { it.sessionId }
            val sessions = postgrest.from("sessions")
                .select {
                    filter {
                        isIn("id", sessionIds)
                    }
                }
                .decodeList<SessionDto>()

            ResultWrapper.Success(sessions.map { it.toDomain() })
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to load favourite sessions", e)
        }
    }
}
