package com.fordance.student.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Main : Screen("main")

    data object SessionDetail : Screen("session_detail/{sessionId}") {
        fun createRoute(sessionId: Long): String = "session_detail/$sessionId"
    }

    data object ClassDetail : Screen("class_detail/{classId}") {
        fun createRoute(classId: Long): String = "class_detail/$classId"
    }

    data object InstructorDetail : Screen("instructor_detail/{instructorId}") {
        fun createRoute(instructorId: String): String = "instructor_detail/$instructorId"
    }
}
