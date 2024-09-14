package com.moashraf.diaryapp.presentation.screens.authentication

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.moashraf.util.Constants.CLIENT_ID
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
    onSuccessfulFirebaseSignIn: (String) -> Unit,
    onFailedFirebaseSignIn: (Exception) -> Unit,
    onDialogDismissed: (String) -> Unit,
    navigateToHome: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding(),
        content = {
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
            val credentials = GoogleAuthProvider.getCredential(tokenId,null)
            FirebaseAuth.getInstance().signInWithCredential(credentials)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        onSuccessfulFirebaseSignIn(tokenId)
                    else
                        task.exception?.let { onFailedFirebaseSignIn(it) }
                }
        },
        onDialogDismissed = { message ->
            onDialogDismissed(message)
        })

    LaunchedEffect(key1 = authenticationState) {
        if (authenticationState)
            navigateToHome.invoke()
    }
}


