package com.fordance.student.features.detail

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.domain.model.ClassDetail
import com.fordance.student.core.domain.model.DanceGenre
import com.fordance.student.core.domain.model.DanceLevel
import com.fordance.student.core.domain.model.SessionDetail
import com.fordance.student.features.detail.domain.repository.DetailRepository
import com.fordance.student.features.detail.domain.usecase.GetClassDetailUseCase
import com.fordance.student.features.detail.domain.usecase.GetSessionDetailUseCase
import com.fordance.student.features.detail.domain.usecase.ToggleFavoriteSessionUseCase
import com.fordance.student.features.detail.domain.usecase.ToggleJoinSessionUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DetailUseCaseTest {

    private lateinit var mockRepository: FakeDetailRepository
    private lateinit var getSessionDetailUseCase: GetSessionDetailUseCase
    private lateinit var toggleJoinSessionUseCase: ToggleJoinSessionUseCase
    private lateinit var toggleFavoriteSessionUseCase: ToggleFavoriteSessionUseCase
    private lateinit var getClassDetailUseCase: GetClassDetailUseCase

    @Before
    fun setup() {
        mockRepository = FakeDetailRepository()
        getSessionDetailUseCase = GetSessionDetailUseCase(mockRepository)
        toggleJoinSessionUseCase = ToggleJoinSessionUseCase(mockRepository)
        toggleFavoriteSessionUseCase = ToggleFavoriteSessionUseCase(mockRepository)
        getClassDetailUseCase = GetClassDetailUseCase(mockRepository)
    }

    @Test
    fun testGetSessionDetailSuccess() = runTest {
        val result = getSessionDetailUseCase("101")
        assertTrue(result is ResultWrapper.Success)
        val data = (result as ResultWrapper.Success).data
        assertEquals("Test Session", data.title)
        assertEquals(DanceLevel.BEGINNER, data.level)
    }

    @Test
    fun testToggleJoinSession() = runTest {
        val joinResult = toggleJoinSessionUseCase("101", false)
        assertTrue(joinResult is ResultWrapper.Success)
        assertEquals(true, (joinResult as ResultWrapper.Success).data)

        val unjoinResult = toggleJoinSessionUseCase("101", true)
        assertTrue(unjoinResult is ResultWrapper.Success)
        assertEquals(false, (unjoinResult as ResultWrapper.Success).data)
    }

    @Test
    fun testToggleFavoriteSession() = runTest {
        val favResult = toggleFavoriteSessionUseCase("101", false)
        assertTrue(favResult is ResultWrapper.Success)
        assertEquals(true, (favResult as ResultWrapper.Success).data)
    }

    @Test
    fun testGetClassDetailSuccess() = runTest {
        val result = getClassDetailUseCase("202")
        assertTrue(result is ResultWrapper.Success)
        val data = (result as ResultWrapper.Success).data
        assertEquals("Test Class", data.className)
        assertEquals(3, data.totalSessions)
    }

    private class FakeDetailRepository : DetailRepository {
        override suspend fun getSessionDetail(sessionId: String): ResultWrapper<SessionDetail> {
            return ResultWrapper.Success(
                SessionDetail(
                    id = sessionId.toLongOrNull() ?: 1L,
                    title = "Test Session",
                    level = DanceLevel.BEGINNER,
                    genre = DanceGenre.HIPHOP,
                    videoUrl = "https://example.com/video.mp4",
                    thumbnailUrl = "https://example.com/thumb.jpg",
                    durationText = "45 phút",
                    classId = null,
                    price = 0.0,
                    description = "Description",
                    whatLearn = "Learn moves",
                    whatPrepare = "Shoes",
                    instructor = null,
                    danceClass = null,
                    isJoined = false,
                    isFavorited = false
                )
            )
        }

        override suspend fun toggleJoinSession(
            sessionId: String,
            currentlyJoined: Boolean
        ): ResultWrapper<Boolean> {
            return ResultWrapper.Success(!currentlyJoined)
        }

        override suspend fun toggleFavoriteSession(
            sessionId: String,
            currentlyFavorited: Boolean
        ): ResultWrapper<Boolean> {
            return ResultWrapper.Success(!currentlyFavorited)
        }

        override suspend fun getClassDetail(classId: String): ResultWrapper<ClassDetail> {
            return ResultWrapper.Success(
                ClassDetail(
                    id = classId.toLongOrNull() ?: 1L,
                    className = "Test Class",
                    level = DanceLevel.INTERMEDIATE,
                    genre = DanceGenre.BALLET,
                    imageCoverUrl = "https://example.com/cover.jpg",
                    instructor = null,
                    introduce = "Intro",
                    whatLearn = "Learn ballet",
                    whatPrepare = "Slippers",
                    sessions = emptyList(),
                    totalSessions = 3
                )
            )
        }
    }
}
