package com.fordance.student.features.auth.domain.usecase

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.features.auth.domain.model.StudentUser
import com.fordance.student.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): ResultWrapper<StudentUser> {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (trimmedEmail.isEmpty()) {
            return ResultWrapper.Error("Email không được để trống")
        }
        if (trimmedPassword.isEmpty()) {
            return ResultWrapper.Error("Mật khẩu không được để trống")
        }

        return authRepository.signIn(trimmedEmail, trimmedPassword)
    }
}
