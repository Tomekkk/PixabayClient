package com.tcode.pixabayclient.di

import android.content.Context
import androidx.room.Room
import com.tcode.pixabayclient.data.db.ImagesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): ImagesDatabase {
        return Room.databaseBuilder(
            context,
            ImagesDatabase::class.java,
            "images_database",
        ).build()
    }
}
