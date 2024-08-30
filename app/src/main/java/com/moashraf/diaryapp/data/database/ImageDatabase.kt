package com.moashraf.diaryapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moashraf.diaryapp.data.database.entity.ImageToUpload

@Database(
    entities = [ImageToUpload::class],
    version = 1,
    exportSchema = false
)
abstract class ImageDatabase : RoomDatabase() {
    abstract fun imageToUploadDao(): ImageToUploadDao
}