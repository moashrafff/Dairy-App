package com.moashraf.diaryapp.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.moashraf.diaryapp.R
import com.moashraf.diaryapp.data.repository.MongoDB
import com.moashraf.diaryapp.presentation.screens.Home.HomeScreen
import com.moashraf.diaryapp.presentation.screens.Home.components.DisplayAlertDialog
import com.moashraf.diaryapp.presentation.screens.authentication.AuthenticationScreen
import com.moashraf.diaryapp.presentation.screens.authentication.AuthenticationViewModel
import com.moashraf.diaryapp.utils.Constants.APP_ID
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetNavGraph(startDestination: String, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authenticationRoute(
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            }
        )
        homeRoute(
            navigateToWrite = {
                navController.navigate(Screen.Write.route)
            }, navigateToAuthentication = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route)
            }
        )
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit
) {
    composable(route = Screen.Authentication.route) {
        val context = LocalContext.current
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        val authenticatedState by viewModel.authenticatedState
        AuthenticationScreen(
            authenticationState = authenticatedState,
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
                        messageBarState.addSuccess(context.getString(R.string.successfully_authenticated))
                    },
                    onError = { exception ->
                        messageBarState.addError(Exception(exception))
                    }
                )
            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
            },
            navigateToHome = navigateToHome
        )
    }
}

fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit,
    navigateToAuthentication: () -> Unit
) {
    composable(route = Screen.Home.route) {
        var signOutDialogOpened by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        HomeScreen(
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            navigateToWrite = navigateToWrite,
            drawerState = drawerState,
            onSignOutClicked = {
                signOutDialogOpened = true
            }
        )
        LaunchedEffect(key1 = Unit) {
            MongoDB.configureRealm()
        }
        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure you want to Sign Out from your Google Account?",
            dialogOpened = signOutDialogOpened,
            onDialogClosed = { signOutDialogOpened = false },
            onYesClicked = {
                scope.launch(Dispatchers.IO) {
                    val user = App.create(APP_ID).currentUser
                    user?.logOut()
                        withContext(Dispatchers.Main) {
                            navigateToAuthentication()
                        }
                }
            }
        )
    }
}

fun NavGraphBuilder.writeRoute() {
    composable(route = Screen.Write.route) {
        // Add your composable content for write here
    }
}