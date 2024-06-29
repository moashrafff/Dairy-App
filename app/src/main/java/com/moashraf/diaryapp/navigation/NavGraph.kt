package com.moashraf.diaryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.createGraph

@Composable
fun SetNavGraph(startDestination: String, navController: NavHostController) {
    val navGraph = navController.createGraph(
        startDestination = startDestination
    ) {
        authenticationRoute()
        homeRoute()
        writeRoute()
    }
    navController.graph = navGraph
}

fun NavGraphBuilder.authenticationRoute() {
    composable(route = Screen.Authentication.route) {
        // Add your composable content for home here
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