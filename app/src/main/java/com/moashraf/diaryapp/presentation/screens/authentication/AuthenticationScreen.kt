package com.moashraf.diaryapp.presentation.screens.authentication

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.moashraf.diaryapp.utils.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    authenticationState: Boolean,
    oneTapSignInState: OneTapSignInState,
    messageBarState: MessageBarState,
    onClick: () -> Unit,
    onTokenIdReceived: (String) -> Unit,
    onDialogDismissed: (String) -> Unit,
    navigateToHome: () -> Unit
) {
    Scaffold(content = {
        ContentWithMessageBar(
            messageBarState = messageBarState
        ) {
            AuthenticationContent(
                loadingState = loadingState, onButtonClicked = onClick
            )
        }
    })

    OneTapSignInWithGoogle(
        clientId = CLIENT_ID,
        state = oneTapSignInState,
        onTokenIdReceived = { tokenId ->
            onTokenIdReceived(tokenId)
        },
        onDialogDismissed = { message ->
            onDialogDismissed(message)
        })

    LaunchedEffect(key1 = authenticationState) {
        if (authenticationState)
            navigateToHome.invoke()
    }
}


