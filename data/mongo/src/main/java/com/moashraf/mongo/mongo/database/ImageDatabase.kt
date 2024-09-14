package com.moashraf.mongo.mongo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moashraf.mongo.mongo.database.entity.ImageToDelete
import com.moashraf.mongo.mongo.database.entity.ImageToUpload

@Database(
    entities = [ImageToUpload::class, ImageToDelete::class],
    version = 2,
    exportSchema = false
)
abstract class ImageDatabase : RoomDatabase() {
    abstract fun imageToUploadDao(): ImageToUploadDao
    abstract fun imageToDeleteDao(): ImageToDeleteDao
}