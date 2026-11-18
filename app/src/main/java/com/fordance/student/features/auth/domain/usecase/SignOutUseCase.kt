package com.fordance.student.features.auth.domain.usecase

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): ResultWrapper<Unit> {
        return authRepository.signOut()
    }
}
