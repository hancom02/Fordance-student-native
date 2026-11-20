package com.fordance.student.features.catalog.domain.usecase

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.domain.model.CatalogFilter
import com.fordance.student.core.domain.model.DanceClass
import com.fordance.student.core.domain.model.DanceLevel
import com.fordance.student.core.domain.model.Instructor
import com.fordance.student.core.domain.model.Session
import com.fordance.student.features.catalog.domain.repository.CatalogRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchCatalogUseCaseTest {

    private class FakeCatalogRepository(
        var sessionsResult: ResultWrapper<List<Session>> = ResultWrapper.Success(emptyList()),
        var classesResult: ResultWrapper<List<DanceClass>> = ResultWrapper.Success(emptyList()),
        var instructorsResult: ResultWrapper<List<Instructor>> = ResultWrapper.Success(emptyList())
    ) : CatalogRepository {
        override suspend fun searchSessions(
            query: String?,
            filter: CatalogFilter?
        ): ResultWrapper<List<Session>> = sessionsResult

        override suspend fun searchClasses(
            query: String?,
            filter: CatalogFilter?
        ): ResultWrapper<List<DanceClass>> = classesResult

        override suspend fun searchInstructors(query: String?): ResultWrapper<List<Instructor>> = instructorsResult

        override suspend fun getInstructors(): ResultWrapper<List<Instructor>> = instructorsResult
    }

    @Test
    fun testSearchSessionsTab() = runTest {
        val sampleSession = Session(
            id = 1L,
            sessionName = "Waacking Level 1",
            level = DanceLevel.BEGINNER,
            genre = "wacking",
            videoUrl = "https://example.com/v.mp4",
            thumbnailUrl = "https://example.com/t.jpg",
            duration = "25",
            classId = 1L,
            price = 20.0,
            instructorId = "ins-1",
            status = "approved"
        )
        val fakeRepo = FakeCatalogRepository(
            sessionsResult = ResultWrapper.Success(listOf(sampleSession))
        )

        val useCase = SearchCatalogUseCase(fakeRepo)
        val result = useCase(CatalogTab.SESSIONS, "Waacking", null)

        assertTrue(result is ResultWrapper.Success)
        val data = (result as ResultWrapper.Success).data
        assertTrue(data is CatalogResult.SessionsResult)
        assertEquals(1, (data as CatalogResult.SessionsResult).sessions.size)
        assertEquals("Waacking Level 1", data.sessions[0].sessionName)
    }

    @Test
    fun testSearchClassesTab() = runTest {
        val sampleClass = DanceClass(
            id = 2L,
            className = "Locking Choreo",
            whatLearn = "Locking basics",
            whatPrepare = "Towel",
            level = DanceLevel.INTERMEDIATE,
            genre = "locking",
            imageCoverUrl = "https://example.com/c.jpg",
            instructorId = "ins-2",
            instructorUsername = "teacher_lock",
            introduce = "Fun locking course",
            sessionCount = 6
        )
        val fakeRepo = FakeCatalogRepository(
            classesResult = ResultWrapper.Success(listOf(sampleClass))
        )

        val useCase = SearchCatalogUseCase(fakeRepo)
        val result = useCase(CatalogTab.CLASSES, null, null)

        assertTrue(result is ResultWrapper.Success)
        val data = (result as ResultWrapper.Success).data
        assertTrue(data is CatalogResult.ClassesResult)
        assertEquals(1, (data as CatalogResult.ClassesResult).classes.size)
    }

    @Test
    fun testSearchInstructorsTab() = runTest {
        val sampleInstructor = Instructor(
            id = "ins-3",
            name = "Sarah Connor",
            username = "sarah_c",
            avatarUrl = "https://example.com/a.jpg",
            role = "instructor",
            active = true
        )
        val fakeRepo = FakeCatalogRepository(
            instructorsResult = ResultWrapper.Success(listOf(sampleInstructor))
        )

        val useCase = SearchCatalogUseCase(fakeRepo)
        val result = useCase(CatalogTab.INSTRUCTORS, "Sarah", null)

        assertTrue(result is ResultWrapper.Success)
        val data = (result as ResultWrapper.Success).data
        assertTrue(data is CatalogResult.InstructorsResult)
        assertEquals(1, (data as CatalogResult.InstructorsResult).instructors.size)
    }
}
