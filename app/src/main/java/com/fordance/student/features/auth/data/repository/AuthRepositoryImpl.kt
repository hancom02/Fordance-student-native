package com.fordance.student.features.auth.data.repository

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.datastore.UserPreferencesDataStore
import com.fordance.student.core.network.ForDanceApiService
import com.fordance.student.core.network.LoginRequest
import com.fordance.student.core.network.RegisterRequest
import com.fordance.student.features.auth.domain.model.StudentUser
import com.fordance.student.features.auth.domain.repository.AuthRepository
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ForDanceApiService,
    private val auth: Auth,
    private val dataStore: UserPreferencesDataStore
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): ResultWrapper<StudentUser> {
        return try {
            val response = apiService.login(LoginRequest(email = email, password = password))
            if (!response.success || response.data == null) {
                return ResultWrapper.Error(response.message ?: "Đăng nhập thất bại: Thông tin không hợp lệ.")
            }

            val authData = response.data
            val userDto = authData.user
            val studentUser = StudentUser(
                id = userDto?.id ?: "",
                email = userDto?.email ?: email,
                username = userDto?.username ?: email.substringBefore("@"),
                avatarUrl = userDto?.avatarUrl,
                role = "student"
            )

            dataStore.saveSession(
                userId = studentUser.id,
                email = studentUser.email,
                username = studentUser.username,
                accessToken = authData.token
            )

            ResultWrapper.Success(studentUser)
        } catch (e: Exception) {
            val message = when {
                e.message?.contains("401") == true || e.message?.contains("credentials", ignoreCase = true) == true ->
                    "Email hoặc mật khẩu không chính xác."
                e.message?.contains("403") == true ->
                    "Tài khoản này không phải là tài khoản học viên."
                else -> e.localizedMessage ?: "Đăng nhập thất bại"
            }
            ResultWrapper.Error(message, e)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        username: String
    ): ResultWrapper<StudentUser> {
        return try {
            val response = apiService.register(
                RegisterRequest(email = email, password = password, name = username)
            )
            if (!response.success || response.data == null) {
                return ResultWrapper.Error(response.message ?: "Đăng ký thất bại.")
            }

            val authData = response.data
            val studentUser = StudentUser(
                id = authData.user?.id ?: "",
                email = email,
                username = username,
                role = "student"
            )

            if (authData.token.isNotEmpty()) {
                dataStore.saveSession(
                    userId = studentUser.id,
                    email = studentUser.email,
                    username = studentUser.username,
                    accessToken = authData.token
                )
            }

            ResultWrapper.Success(studentUser)
        } catch (e: Exception) {
            ResultWrapper.Error(e.localizedMessage ?: "Đăng ký thất bại", e)
        }
    }

    override suspend fun signOut(): ResultWrapper<Unit> {
        return try {
            try {
                auth.signOut()
            } catch (_: Exception) { }
            dataStore.clearSession()
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            dataStore.clearSession()
            ResultWrapper.Error(e.localizedMessage ?: "Lỗi khi đăng xuất", e)
        }
    }

    override suspend fun getCurrentUser(): ResultWrapper<StudentUser?> {
        return try {
            val response = apiService.getCurrentUser()
            if (response.success && response.data != null) {
                val dto = response.data
                val student = StudentUser(
                    id = dto.id,
                    email = dto.email ?: "",
                    username = dto.username ?: "",
                    avatarUrl = dto.avatarUrl,
                    role = dto.role ?: "student"
                )
                ResultWrapper.Success(student)
            } else {
                ResultWrapper.Success(null)
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e.localizedMessage ?: "Lỗi tải thông tin người dùng", e)
        }
    }

    override fun getSessionFlow(): Flow<StudentUser?> {
        return dataStore.userSessionFlow.map { session ->
            if (session.isLoggedIn && !session.userId.isNullOrEmpty()) {
                StudentUser(
                    id = session.userId,
                    email = session.email ?: "",
                    username = session.username ?: "",
                    role = "student"
                )
            } else {
                null
            }
        }
    }
}
