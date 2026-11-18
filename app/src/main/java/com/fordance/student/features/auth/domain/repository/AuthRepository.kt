package com.fordance.student.features.auth.domain.repository

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.features.auth.domain.model.StudentUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signIn(email: String, password: String): ResultWrapper<StudentUser>
    suspend fun signUp(email: String, password: String, username: String): ResultWrapper<StudentUser>
    suspend fun signOut(): ResultWrapper<Unit>
    suspend fun getCurrentUser(): ResultWrapper<StudentUser?>
    fun getSessionFlow(): Flow<StudentUser?>
}
