package com.moashraf.diaryapp.presentation.screens.Home

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moashraf.diaryapp.presentation.screens.Home.components.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    onMenuClicked: () -> Unit,
    navigateToWrite: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(onMenuClicked = onMenuClicked)
        },
        content = {

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToWrite
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Floating Action Button"
                )
            }
        }
    )
}