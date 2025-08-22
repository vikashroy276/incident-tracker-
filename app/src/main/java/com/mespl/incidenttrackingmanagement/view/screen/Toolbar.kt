package com.mespl.incidenttrackingmanagement.view.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mespl.incidenttrackingmanagement.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    title: String = "Incident Tracking Management",
    showBackButton: Boolean = true,
    showImage: Boolean = true,
    navController: NavController,
    imageTint: Color = Color.White,
    onBackClick: () -> Unit = {},
    onImageClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showBackButton) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        actions = {
            if (showImage) { // 🔹 only show logout when true
                IconButton(onClick = onImageClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = "Logout",
                        tint = imageTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        modifier = Modifier.height(70.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.light_blue)
        )
    )
}



@Preview
@Composable
fun ToolbarPreview() = Toolbar(navController = NavController(LocalContext.current))

