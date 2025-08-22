package com.mespl.incidenttrackingmanagement.navigation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash")
    object LoginScreen : Screen("login")
    object DashboardScreen : Screen("dashboard")
    object CreateScreen : Screen("create")
    object UserScreen : Screen("user")
    object IncidenceScreen : Screen("incidence")
    object DetailsScreen : Screen("details")

}