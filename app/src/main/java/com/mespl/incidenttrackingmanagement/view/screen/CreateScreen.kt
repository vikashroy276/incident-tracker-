package com.mespl.incidenttrackingmanagement.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mespl.incidenttrackingmanagement.R

@Composable
fun CreateScreen(navController: NavController) {
    val tintColor = Color.White

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Toolbar(
                title = "Create",
                navController = navController,
                showBackButton = true,
                showImage = false,
                imageTint = tintColor,
                onBackClick = {
                    navController.navigate("dashboard") {
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(10.dp))

            // Name Input
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = false
                },
                label = { Text("Incidents Name") },
                isError = nameError,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
            if (nameError) {
                Text(
                    text = "Please fill the Name",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 12.dp, bottom = 4.dp)
                )
            }

            // Description Input
            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    descriptionError = false
                },
                label = { Text("Incidents Description") },
                isError = descriptionError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
            if (descriptionError) {
                Text(
                    text = "Please fill the Description",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 12.dp, bottom = 4.dp)
                )
            }

            Spacer(modifier = Modifier.padding(10.dp))

            // Button
            Button(
                onClick = {
                    if (name.isBlank()) {
                        nameError = true
                    }
                    if (description.isBlank()) {
                        descriptionError = true
                    }

                    if (name.isNotBlank() && description.isNotBlank()) {
                        navController.navigate("user/$name/$description") {
                            popUpTo("create") { inclusive = true }
                        }
                    }

                },
                shape = RoundedCornerShape(10),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.light_blue),
                    contentColor = Color.White
                ),
            ) {
                Text(text = "Assigned To")
            }
        }
    }
}


@Preview
@Composable
fun CreateScreenPreview() = CreateScreen(NavController(LocalContext.current))