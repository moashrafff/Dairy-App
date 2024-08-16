package com.moashraf.diaryapp.presentation.screens.write

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.moashraf.diaryapp.model.Diary
import com.moashraf.diaryapp.model.Mood
import com.moashraf.diaryapp.utils.Constants.WRITE_SCREEN_ARGUMENT_KEY
import io.realm.kotlin.types.RealmInstant


class WriteViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Initialize uiState with default values
    var uiState by mutableStateOf(UiState())
        private set

    init {
        getDiaryIdArgument()
    }

    // Function to retrieve Diary ID from SavedStateHandle and update the UI state
    private fun getDiaryIdArgument() {
        val diaryId = savedStateHandle.get<String>(WRITE_SCREEN_ARGUMENT_KEY) ?: ""

        // Check if the savedStateHandle returned a valid ID
        if (diaryId.isNotEmpty()) {
            uiState = uiState.copy(
                selectedDiaryId = diaryId
            )
        } else {
            Log.e("WriteViewModel", "No Diary ID found in SavedStateHandle")
        }
    }
}

data class UiState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral,
    val updatedDateTime: RealmInstant? = null
)