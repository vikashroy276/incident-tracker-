package com.mespl.incidenttrackingmanagement.view.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mespl.incidenttrackingmanagement.datamodel.User
import com.mespl.incidenttrackingmanagement.db.AppDatabase
import kotlinx.coroutines.delay


/**
 * Composable function for the splash screen.
 *
 * This screen displays an animated logo and requests necessary permissions before
 * navigating to the login screen.
 *
 * @param navController The navigation controller to handle screen transitions.
 */
@Composable
fun SplashScreen(navController: NavController) {

    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val userDao = db.userDao()

    LaunchedEffect(Unit) {
        val existingUsers = userDao.getAllUsers()  // <-- You need a DAO method for this
        if (existingUsers.isEmpty()) {
            val users = listOf(
                User(name = "abhay", email = "Abhay@example.com", password = "12345"),
                User(name = "vishal", email = "Vishal@example.com", password = "00000"),
                User(name = "nitin", email = "Nitin@example.com", password = "11111"),
                User(name = "vikash", email = "Vikash@example.com", password = "12222"),
                User(name = "ashiwani", email = "Ashiwani@example.com", password = "22222"),
                User(name = "ravi", email = "Ravi@example.com", password = "33333"),
                User(name = "sunil", email = "Sunil@example.com", password = "44444"),
                User(name = "govind", email = "Govind@example.com", password = "55555"),
                User(name = "vandana", email = "Vandana@example.com", password = "66666"),
                User(name = "naresh", email = "Naresh@example.com", password = "77777")
            )
            userDao.insertAll(users)
            Log.d("DB insert ", "Inserted default users ")
        } else {
            Log.d("DB inserted already", "Users already exist, skipping insert ")
        }
    }

    LaunchedEffect(key1 = true) {

        delay(1000)

        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2AC8DA)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Incident Tracking Management", color = Color.Black, fontSize = 16.sp
            )
        }

    }
}


@Preview
@Composable
fun SplashScreenPreview() = SplashScreen(NavController(LocalContext.current))