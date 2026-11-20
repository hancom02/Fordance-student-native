package com.fordance.student.core.network

import com.fordance.student.core.data.dto.ClassDetailDto
import com.fordance.student.core.data.dto.ClassDto
import com.fordance.student.core.data.dto.InstructorDto
import com.fordance.student.core.data.dto.SessionDetailDto
import com.fordance.student.core.data.dto.SessionDto
import com.fordance.student.features.auth.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ForDanceApiService {

    // Auth
    @POST("api/v1/auth/login")
    suspend fun login(@Body body: LoginRequest): ApiResponse<AuthResponseData>

    @POST("api/v1/auth/register")
    suspend fun register(@Body body: RegisterRequest): ApiResponse<AuthResponseData>

    @GET("api/v1/auth/me")
    suspend fun getCurrentUser(): ApiResponse<UserDto>

    // Discover
    @GET("api/v1/discover/feeds")
    suspend fun getDiscoverFeeds(): ApiResponse<DiscoverFeedsResponseData>

    // Catalog
    @GET("api/v1/catalog/sessions")
    suspend fun getCatalogSessions(
        @Query("q") query: String? = null,
        @Query("level") level: String? = null,
        @Query("genre") genre: String? = null,
        @Query("instructor_id") instructorId: String? = null
    ): ApiResponse<List<SessionDto>>

    @GET("api/v1/catalog/classes")
    suspend fun getCatalogClasses(
        @Query("q") query: String? = null,
        @Query("level") level: String? = null,
        @Query("genre") genre: String? = null,
        @Query("instructor_id") instructorId: String? = null
    ): ApiResponse<List<ClassDto>>

    @GET("api/v1/catalog/instructors")
    suspend fun getCatalogInstructors(): ApiResponse<List<InstructorDto>>

    // Detail & Actions (Session & Class)
    @GET("api/v1/sessions/{id}")
    suspend fun getSessionDetail(@Path("id") sessionId: String): ApiResponse<SessionDetailDto>

    @POST("api/v1/sessions/{id}/join")
    suspend fun joinSession(@Path("id") sessionId: String): ApiResponse<Map<String, String>>

    @DELETE("api/v1/sessions/{id}/join")
    suspend fun leaveSession(@Path("id") sessionId: String): ApiResponse<Map<String, String>>

    @POST("api/v1/sessions/{id}/favorite")
    suspend fun favoriteSession(@Path("id") sessionId: String): ApiResponse<Map<String, String>>

    @DELETE("api/v1/sessions/{id}/favorite")
    suspend fun unfavoriteSession(@Path("id") sessionId: String): ApiResponse<Map<String, String>>

    @GET("api/v1/classes/{id}")
    suspend fun getClassDetail(@Path("id") classId: String): ApiResponse<ClassDetailDto>
}
