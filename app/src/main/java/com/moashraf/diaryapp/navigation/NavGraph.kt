package com.moashraf.diaryapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.moashraf.diaryapp.presentation.screens.authentication.AuthenticationScreen
import com.moashraf.diaryapp.presentation.screens.authentication.AuthenticationViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState

@Composable
fun SetNavGraph(startDestination: String, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authenticationRoute()
        homeRoute()
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute() {
    composable(route = Screen.Authentication.route) {
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val viewModel : AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        AuthenticationScreen(
            loadingState = loadingState,
            oneTapSignInState = oneTapState,
            messageBarState = messageBarState,
            onClick = {
                oneTapState.open()
            },
            onTokenIdReceived = {
                viewModel.signInWithGoogle(
                    tokenID = it,
                    onSuccess = {
                        if (it)
                            messageBarState.addSuccess("SuccessFully Authenticated!")
                    },
                    onError = { exception ->
                        messageBarState.addError(Exception(exception))
                    }
                )
            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
            }
        )
    }
}

fun NavGraphBuilder.homeRoute() {
    composable(route = Screen.Home.route) {
        // Add your composable content for home here
    }
}

fun NavGraphBuilder.writeRoute() {
    composable(route = Screen.Write.route) {
        // Add your composable content for write here
    }
}