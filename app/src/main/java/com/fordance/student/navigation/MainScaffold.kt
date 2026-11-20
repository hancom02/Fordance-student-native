package com.fordance.student.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.fordance.student.core.theme.FordancePrimary
import com.fordance.student.core.theme.TextSecondary
import com.fordance.student.features.auth.presentation.AuthViewModel
import com.fordance.student.features.catalog.presentation.CatalogScreen
import com.fordance.student.features.discover.presentation.DiscoverScreen
import com.fordance.student.features.profile.presentation.ProfileScreen

enum class BottomTabDestination(val label: String, val icon: ImageVector) {
    DISCOVER("Discover", Icons.Default.Explore),
    CATALOG("Catalog", Icons.AutoMirrored.Filled.MenuBook),
    PROFILE("Profile", Icons.Default.Person)
}

@Composable
fun MainScaffold(
    authViewModel: AuthViewModel,
    onSignOut: () -> Unit,
    onNavigateToSessionDetail: (Long) -> Unit = {},
    onNavigateToClassDetail: (Long) -> Unit = {},
    onNavigateToInstructorDetail: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableStateOf(BottomTabDestination.DISCOVER) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                BottomTabDestination.entries.forEach { destination ->
                    val isSelected = selectedTab == destination
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = destination },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label
                            )
                        },
                        label = { Text(text = destination.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = FordancePrimary,
                            selectedTextColor = FordancePrimary,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = FordancePrimary.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                BottomTabDestination.DISCOVER -> {
                    DiscoverScreen(
                        onNavigateToSessionDetail = onNavigateToSessionDetail,
                        onNavigateToClassDetail = onNavigateToClassDetail,
                        onNavigateToSearch = { selectedTab = BottomTabDestination.CATALOG },
                        onNavigateToSeeAllJoined = { selectedTab = BottomTabDestination.CATALOG },
                        onNavigateToSeeAllClasses = { selectedTab = BottomTabDestination.CATALOG },
                        onNavigateToSeeAllFavourites = { selectedTab = BottomTabDestination.CATALOG }
                    )
                }
                BottomTabDestination.CATALOG -> {
                    CatalogScreen(
                        onNavigateToSessionDetail = onNavigateToSessionDetail,
                        onNavigateToClassDetail = onNavigateToClassDetail,
                        onNavigateToInstructorDetail = onNavigateToInstructorDetail
                    )
                }
                BottomTabDestination.PROFILE -> {
                    ProfileScreen(
                        authViewModel = authViewModel,
                        onSignOut = onSignOut
                    )
                }
            }
        }
    }
}
