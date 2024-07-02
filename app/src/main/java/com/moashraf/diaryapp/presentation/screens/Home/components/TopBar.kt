package com.moashraf.diaryapp.presentation.screens.Home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onMenuClicked: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onMenuClicked
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu Hamburger Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        title = {
            Text(text = "Diary")
        },
        actions = {
            IconButton(
                onClick = onMenuClicked
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date Range Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}