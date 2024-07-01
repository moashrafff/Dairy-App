package com.moashraf.diaryapp.presentation.screens.authentication

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moashraf.diaryapp.utils.Constants.APP_ID
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Error

class AuthenticationViewModel : ViewModel() {

    var loadingState = mutableStateOf(false)
        private set

    private fun setLoadingState(state: Boolean) {
        loadingState.value = state
    }

    fun signInWithGoogle(
        tokenID: String,
        onSuccess: (Boolean) -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                setLoadingState(true)
                Log.e("TAG123", tokenID )
                val result = withContext(Dispatchers.IO) {
                    App.create(APP_ID)
//                        .login(Credentials.google(tokenID, GoogleAuthType.ID_TOKEN))
                        .login(Credentials.jwt(tokenID))
                        .loggedIn
                }
                withContext(Dispatchers.Main){
                    onSuccess(result)
                    setLoadingState(false)
                }
            } catch (e: Exception) {
                onError(e.message.orEmpty())
                setLoadingState(false)
            }
        }
    }
}