package com.tcode.pixabayclient.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ImageEntity::class, RemoteKey::class],
    version = 1,
)
abstract class ImagesDatabase : RoomDatabase() {
    abstract fun getImagesDao(): ImagesDao

    abstract fun getRemoteKeysDao(): RemoteKeysDao
}
