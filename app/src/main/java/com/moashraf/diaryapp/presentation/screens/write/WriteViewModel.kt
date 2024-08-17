package com.moashraf.diaryapp.presentation.screens.write

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moashraf.diaryapp.data.repository.MongoDB
import com.moashraf.diaryapp.model.Diary
import com.moashraf.diaryapp.model.Mood
import com.moashraf.diaryapp.model.RequestState
import com.moashraf.diaryapp.utils.Constants.WRITE_SCREEN_ARGUMENT_KEY
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.BsonObjectId


@RequiresApi(Build.VERSION_CODES.O)
class WriteViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Initialize uiState with default values
    var uiState by mutableStateOf(UiState())
        private set

    init {
        getDiaryIdArgument()
        fetchSelectedDiary()
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

    // Function to retrieve Diary
    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchSelectedDiary(){
        if (uiState.selectedDiaryId != null){
            viewModelScope.launch(Dispatchers.Main) {
                val diary = MongoDB.getSelectedDiary(org.mongodb.kbson.ObjectId(uiState.selectedDiaryId!!))
                if (diary is RequestState.Success){
                        setTitle(diary.data.title)
                        setDescription(diary.data.description)
                        setMood(Mood.valueOf(diary.data.mood))
                }
            }
        }
    }

    fun setTitle(title: String){
        uiState = uiState.copy(title = title)
    }

    fun setDescription(description: String){
        uiState = uiState.copy(description = description)
    }

    fun setMood(mood: Mood){
        uiState = uiState.copy(mood = mood)
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