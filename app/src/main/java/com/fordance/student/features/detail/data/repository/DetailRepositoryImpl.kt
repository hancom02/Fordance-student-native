package com.fordance.student.features.detail.data.repository

import com.fordance.student.core.common.IoDispatcher
import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.data.mapper.toDomain
import com.fordance.student.core.domain.model.ClassDetail
import com.fordance.student.core.domain.model.SessionDetail
import com.fordance.student.core.network.ForDanceApiService
import com.fordance.student.features.detail.domain.repository.DetailRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailRepositoryImpl @Inject constructor(
    private val apiService: ForDanceApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DetailRepository {

    override suspend fun getSessionDetail(sessionId: String): ResultWrapper<SessionDetail> = withContext(ioDispatcher) {
        try {
            val response = apiService.getSessionDetail(sessionId)
            if (response.success && response.data != null) {
                ResultWrapper.Success(response.data.toDomain())
            } else {
                ResultWrapper.Error(response.message ?: "Failed to load session details")
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to load session details", e)
        }
    }

    override suspend fun toggleJoinSession(
        sessionId: String,
        currentlyJoined: Boolean
    ): ResultWrapper<Boolean> = withContext(ioDispatcher) {
        try {
            val response = if (currentlyJoined) {
                apiService.leaveSession(sessionId)
            } else {
                apiService.joinSession(sessionId)
            }
            if (response.success) {
                ResultWrapper.Success(!currentlyJoined)
            } else {
                ResultWrapper.Error(response.message ?: "Failed to update enrollment")
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to update enrollment", e)
        }
    }

    override suspend fun toggleFavoriteSession(
        sessionId: String,
        currentlyFavorited: Boolean
    ): ResultWrapper<Boolean> = withContext(ioDispatcher) {
        try {
            val response = if (currentlyFavorited) {
                apiService.unfavoriteSession(sessionId)
            } else {
                apiService.favoriteSession(sessionId)
            }
            if (response.success) {
                ResultWrapper.Success(!currentlyFavorited)
            } else {
                ResultWrapper.Error(response.message ?: "Failed to update favorite status")
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to update favorite status", e)
        }
    }

    override suspend fun getClassDetail(classId: String): ResultWrapper<ClassDetail> = withContext(ioDispatcher) {
        try {
            val response = apiService.getClassDetail(classId)
            if (response.success && response.data != null) {
                ResultWrapper.Success(response.data.toDomain())
            } else {
                ResultWrapper.Error(response.message ?: "Failed to load class details")
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.message ?: "Failed to load class details", e)
        }
    }
}
