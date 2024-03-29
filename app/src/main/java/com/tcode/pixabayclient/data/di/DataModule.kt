package com.tcode.pixabayclient.data.di

import com.tcode.pixabayclient.data.CachedImagesRepository
import com.tcode.pixabayclient.data.CachedQueriesRepository
import com.tcode.pixabayclient.data.ImagesDataSource
import com.tcode.pixabayclient.data.ImagesRepository
import com.tcode.pixabayclient.data.QueriesRepository
import com.tcode.pixabayclient.data.RemoteImagesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindImagesDataSource(imagesDataSource: RemoteImagesDataSource): ImagesDataSource

    @Binds
    abstract fun bindImagesRepository(imagesRepository: CachedImagesRepository): ImagesRepository

    @Binds
    abstract fun bindQueriesRepository(queriesRepository: CachedQueriesRepository): QueriesRepository
}
