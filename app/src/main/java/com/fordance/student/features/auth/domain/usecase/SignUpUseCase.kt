package com.fordance.student.features.auth.domain.usecase

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.features.auth.domain.model.StudentUser
import com.fordance.student.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        username: String
    ): ResultWrapper<StudentUser> {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()
        val trimmedUsername = username.trim()

        if (trimmedEmail.isEmpty()) {
            return ResultWrapper.Error("Email không được để trống")
        }
        if (trimmedPassword.length < 6) {
            return ResultWrapper.Error("Mật khẩu phải có ít nhất 6 ký tự")
        }
        if (trimmedUsername.isEmpty()) {
            return ResultWrapper.Error("Tên người dùng không được để trống")
        }

        return authRepository.signUp(trimmedEmail, trimmedPassword, trimmedUsername)
    }
}
