package com.mespl.incidenttrackingmanagement.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mespl.incidenttrackingmanagement.view.screen.CreateScreen
import com.mespl.incidenttrackingmanagement.view.screen.DashboardScreen
import com.mespl.incidenttrackingmanagement.view.screen.DetailsScreen
import com.mespl.incidenttrackingmanagement.view.screen.IncidenceScreen
import com.mespl.incidenttrackingmanagement.view.screen.LoginScreen
import com.mespl.incidenttrackingmanagement.view.screen.SplashScreen
import com.mespl.incidenttrackingmanagement.view.screen.UserScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = Screen.SplashScreen.route
    ) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.DashboardScreen.route) {
            DashboardScreen(navController = navController)
        }
        composable(Screen.CreateScreen.route) {
            CreateScreen(navController = navController)
        }

        composable(
            route = Screen.IncidenceScreen.route + "/{status}",
            arguments = listOf(navArgument("status") { type = NavType.StringType })
        ) { backStackEntry ->
            val status = backStackEntry.arguments?.getString("status") ?: ""
            IncidenceScreen(navController = navController, status = status)
        }
        composable(
            route = Screen.UserScreen.route + "/{incidence}/{description}",
            arguments = listOf(
                navArgument("incidence") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType })) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("incidence") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            UserScreen(navController = navController, name = name, description = description)
        }
        composable(
            route = "details/{id}/{incidence}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("incidence") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            val incidence = backStackEntry.arguments?.getString("incidence") ?: ""
            DetailsScreen(navController, id, incidence)
        }

    }
}
