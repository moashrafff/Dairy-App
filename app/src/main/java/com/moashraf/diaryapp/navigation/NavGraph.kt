package com.moashraf.diaryapp.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.moashraf.diaryapp.R
import com.moashraf.diaryapp.data.repository.MongoDB
import com.moashraf.diaryapp.model.RequestState
import com.moashraf.diaryapp.presentation.screens.Home.HomeScreen
import com.moashraf.diaryapp.presentation.screens.Home.HomeViewModel
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
import java.sql.Timestamp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetNavGraph(
    startDestination: String,
    navController: NavHostController,
    onDataLoaded : () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authenticationRoute(
            onDataLoaded = onDataLoaded,
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            }
        )
        homeRoute(
            onDataLoaded = onDataLoaded,
            navigateToWrite = {
                navController.navigate(Screen.Write.route)
            },
            navigateToAuthentication = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route)
            }
        )
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit,
    onDataLoaded: () -> Unit
) {
    composable(route = Screen.Authentication.route) {
        val context = LocalContext.current
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        val authenticatedState by viewModel.authenticatedState

        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }

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

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit,
    navigateToAuthentication: () -> Unit,
    onDataLoaded : () -> Unit
) {
    composable(route = Screen.Home.route) {
        val viewModel : HomeViewModel  = viewModel()
        val diaries by viewModel.diaries

        var signOutDialogOpened by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        LaunchedEffect(key1 = diaries) {
            if (diaries !is RequestState.Loading)
                onDataLoaded()

        }

        HomeScreen(
            diaries = diaries,
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

    }
}