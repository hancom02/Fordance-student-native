package com.fordance.student.features.detail.domain.usecase

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.features.detail.domain.repository.DetailRepository
import javax.inject.Inject

class ToggleFavoriteSessionUseCase @Inject constructor(
    private val repository: DetailRepository
) {
    suspend operator fun invoke(sessionId: String, currentlyFavorited: Boolean): ResultWrapper<Boolean> {
        return repository.toggleFavoriteSession(sessionId, currentlyFavorited)
    }
}
