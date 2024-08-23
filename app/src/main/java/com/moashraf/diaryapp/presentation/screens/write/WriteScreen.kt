package com.moashraf.diaryapp.presentation.screens.write

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.moashraf.diaryapp.model.Diary
import com.moashraf.diaryapp.model.Mood
import com.moashraf.diaryapp.presentation.screens.write.components.WriteContent
import com.moashraf.diaryapp.presentation.screens.write.components.WriteTopBar
import java.time.ZonedDateTime

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    uiState: UiState,
    pagerState: PagerState,
    moodName : () -> String,
    onBackPressed: () -> Unit,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onSaveClicked: (Diary) -> Unit,

    ) {
    LaunchedEffect(key1 = uiState.mood) {
        pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
    }
    Scaffold(topBar = {
        WriteTopBar(
            selectedDiary = uiState.selectedDiary,
            onBackPressed = onBackPressed,
            onDeleteConfirmed = onDeleteConfirmed,
            moodName = moodName,
            onDateTimeUpdated = onDateTimeUpdated
        )
    }, content = { paddingValues ->
        WriteContent(
            uiState = uiState,
            pagerState = pagerState,
            title = uiState.title,
            onTitleChanged = onTitleChanged,
            description = uiState.description,
            onDescriptionChanged = onDescriptionChanged,
            paddingValues = paddingValues,
            onSaveClicked = onSaveClicked
        )
    })
}