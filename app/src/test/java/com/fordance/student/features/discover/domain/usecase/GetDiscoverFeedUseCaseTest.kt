package com.fordance.student.features.discover.domain.usecase

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.domain.model.DanceClass
import com.fordance.student.core.domain.model.DanceLevel
import com.fordance.student.core.domain.model.Session
import com.fordance.student.features.discover.domain.repository.DiscoverRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetDiscoverFeedUseCaseTest {

    private class FakeDiscoverRepository(
        var featuredResult: ResultWrapper<List<Session>> = ResultWrapper.Success(emptyList()),
        var joinedResult: ResultWrapper<List<Session>> = ResultWrapper.Success(emptyList()),
        var classesResult: ResultWrapper<List<DanceClass>> = ResultWrapper.Success(emptyList()),
        var favResult: ResultWrapper<List<Session>> = ResultWrapper.Success(emptyList())
    ) : DiscoverRepository {
        override suspend fun getFeaturedSessions(): ResultWrapper<List<Session>> = featuredResult
        override suspend fun getJoinedSessions(userId: String): ResultWrapper<List<Session>> = joinedResult
        override suspend fun getClasses(): ResultWrapper<List<DanceClass>> = classesResult
        override suspend fun getFavouriteSessions(userId: String): ResultWrapper<List<Session>> = favResult
    }

    @Test
    fun testGetDiscoverFeedSuccess() = runTest {
        val sampleSession = Session(
            id = 1L,
            sessionName = "HipHop 101",
            level = DanceLevel.BEGINNER,
            genre = "hiphop",
            videoUrl = "https://example.com/video.mp4",
            thumbnailUrl = "https://example.com/thumb.jpg",
            duration = "30",
            classId = 1L,
            price = 10.0,
            instructorId = "ins-1",
            status = "approved"
        )
        val sampleClass = DanceClass(
            id = 1L,
            className = "Intro to HipHop",
            whatLearn = "Basic Grooves",
            whatPrepare = "Sneakers",
            level = DanceLevel.BEGINNER,
            genre = "hiphop",
            imageCoverUrl = "https://example.com/cover.jpg",
            instructorId = "ins-1",
            instructorUsername = "teacher_dan",
            introduce = "Start your dance journey",
            sessionCount = 4
        )

        val fakeRepo = FakeDiscoverRepository(
            featuredResult = ResultWrapper.Success(listOf(sampleSession)),
            joinedResult = ResultWrapper.Success(listOf(sampleSession)),
            classesResult = ResultWrapper.Success(listOf(sampleClass)),
            favResult = ResultWrapper.Success(listOf(sampleSession))
        )

        val useCase = GetDiscoverFeedUseCase(fakeRepo)
        val result = useCase("student-uuid")

        assertTrue(result is ResultWrapper.Success)
        val feed = (result as ResultWrapper.Success).data
        assertEquals(1, feed.featuredSessions.size)
        assertEquals(1, feed.joinedSessions.size)
        assertEquals(1, feed.classes.size)
        assertEquals(1, feed.favouriteSessions.size)
    }

    @Test
    fun testGetDiscoverFeedTotalFailure() = runTest {
        val fakeRepo = FakeDiscoverRepository(
            featuredResult = ResultWrapper.Error("Network error"),
            classesResult = ResultWrapper.Error("Network error")
        )

        val useCase = GetDiscoverFeedUseCase(fakeRepo)
        val result = useCase("student-uuid")

        assertTrue(result is ResultWrapper.Error)
    }
}
