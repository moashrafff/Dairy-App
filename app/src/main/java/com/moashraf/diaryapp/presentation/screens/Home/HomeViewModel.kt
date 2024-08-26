package com.moashraf.diaryapp.presentation.screens.Home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moashraf.diaryapp.data.repository.Diaries
import com.moashraf.diaryapp.data.repository.MongoDB
import com.moashraf.diaryapp.model.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel : ViewModel() {

    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)

    init {
        observeAllDiaries()
    }

    private fun observeAllDiaries() {
        viewModelScope.launch {
            MongoDB.getAllDiaries().collect { result ->
                diaries.value = result
            }
        }
    }

}