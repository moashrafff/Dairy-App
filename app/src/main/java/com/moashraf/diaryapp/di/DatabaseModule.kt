package com.moashraf.diaryapp.di

import android.content.Context
import androidx.room.Room
import com.moashraf.mongo.mongo.database.ImageDatabase
import com.moashraf.util.Constants.IMAGES_DATABASE
import com.moashraf.util.connectivity.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ImageDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = ImageDatabase::class.java,
            name = IMAGES_DATABASE
        ).build()
    }

    @Singleton
    @Provides
    fun provideFirstDao(database: ImageDatabase) = database.imageToUploadDao()

    @Singleton
    @Provides
    fun provideSecondDao(database: ImageDatabase) = database.imageToDeleteDao()

    @Singleton
    @Provides
    fun provideNetworkConnectivityObserver(
        @ApplicationContext context: Context
    ) = NetworkConnectivityObserver(context = context)
}