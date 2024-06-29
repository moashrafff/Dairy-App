package com.moashraf.diaryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.moashraf.diaryapp.presentation.screens.authentication.AuthenticationScreen

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
        AuthenticationScreen(
            loadingState = false,
            onClick = {}
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