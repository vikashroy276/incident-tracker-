package com.mespl.incidenttrackingmanagement.view.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mespl.incidenttrackingmanagement.datamodel.Incident
import com.mespl.incidenttrackingmanagement.datamodel.TaskList
import com.mespl.incidenttrackingmanagement.datamodel.User
import com.mespl.incidenttrackingmanagement.db.AppDatabase
import com.mespl.incidenttrackingmanagement.utils.SharedPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UserScreen(navController: NavController, name: String, description: String) {
    val tintColor = Color.White
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val userDao = db.userDao()
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var showDialog by remember { mutableStateOf(false) } // 👈 for AlertDialog

    LaunchedEffect(Unit) {
        users = userDao.getAllUsers()
    }

    Scaffold(
        topBar = {
            Toolbar(
                title = "User",
                navController = navController,
                showBackButton = true,
                imageTint = tintColor,
                showImage = false,
                onBackClick = {
                    navController.navigate("create") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                },
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(users) { user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = user.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = user.email, fontSize = 14.sp, color = Color.Gray
                            )
                        }

                        RadioButton(
                            selected = selectedUser?.id == user.id,
                            onClick = { selectedUser = user })
                    }
                }
            }

            //submit button
            Button(
                onClick = {
                    if (selectedUser != null) {
                        val incident = Incident(
                            userName = selectedUser!!.name,
                            email = selectedUser!!.email,
                            incidence = name,
                            description = description,
                            date = SimpleDateFormat(
                                "yyyy-MM-dd", Locale.getDefault()
                            ).format(Date()),
                            createdBy = SharedPreference.get(context).userName,
                            status = "Open",
                            remarks = ""
                        )

                        val task = TaskList(
                            assignedBy = SharedPreference.get(context).userName,
                            task = name,
                            assignedTo = selectedUser!!.name,
                            createdDate = SimpleDateFormat(
                                "yyyy-MM-dd", Locale.getDefault()
                            ).format(Date()),
                            status = "Open"
                        )


                        CoroutineScope(Dispatchers.IO).launch {
                            db.incidentDao().insert(incident)
                            db.taskListDao().insertTask(task)
                        }

                        showDialog = true
                    } else {
                        Toast.makeText(context, "Please select a user", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue, contentColor = Color.White
                )
            ) {
                Text(text = "Submit")
            }
            //dialog box
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Incidents Saved") },
                    text = { Text("Your incidents has been saved successfully.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDialog = false
                                navController.navigate("dashboard") {
                                    popUpTo("user") { inclusive = true }
                                }
                            }) {
                            Text("OK")
                        }
                    })
            }
        }
    }
}







