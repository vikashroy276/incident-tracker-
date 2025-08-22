package com.mespl.incidenttrackingmanagement.view.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mespl.incidenttrackingmanagement.R
import com.mespl.incidenttrackingmanagement.datamodel.Incident
import com.mespl.incidenttrackingmanagement.datamodel.TaskList
import com.mespl.incidenttrackingmanagement.datamodel.Transection
import com.mespl.incidenttrackingmanagement.db.AppDatabase
import com.mespl.incidenttrackingmanagement.utils.SharedPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController, id: Int, incidence: String) {
    val tintColor = Color.White
    var remarks by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val incidentDao = db.incidentDao()
    val taskListDao = db.taskListDao()
    var incident by remember { mutableStateOf<Incident?>(null) }
    var task by remember { mutableStateOf<TaskList?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val statusOptions = listOf("OnHold", "Close")
    var selectedStatus by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val transectionDao = db.transectionDao()
    var transections by remember { mutableStateOf<List<Transection>>(emptyList()) }
    var isExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        withContext(Dispatchers.IO) {
            incident = incidentDao.getIncidentById(id)  // suspend function
            task = taskListDao.getTaskByIncidentId(id)  // suspend function
            transections = transectionDao.getTransectionsByIncidentId(id)
        }
    }

    Scaffold(
        topBar = {
            Toolbar(
                title = "Incident Details",
                navController = navController,
                showBackButton = true,
                imageTint = tintColor,
                showImage = false,
                onBackClick = { navController.popBackStack() },
                onImageClick = {})
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            incident?.let { inc ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Incident Name:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(inc.incidence, fontSize = 16.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Description:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(inc.description, fontSize = 16.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Assigned By:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(task?.assignedBy ?: "-", fontSize = 16.sp)
                }
            }

            // Show dropdown, remarks, and submit only if not closed
            if (incident?.status != "Close") {

                // Dropdown for Status
                ExposedDropdownMenuBox(
                    expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = selectedStatus ?: "Select Status",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                    )
                    ExposedDropdownMenu(
                        expanded = expanded, onDismissRequest = { expanded = false }) {
                        statusOptions.forEach { status ->
                            DropdownMenuItem(text = { Text(status) }, onClick = {
                                selectedStatus = status
                                expanded = false
                            })
                        }
                    }
                }

                OutlinedTextField(
                    value = remarks,
                    onValueChange = { remarks = it },
                    label = { Text("Remarks") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (selectedStatus.isNullOrEmpty() && remarks.isBlank()) {
                            errorMessage = "Please enter remarks"
                        } else {
                            errorMessage = null
                            CoroutineScope(Dispatchers.IO).launch {
                                val finalStatus = when {
                                    !selectedStatus.isNullOrEmpty() -> selectedStatus!!
                                    remarks.isNotBlank() -> "InProgress"
                                    else -> incident?.status ?: "InProgress"
                                }

                                // update incident table
                                incidentDao.updateStatusAndRemarks(id, finalStatus, remarks)

                                // update task_list table
                                taskListDao.updateTaskStatus(id, finalStatus)

                                // insert into transection table
                                val dateTime = SimpleDateFormat(
                                    "yyyy-MM-dd HH:mm:ss", Locale.getDefault()
                                ).format(Date())
                                val transection = Transection(
                                    incidentId = id,
                                    remarks = remarks,
                                    status = finalStatus,
                                    updatedBy = SharedPreference.get(context).userName,
                                    dateTime = dateTime
                                )
                                transectionDao.insertTransection(transection)

                                // refresh data
                                incident = incidentDao.getIncidentById(id)
                                transections = transectionDao.getTransectionsByIncidentId(id)

                                withContext(Dispatchers.Main) {
                                    showDialog = true
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(10),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.light_blue),
                        contentColor = Color.White
                    )
                ) {
                    Text("Submit")
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }, // toggle expand
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Activity",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand/Collapse"
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    items(transections) { transection ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF3A79F)
                            )
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Date: ${transection.dateTime}", fontWeight = FontWeight.SemiBold)
                                Text("Remarks: ${transection.remarks}")
                                Text("Status: ${transection.status}")
                                Text("Updated By: ${transection.updatedBy}")
                            }
                        }
                    }
                }
            }

            if (showDialog) {
                AlertDialog(onDismissRequest = { showDialog = false }, confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            navController.navigate("dashboard") {
                                popUpTo("dashboard") { inclusive = true }
                            }
                        }) {
                        Text("OK")
                    }
                }, title = { Text("Success") }, text = { Text("Update status successfully") })
            }
        }
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    val navController = NavController(LocalContext.current)
    DetailsScreen(navController, id = 1, incidence = "Fire")
}