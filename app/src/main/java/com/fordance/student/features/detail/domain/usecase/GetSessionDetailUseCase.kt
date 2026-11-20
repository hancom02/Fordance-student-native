package com.fordance.student.features.detail.domain.usecase

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.domain.model.SessionDetail
import com.fordance.student.features.detail.domain.repository.DetailRepository
import javax.inject.Inject

class GetSessionDetailUseCase @Inject constructor(
    private val repository: DetailRepository
) {
    suspend operator fun invoke(sessionId: String): ResultWrapper<SessionDetail> {
        return repository.getSessionDetail(sessionId)
    }
}
