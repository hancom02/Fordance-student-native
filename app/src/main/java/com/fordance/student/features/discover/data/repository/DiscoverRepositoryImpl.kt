package com.fordance.student.features.discover.data.repository

import com.fordance.student.core.common.IoDispatcher
import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.data.mapper.toDomain
import com.fordance.student.core.domain.model.DanceClass
import com.fordance.student.core.domain.model.Session
import com.fordance.student.core.network.ForDanceApiService
import com.fordance.student.features.discover.domain.repository.DiscoverRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoverRepositoryImpl @Inject constructor(
    private val apiService: ForDanceApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DiscoverRepository {

    override suspend fun getFeaturedSessions(): ResultWrapper<List<Session>> = withContext(ioDispatcher) {
        try {
            val response = apiService.getDiscoverFeeds()
            if (response.success && response.data != null) {
                ResultWrapper.Success(response.data.featured.map { it.toDomain() })
            } else {
                ResultWrapper.Error(response.message ?: "Failed to load featured sessions")
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to load featured sessions", e)
        }
    }

    override suspend fun getJoinedSessions(userId: String): ResultWrapper<List<Session>> = withContext(ioDispatcher) {
        try {
            if (userId.isBlank()) {
                return@withContext ResultWrapper.Success(emptyList())
            }
            val response = apiService.getDiscoverFeeds()
            if (response.success && response.data != null) {
                ResultWrapper.Success(response.data.joined.map { it.toDomain() })
            } else {
                ResultWrapper.Success(emptyList())
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to load joined sessions", e)
        }
    }

    override suspend fun getClasses(): ResultWrapper<List<DanceClass>> = withContext(ioDispatcher) {
        try {
            val response = apiService.getDiscoverFeeds()
            if (response.success && response.data != null) {
                ResultWrapper.Success(response.data.classes.map { it.toDomain() })
            } else {
                ResultWrapper.Error(response.message ?: "Failed to load classes")
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to load classes", e)
        }
    }

    override suspend fun getFavouriteSessions(userId: String): ResultWrapper<List<Session>> = withContext(ioDispatcher) {
        try {
            if (userId.isBlank()) {
                return@withContext ResultWrapper.Success(emptyList())
            }
            val response = apiService.getDiscoverFeeds()
            if (response.success && response.data != null) {
                ResultWrapper.Success(response.data.favourites.map { it.toDomain() })
            } else {
                ResultWrapper.Success(emptyList())
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to load favourite sessions", e)
        }
    }
}
