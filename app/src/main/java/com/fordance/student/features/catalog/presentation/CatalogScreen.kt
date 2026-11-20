package com.fordance.student.features.catalog.presentation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fordance.student.core.components.ClassCard
import com.fordance.student.core.components.FordanceErrorAlert
import com.fordance.student.core.components.FordancePrimaryButton
import com.fordance.student.core.components.InstructorCard
import com.fordance.student.core.components.SessionCard
import com.fordance.student.core.components.TagChip
import com.fordance.student.core.theme.FordancePrimary
import com.fordance.student.core.theme.TextPrimary
import com.fordance.student.core.theme.TextSecondary
import com.fordance.student.features.catalog.domain.usecase.CatalogTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onNavigateToSessionDetail: (Long) -> Unit = {},
    onNavigateToClassDetail: (Long) -> Unit = {},
    onNavigateToInstructorDetail: (String) -> Unit = {},
    viewModel: CatalogViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        // Title
        Text(
            text = "HOME",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary,
                fontSize = 24.sp
            ),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        // Tab Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CatalogTab.entries.forEach { tab ->
                val isSelected = uiState.selectedTab == tab
                TagChip(
                    text = tab.name,
                    selected = isSelected,
                    onClick = { viewModel.onEvent(CatalogUiEvent.TabSelected(tab)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Search Input Bar
        SearchInputBar(
            query = uiState.searchQuery,
            onQueryChange = { query -> viewModel.onEvent(CatalogUiEvent.SearchQueryChanged(query)) },
            placeholder = when (uiState.selectedTab) {
                CatalogTab.SESSIONS -> "Search sessions..."
                CatalogTab.CLASSES -> "Search classes..."
                CatalogTab.INSTRUCTORS -> "Search instructors..."
            },
            onFilterClick = if (uiState.selectedTab != CatalogTab.INSTRUCTORS) {
                { viewModel.onEvent(CatalogUiEvent.FilterSheetToggled) }
            } else null,
            hasActiveFilter = !uiState.filter.isEmpty
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Content Area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = FordancePrimary)
                }
            } else if (!uiState.errorMessage.isNullOrEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    FordanceErrorAlert(errorMessage = uiState.errorMessage)
                    Spacer(modifier = Modifier.height(16.dp))
                    FordancePrimaryButton(
                        text = "Retry",
                        onClick = { viewModel.onEvent(CatalogUiEvent.Retry) },
                        modifier = Modifier.size(width = 160.dp, height = 48.dp)
                    )
                }
            } else {
                when (uiState.selectedTab) {
                    CatalogTab.SESSIONS -> {
                        if (uiState.sessions.isEmpty()) {
                            EmptyResultsPlaceholder(message = "No sessions found matching your search or filters.")
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 32.dp)
                            ) {
                                items(uiState.sessions, key = { it.id }) { session ->
                                    SessionCard(
                                        session = session,
                                        onClick = { onNavigateToSessionDetail(session.id) }
                                    )
                                }
                            }
                        }
                    }
                    CatalogTab.CLASSES -> {
                        if (uiState.classes.isEmpty()) {
                            EmptyResultsPlaceholder(message = "No dance classes found matching your search or filters.")
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 32.dp)
                            ) {
                                items(uiState.classes, key = { it.id }) { danceClass ->
                                    ClassCard(
                                        danceClass = danceClass,
                                        onClick = { onNavigateToClassDetail(danceClass.id) }
                                    )
                                }
                            }
                        }
                    }
                    CatalogTab.INSTRUCTORS -> {
                        if (uiState.instructors.isEmpty()) {
                            EmptyResultsPlaceholder(message = "No instructors found.")
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 32.dp)
                            ) {
                                items(uiState.instructors, key = { it.id }) { instructor ->
                                    InstructorCard(
                                        instructor = instructor,
                                        onClick = { onNavigateToInstructorDetail(instructor.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Filter Bottom Sheet
    if (uiState.isFilterSheetOpen) {
        FilterBottomSheet(
            initialFilter = uiState.filter,
            instructors = uiState.availableInstructors,
            onDismissRequest = { viewModel.onEvent(CatalogUiEvent.FilterSheetToggled) },
            onApplyFilter = { newFilter -> viewModel.onEvent(CatalogUiEvent.FilterApplied(newFilter)) }
        )
    }
}

@Composable
private fun EmptyResultsPlaceholder(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge.copy(color = TextSecondary),
            modifier = Modifier.padding(32.dp)
        )
    }
}
