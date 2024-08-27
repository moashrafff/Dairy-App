package com.moashraf.diaryapp.presentation.screens.authentication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moashraf.diaryapp.utils.Constants.APP_ID
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel : ViewModel() {

    var loadingState = mutableStateOf(false)
        private set

    var authenticatedState = mutableStateOf(false)
        private set

    fun setLoadingState(state: Boolean) {
        loadingState.value = state
    }

    fun signInWithGoogle(
        tokenID: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                setLoadingState(true)
                val result = withContext(Dispatchers.IO) {
                    App.create(APP_ID)
//                        .login(Credentials.google(tokenID, GoogleAuthType.ID_TOKEN))
                        .login(Credentials.jwt(tokenID))
                        .loggedIn
                }
                withContext(Dispatchers.Main){
                    if (result){
                        onSuccess()
                        setLoadingState(false)
                        delay(600)
                        authenticatedState.value = true
                    }else{
                        onError("User is not logged in.")
                        setLoadingState(false)
                    }
                }
            } catch (e: Exception) {
                onError(e.message.orEmpty())
                setLoadingState(false)
            }
        }
    }
}