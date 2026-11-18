package com.fordance.student.features.auth.data.repository

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.datastore.UserPreferencesDataStore
import com.fordance.student.features.auth.data.remote.dto.UserDto
import com.fordance.student.features.auth.domain.model.StudentUser
import com.fordance.student.features.auth.domain.repository.AuthRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val dataStore: UserPreferencesDataStore
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): ResultWrapper<StudentUser> {
        return try {
            val users = postgrest.from("users")
                .select {
                    filter {
                        eq("email", email)
                        eq("role", "student")
                    }
                }
                .decodeList<UserDto>()

            if (users.isEmpty()) {
                return ResultWrapper.Error("Không tìm thấy tài khoản học viên với email này.")
            }

            val studentRecord = users.first()

            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val currentAuthUser = auth.currentUserOrNull()
            val session = auth.currentSessionOrNull()

            val studentUser = StudentUser(
                id = studentRecord.id.ifEmpty { currentAuthUser?.id ?: "" },
                email = email,
                username = studentRecord.username ?: email.substringBefore("@"),
                avatarUrl = studentRecord.avatarUrl,
                role = "student"
            )

            dataStore.saveSession(
                userId = studentUser.id,
                email = studentUser.email,
                username = studentUser.username,
                accessToken = session?.accessToken
            )

            ResultWrapper.Success(studentUser)
        } catch (e: Exception) {
            ResultWrapper.Error(e.localizedMessage ?: "Đăng nhập thất bại", e)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        username: String
    ): ResultWrapper<StudentUser> {
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("role", "student")
                    put("username", username)
                }
            }

            val createdUser = auth.currentUserOrNull()
            val userId = createdUser?.id ?: ""

            try {
                postgrest.from("users").insert(
                    UserDto(
                        id = userId,
                        email = email,
                        username = username,
                        role = "student"
                    )
                )
            } catch (_: Exception) {
                // Ignore if trigger already populated users table
            }

            val studentUser = StudentUser(
                id = userId,
                email = email,
                username = username,
                role = "student"
            )

            ResultWrapper.Success(studentUser)
        } catch (e: Exception) {
            ResultWrapper.Error(e.localizedMessage ?: "Đăng ký thất bại", e)
        }
    }

    override suspend fun signOut(): ResultWrapper<Unit> {
        return try {
            auth.signOut()
            dataStore.clearSession()
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            dataStore.clearSession()
            ResultWrapper.Error(e.localizedMessage ?: "Lỗi khi đăng xuất", e)
        }
    }

    override suspend fun getCurrentUser(): ResultWrapper<StudentUser?> {
        return try {
            val authUser = auth.currentUserOrNull()
            if (authUser == null) {
                ResultWrapper.Success(null)
            } else {
                val users = postgrest.from("users")
                    .select {
                        filter {
                            eq("id", authUser.id)
                        }
                    }
                    .decodeList<UserDto>()

                val record = users.firstOrNull()
                val student = StudentUser(
                    id = authUser.id,
                    email = authUser.email ?: "",
                    username = record?.username ?: authUser.email?.substringBefore("@") ?: "",
                    avatarUrl = record?.avatarUrl,
                    role = record?.role ?: "student"
                )
                ResultWrapper.Success(student)
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
