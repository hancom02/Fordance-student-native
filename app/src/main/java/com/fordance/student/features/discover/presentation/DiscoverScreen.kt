package com.fordance.student.features.discover.presentation

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fordance.student.core.components.ClassWiderCard
import com.fordance.student.core.components.FordanceErrorAlert
import com.fordance.student.core.components.FordancePrimaryButton
import com.fordance.student.core.components.SessionHorizontalCard
import com.fordance.student.core.theme.FordancePrimary
import com.fordance.student.core.theme.TextPrimary
import com.fordance.student.core.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    onNavigateToSessionDetail: (Long) -> Unit = {},
    onNavigateToClassDetail: (Long) -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToSeeAllJoined: () -> Unit = {},
    onNavigateToSeeAllClasses: () -> Unit = {},
    onNavigateToSeeAllFavourites: () -> Unit = {},
    viewModel: DiscoverViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.onEvent(DiscoverUiEvent.Refresh) },
        state = pullRefreshState,
        modifier = modifier.fillMaxSize()
    ) {
        if (uiState.isLoading && uiState.featuredSessions.isEmpty() && uiState.classes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = FordancePrimary)
            }
        } else if (!uiState.errorMessage.isNullOrEmpty() && uiState.featuredSessions.isEmpty()) {
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
                    onClick = { viewModel.onEvent(DiscoverUiEvent.Retry) },
                    modifier = Modifier.size(width = 160.dp, height = 48.dp)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 32.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FORDANCE",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = FordancePrimary,
                            letterSpacing = 1.5.sp
                        )
                    )
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Black
                        )
                    }
                }

                // Error banner if any non-blocking error occurs
                if (!uiState.errorMessage.isNullOrEmpty()) {
                    FordanceErrorAlert(
                        errorMessage = uiState.errorMessage,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }

                // Featured Sessions Carousel
                if (uiState.featuredSessions.isNotEmpty()) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        items(uiState.featuredSessions, key = { it.id }) { session ->
                            SessionHorizontalCard(
                                session = session,
                                onClick = { onNavigateToSessionDetail(session.id) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Joined Sessions Section
                SectionHeader(
                    title = "Joined Sessions",
                    onSeeAll = onNavigateToSeeAllJoined
                )
                if (uiState.joinedSessions.isNotEmpty()) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        items(uiState.joinedSessions, key = { it.id }) { session ->
                            SessionHorizontalCard(
                                session = session,
                                onClick = { onNavigateToSessionDetail(session.id) }
                            )
                        }
                    }
                } else {
                    Text(
                        text = "You haven't joined any sessions yet.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Classes Programs Section
                SectionHeader(
                    title = "Classes",
                    onSeeAll = onNavigateToSeeAllClasses
                )
                if (uiState.classes.isNotEmpty()) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        items(uiState.classes, key = { it.id }) { danceClass ->
                            ClassWiderCard(
                                danceClass = danceClass,
                                onClick = { onNavigateToClassDetail(danceClass.id) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Favourite Sessions Section
                SectionHeader(
                    title = "Favourite Sessions",
                    onSeeAll = onNavigateToSeeAllFavourites
                )
                if (uiState.favouriteSessions.isNotEmpty()) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        items(uiState.favouriteSessions, key = { it.id }) { session ->
                            SessionHorizontalCard(
                                session = session,
                                onClick = { onNavigateToSessionDetail(session.id) }
                            )
                        }
                    }
                } else {
                    Text(
                        text = "No favourite sessions bookmarked yet.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    onSeeAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                fontSize = 20.sp
            )
        )
        Text(
            text = "See All",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = FordancePrimary,
                fontSize = 14.sp
            ),
            modifier = Modifier.clickable(onClick = onSeeAll)
        )
    }
}
