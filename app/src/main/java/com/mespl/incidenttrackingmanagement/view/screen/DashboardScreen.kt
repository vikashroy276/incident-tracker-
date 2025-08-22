package com.mespl.incidenttrackingmanagement.view.screen

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.mespl.incidenttrackingmanagement.db.AppDatabase
import com.mespl.incidenttrackingmanagement.navigation.Screen
import com.mespl.incidenttrackingmanagement.utils.SharedPreference


@SuppressLint("ContextCastToActivity")
@Composable
fun DashboardScreen(navController: NavController) {
    val tintColor = Color.White
    val activity = (LocalContext.current as? Activity)
    val context = LocalContext.current

    val userName = SharedPreference.get(context).userName

    var showLogoutDialog by remember { mutableStateOf(false) }

    val taskDao = AppDatabase.getInstance(context).taskListDao()
    val totalTaskCount by taskDao.getTaskCountFlow(userName).collectAsState(initial = 0)
    val openTaskCount by taskDao.getTaskCountByStatusFlow(userName, "Open")
        .collectAsState(initial = 0)
    val inProgressTaskCount by taskDao.getTaskCountByStatusFlow(userName, "InProgress")
        .collectAsState(initial = 0)
    val onHoldTaskCount by taskDao.getTaskCountByStatusFlow(userName, "OnHold")
        .collectAsState(initial = 0)
    val closedTaskCount by taskDao.getTaskCountByStatusFlow(userName, "Close")
        .collectAsState(initial = 0)

    BackHandler {
        activity?.let { ActivityCompat.finishAffinity(it) }
    }

    Scaffold(
        topBar = {
            Toolbar(
                title = "Dashboard",
                navController = navController,
                showBackButton = true,
                imageTint = tintColor,
                onBackClick = {
                    activity?.let { ActivityCompat.finishAffinity(it) }
                },
                onImageClick = {
                    showLogoutDialog = true
                })
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.padding(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total Incidents", fontWeight = FontWeight.Bold, fontSize = 20.sp
                )
                Text(
                    text = totalTaskCount.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Blue
                )
            }

            Spacer(modifier = Modifier.padding(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardCard(
                        title = "Open",
                        count = openTaskCount,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            navController.navigate(Screen.IncidenceScreen.route + "/Open")
                        })
                    DashboardCard(
                        title = "In-Progress",
                        count = inProgressTaskCount,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            navController.navigate(Screen.IncidenceScreen.route + "/InProgress")
                        })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardCard(
                        title = "On-Hold",
                        count = onHoldTaskCount,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            navController.navigate(Screen.IncidenceScreen.route + "/OnHold")
                        })
                    DashboardCard(
                        title = "Closed",
                        count = closedTaskCount,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            navController.navigate(Screen.IncidenceScreen.route + "/Close")
                        })
                }

                Spacer(modifier = Modifier.padding(10.dp))
                RoundAddButton(navController)
            }
        }
    }
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(Screen.DashboardScreen.route) { inclusive = true }
                        }
                    }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            })
    }

}

@Composable
fun DashboardCard(
    title: String, count: Int, modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp) // Adjust card height
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)), // light blue background
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title, fontWeight = FontWeight.Medium, fontSize = 18.sp, color = Color.Black
            )
            Text(
                text = count.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = Color.Blue
            )

        }
    }
}


@Composable
fun RoundAddButton(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // margin from screen edges
    ) {
        FloatingActionButton(
            onClick = {
                navController.navigate("create") {
                    popUpTo("dashboard") { inclusive = true }
                }
            }, shape = CircleShape, modifier = Modifier.align(Alignment.BottomEnd) // bottom-right
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add button")
        }
    }
}


@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(navController = NavController(LocalContext.current))
}

