package com.fordance.student.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fordance.student.core.components.FordancePrimaryButton
import com.fordance.student.core.theme.FordancePrimary
import com.fordance.student.features.auth.presentation.AuthViewModel
import com.fordance.student.features.auth.presentation.LoginScreen
import com.fordance.student.features.auth.presentation.RegisterScreen
import com.fordance.student.features.detail.presentation.courseclass.ClassDetailScreen
import com.fordance.student.features.detail.presentation.session.SessionDetailScreen

@Composable
fun ForDanceNavGraph(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = FordancePrimary)
            }

            LaunchedEffect(uiState.isSessionChecked) {
                if (uiState.isSessionChecked) {
                    if (uiState.currentUser != null) {
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            }
        }

        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Main.route) {
            MainScaffold(
                authViewModel = authViewModel,
                onSignOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                onNavigateToSessionDetail = { sessionId ->
                    navController.navigate(Screen.SessionDetail.createRoute(sessionId))
                },
                onNavigateToClassDetail = { classId ->
                    navController.navigate(Screen.ClassDetail.createRoute(classId))
                },
                onNavigateToInstructorDetail = { instructorId ->
                    navController.navigate(Screen.InstructorDetail.createRoute(instructorId))
                }
            )
        }

        composable(
            route = Screen.SessionDetail.route,
            arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
        ) {
            SessionDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ClassDetail.route,
            arguments = listOf(navArgument("classId") { type = NavType.StringType })
        ) {
            ClassDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSession = { sessionId ->
                    navController.navigate(Screen.SessionDetail.createRoute(sessionId.toLongOrNull() ?: 0L))
                }
            )
        }

        composable(
            route = Screen.InstructorDetail.route,
            arguments = listOf(navArgument("instructorId") { type = NavType.StringType })
        ) { backStackEntry ->
            val instructorId = backStackEntry.arguments?.getString("instructorId").orEmpty()
            PlaceholderDetailScreen(
                title = "Instructor Profile",
                detail = "Instructor ID: $instructorId\n(Instructor bio & courses coming in Change 4)",
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun PlaceholderDetailScreen(
    title: String,
    detail: String,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = detail,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(32.dp))
        FordancePrimaryButton(
            text = "Back",
            onClick = onBack,
            modifier = Modifier.height(48.dp)
        )
    }
}
