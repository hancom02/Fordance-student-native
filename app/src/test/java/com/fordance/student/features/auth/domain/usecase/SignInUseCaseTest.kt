package com.fordance.student.features.auth.domain.usecase

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.features.auth.domain.model.StudentUser
import com.fordance.student.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FakeAuthRepository : AuthRepository {
    var shouldSucceed: Boolean = true

    override suspend fun signIn(email: String, password: String): ResultWrapper<StudentUser> {
        return if (shouldSucceed) {
            ResultWrapper.Success(
                StudentUser(
                    id = "test-123",
                    email = email,
                    username = "student_test",
                    role = "student"
                )
            )
        } else {
            ResultWrapper.Error("Thông tin đăng nhập không hợp lệ")
        }
    }

    override suspend fun signUp(email: String, password: String, username: String): ResultWrapper<StudentUser> {
        return ResultWrapper.Success(StudentUser("new-123", email, username, role = "student"))
    }

    override suspend fun signOut(): ResultWrapper<Unit> = ResultWrapper.Success(Unit)

    override suspend fun getCurrentUser(): ResultWrapper<StudentUser?> = ResultWrapper.Success(null)

    override fun getSessionFlow(): Flow<StudentUser?> = flowOf(null)
}

class SignInUseCaseTest {
    @Test
    fun emptyEmailReturnsError() = runTest {
        val fakeRepo = FakeAuthRepository()
        val useCase = SignInUseCase(fakeRepo)

        val result = useCase("", "password123")
        assertTrue(result is ResultWrapper.Error)
        assertEquals("Email không được để trống", (result as ResultWrapper.Error).message)
    }

    @Test
    fun emptyPasswordReturnsError() = runTest {
        val fakeRepo = FakeAuthRepository()
        val useCase = SignInUseCase(fakeRepo)

        val result = useCase("test@example.com", "")
        assertTrue(result is ResultWrapper.Error)
        assertEquals("Mật khẩu không được để trống", (result as ResultWrapper.Error).message)
    }

    @Test
    fun validCredentialsCallsRepoSuccess() = runTest {
        val fakeRepo = FakeAuthRepository().apply { shouldSucceed = true }
        val useCase = SignInUseCase(fakeRepo)

        val result = useCase("student@fordance.com", "validpass")
        assertTrue(result is ResultWrapper.Success)
        assertEquals("student@fordance.com", (result as ResultWrapper.Success).data.email)
    }
}
