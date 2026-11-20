package com.fordance.student.features.discover.domain.repository

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.domain.model.DanceClass
import com.fordance.student.core.domain.model.Session

interface DiscoverRepository {
    suspend fun getFeaturedSessions(): ResultWrapper<List<Session>>
    suspend fun getJoinedSessions(userId: String): ResultWrapper<List<Session>>
    suspend fun getClasses(): ResultWrapper<List<DanceClass>>
    suspend fun getFavouriteSessions(userId: String): ResultWrapper<List<Session>>
}
