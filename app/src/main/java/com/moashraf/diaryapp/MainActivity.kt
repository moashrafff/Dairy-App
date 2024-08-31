package com.moashraf.diaryapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.initialize
import com.moashraf.diaryapp.data.database.ImageToDeleteDao
import com.moashraf.diaryapp.data.database.ImageToUploadDao
import com.moashraf.diaryapp.navigation.Screen
import com.moashraf.diaryapp.navigation.SetNavGraph
import com.moashraf.diaryapp.ui.theme.DiaryAppTheme
import com.moashraf.diaryapp.utils.Constants.APP_ID
import com.moashraf.diaryapp.utils.retryDeletingImageFromFirebase
import com.moashraf.diaryapp.utils.retryUploadingImageToFirebase
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var imageToUploadDao: ImageToUploadDao
    @Inject
    lateinit var imageToDeleteDao: ImageToDeleteDao
    private var keepSplashOpened = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{
            keepSplashOpened
        }
        FirebaseApp.initializeApp(this)
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
        cleanupCheck(
            scope = lifecycleScope,
            imageToUploadDao = imageToUploadDao,
//            imageToDeleteDao = imageToDeleteDao
        )
    }
    private fun cleanupCheck(
        scope: CoroutineScope,
        imageToUploadDao: ImageToUploadDao,
//        imageToDeleteDao: ImageToDeleteDao
    ) {
        scope.launch(Dispatchers.IO) {
            val result = imageToUploadDao.getAllImages()
            result.forEach { imageToUpload ->
                retryUploadingImageToFirebase(
                    imageToUpload = imageToUpload,
                    onSuccess = {
                        scope.launch(Dispatchers.IO) {
                            imageToUploadDao.cleanupImage(imageId = imageToUpload.id)
                        }
                    }
                )
            }
            val result2 = imageToDeleteDao.getAllImages()
            result2.forEach { imageToDelete ->
                retryDeletingImageFromFirebase(
                    imageToDelete = imageToDelete,
                    onSuccess = {
                        scope.launch(Dispatchers.IO) {
                            imageToDeleteDao.cleanupImage(imageId = imageToDelete.id)
                        }
                    }
                )
            }
        }
    }
    private fun getStartDestination():String{
        val user = App.create(APP_ID).currentUser
        return if (user != null && user.loggedIn) Screen.Home.route else Screen.Authentication.route
    }
}
