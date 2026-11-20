package com.fordance.student.features.detail.domain.repository

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.domain.model.ClassDetail
import com.fordance.student.core.domain.model.SessionDetail

interface DetailRepository {
    suspend fun getSessionDetail(sessionId: String): ResultWrapper<SessionDetail>
    suspend fun toggleJoinSession(sessionId: String, currentlyJoined: Boolean): ResultWrapper<Boolean>
    suspend fun toggleFavoriteSession(sessionId: String, currentlyFavorited: Boolean): ResultWrapper<Boolean>
    suspend fun getClassDetail(classId: String): ResultWrapper<ClassDetail>
}
