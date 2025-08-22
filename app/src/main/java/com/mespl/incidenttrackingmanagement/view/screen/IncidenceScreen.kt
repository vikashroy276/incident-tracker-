package com.mespl.incidenttrackingmanagement.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mespl.incidenttrackingmanagement.datamodel.Incident
import com.mespl.incidenttrackingmanagement.db.AppDatabase
import com.mespl.incidenttrackingmanagement.utils.SharedPreference

@Composable
fun IncidenceScreen(navController: NavController, status: String) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val incidentDao = db.incidentDao()
    val userName = SharedPreference.get(context).userName

    var incidences by remember { mutableStateOf(emptyList<Incident>()) }

    LaunchedEffect(status, userName) {
        incidences = incidentDao.getIncidentsByStatusAndUser(status, userName)
    }

    val screenTitle = "$status Incidents"

    Scaffold(
        topBar = {
            Toolbar(
                title = screenTitle,
                navController = navController,
                showBackButton = true,
                imageTint = Color.White,
                showImage = false,
                onBackClick = { navController.popBackStack() },
                onImageClick = {})
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            if (incidences.isEmpty()) {
                Text(
                    text = "No records found",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(incidences) { incident ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("details/${incident.incidenceId}/${incident.incidence}")
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF86C5E1))
                                    .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Incident Number",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(incident.incidenceId.toString(), color = Color.DarkGray)
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF86C5E1))
                                    .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Incident Name",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(incident.incidence, color = Color.DarkGray)
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF86C5E1))
                                    .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Created at:", fontWeight = FontWeight.Bold, color = Color.Black
                                )
                                Text(incident.date, color = Color.DarkGray)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun IncidenceScreenPreview() {
    val navController = NavController(LocalContext.current)
    IncidenceScreen(navController = navController, status = "Pending")
}

