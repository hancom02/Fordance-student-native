package com.fordance.student.features.auth.domain.usecase

import com.fordance.student.features.auth.domain.model.StudentUser
import com.fordance.student.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<StudentUser?> {
        return authRepository.getSessionFlow()
    }
}
