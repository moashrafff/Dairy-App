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
import com.moashraf.diaryapp.utils.toRealmInstant
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import java.time.ZonedDateTime


@RequiresApi(Build.VERSION_CODES.O)
class WriteViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    var uiState by mutableStateOf(UiState())
        private set

    init {
        getDiaryIdArgument()
        fetchSelectedDiary()
    }

    private fun getDiaryIdArgument() {
        val diaryId = savedStateHandle.get<String>(WRITE_SCREEN_ARGUMENT_KEY) ?: ""

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
    private fun fetchSelectedDiary() {
        if (uiState.selectedDiaryId != null) {
            viewModelScope.launch(Dispatchers.Main) {
                MongoDB.getSelectedDiary(org.mongodb.kbson.ObjectId(uiState.selectedDiaryId!!))
                    .collect { diary ->
                        if (diary is RequestState.Success) {
                            setSelectedDiary(diary.data)
                            setTitle(diary.data.title)
                            setDescription(diary.data.description)
                            setMood(Mood.valueOf(diary.data.mood))
                        }
                    }
            }
        }
    }

    private fun setSelectedDiary(diary: Diary) {
        uiState = uiState.copy(
            selectedDiary = diary
        )
    }

    fun setTitle(title: String) {
        uiState = uiState.copy(title = title)
    }

    fun setDescription(description: String) {
        uiState = uiState.copy(description = description)
    }

    private fun setMood(mood: Mood) {
        uiState = uiState.copy(mood = mood)
    }

    fun updateDateTime(zonedDateTime: ZonedDateTime) {
        uiState = uiState.copy(updatedDateTime = zonedDateTime.toInstant().toRealmInstant())
    }

    fun upsertDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.selectedDiaryId != null) {
                updateDiary(diary, onSuccess, onError)
            } else {
                insertDiary(diary, onSuccess, onError)
            }
        }
    }

    private suspend fun insertDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val result = MongoDB.addNewDiary(diary = diary.apply {
            if (uiState.updatedDateTime != null) {
                date = uiState.updatedDateTime!!
            }
        })
        if (result is RequestState.Success) {
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        } else {
            if (result is RequestState.Error) {
                onError(result.error.message.toString())
            }
        }
    }

    private suspend fun updateDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val result = MongoDB.updateDiary(diary = diary.apply {
            _id = ObjectId.invoke(uiState.selectedDiaryId!!)
            date = if (uiState.updatedDateTime != null) {
                uiState.updatedDateTime!!
            } else {
                uiState.selectedDiary!!.date
            }
        })
        if (result is RequestState.Success) {
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        } else {
            if (result is RequestState.Error) {
                onError(result.error.message.toString())
            }
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