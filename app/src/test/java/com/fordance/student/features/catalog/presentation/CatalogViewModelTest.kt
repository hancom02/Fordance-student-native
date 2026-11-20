package com.fordance.student.features.catalog.presentation

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.domain.model.CatalogFilter
import com.fordance.student.core.domain.model.DanceClass
import com.fordance.student.core.domain.model.DanceLevel
import com.fordance.student.core.domain.model.Instructor
import com.fordance.student.core.domain.model.Session
import com.fordance.student.features.catalog.domain.repository.CatalogRepository
import com.fordance.student.features.catalog.domain.usecase.CatalogTab
import com.fordance.student.features.catalog.domain.usecase.SearchCatalogUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private class FakeCatalogRepository : CatalogRepository {
        var sessionsList = listOf(
            Session(
                id = 1L,
                sessionName = "HipHop 101",
                level = DanceLevel.BEGINNER,
                genre = "hiphop",
                videoUrl = "",
                thumbnailUrl = "",
                duration = "",
                classId = 1L,
                price = 0.0,
                instructorId = "1",
                status = "approved"
            )
        )
        var classesList = listOf(
            DanceClass(
                id = 1L,
                className = "Ballet Masterclass",
                whatLearn = "",
                whatPrepare = "",
                level = DanceLevel.ADVANCED,
                genre = "ballet",
                imageCoverUrl = "",
                instructorId = "2",
                instructorUsername = "maria",
                introduce = "",
                sessionCount = 5
            )
        )
        var instructorsList = listOf(
            Instructor(
                id = "1",
                name = "Dan",
                username = "dan_dance",
                avatarUrl = "",
                role = "instructor",
                active = true
            )
        )

        override suspend fun searchSessions(query: String?, filter: CatalogFilter?): ResultWrapper<List<Session>> =
            ResultWrapper.Success(sessionsList)

        override suspend fun searchClasses(query: String?, filter: CatalogFilter?): ResultWrapper<List<DanceClass>> =
            ResultWrapper.Success(classesList)

        override suspend fun searchInstructors(query: String?): ResultWrapper<List<Instructor>> =
            ResultWrapper.Success(instructorsList)

        override suspend fun getInstructors(): ResultWrapper<List<Instructor>> =
            ResultWrapper.Success(instructorsList)
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testTabSelectionSwitchesTabAndResetsFilter() = runTest {
        val repo = FakeCatalogRepository()
        val useCase = SearchCatalogUseCase(repo)
        val viewModel = CatalogViewModel(useCase, repo)

        advanceUntilIdle()
        assertEquals(CatalogTab.SESSIONS, viewModel.uiState.value.selectedTab)
        assertEquals(1, viewModel.uiState.value.sessions.size)

        viewModel.onEvent(CatalogUiEvent.TabSelected(CatalogTab.CLASSES))
        advanceUntilIdle()

        assertEquals(CatalogTab.CLASSES, viewModel.uiState.value.selectedTab)
        assertEquals(1, viewModel.uiState.value.classes.size)
    }

    @Test
    fun testFilterToggle() = runTest {
        val repo = FakeCatalogRepository()
        val useCase = SearchCatalogUseCase(repo)
        val viewModel = CatalogViewModel(useCase, repo)

        advanceUntilIdle()
        val filter = CatalogFilter(levels = setOf(DanceLevel.BEGINNER))
        viewModel.onEvent(CatalogUiEvent.FilterApplied(filter))
        advanceUntilIdle()

        assertEquals(setOf(DanceLevel.BEGINNER), viewModel.uiState.value.filter.levels)
    }
}
