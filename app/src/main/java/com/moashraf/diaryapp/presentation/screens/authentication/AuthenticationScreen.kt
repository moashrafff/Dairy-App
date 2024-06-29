package com.moashraf.diaryapp.presentation.screens.authentication

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    onClick: () -> Unit
) {
    Scaffold(
        content = {
            AuthenticationContent(
            loadingState = loadingState,
            onButtonClicked = onClick
        )
    })
}


