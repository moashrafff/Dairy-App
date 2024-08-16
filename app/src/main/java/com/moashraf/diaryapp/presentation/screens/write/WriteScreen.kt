package com.moashraf.diaryapp.presentation.screens.write

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moashraf.diaryapp.model.Diary
import com.moashraf.diaryapp.presentation.screens.write.components.WriteContent
import com.moashraf.diaryapp.presentation.screens.write.components.WriteTopBar

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    selectedDiary: Diary?,
    pagerState: PagerState,
    onBackPressed: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
) {
    Scaffold(topBar = {
        WriteTopBar(
            selectedDiary = selectedDiary,
            onBackPressed = onBackPressed,
            onDeleteConfirmed = onDeleteConfirmed
        )
    }, content = { paddingValues ->
        WriteContent(
            pagerState = pagerState,
            title = "",
            onTitleChanged = onTitleChanged,
            description = "",
            onDescriptionChanged = onDescriptionChanged,
            paddingValues = paddingValues
        )
    })
}