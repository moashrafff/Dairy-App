package com.moashraf.diaryapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.moashraf.diaryapp.navigation.Screen
import com.moashraf.diaryapp.navigation.SetNavGraph
import com.moashraf.diaryapp.ui.theme.DiaryAppTheme
import com.moashraf.diaryapp.utils.Constants.APP_ID
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App

@AndroidEntryPoint
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    private var keepSplashOpened = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{
            keepSplashOpened
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DiaryAppTheme {
                val navController = rememberNavController()
                SetNavGraph(
                    onDataLoaded = {
                        keepSplashOpened = false
                    },
                    startDestination = getStartDestination(),
                    navController = navController
                )
            }
        }
    }
    private fun getStartDestination():String{
        val user = App.create(APP_ID).currentUser
        return if (user != null && user.loggedIn) Screen.Home.route else Screen.Authentication.route
    }
}
