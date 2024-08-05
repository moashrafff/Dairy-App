package com.moashraf.diaryapp.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moashraf.diaryapp.presentation.screens.write.components.WriteTopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen() {
    Scaffold(
        topBar = {
            WriteTopBar()
        },
        content = {

        }
    )
}