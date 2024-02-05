package com.tcode.pixabayclient.data.di

import com.tcode.pixabayclient.data.mediator.CacheLifetime
import com.tcode.pixabayclient.data.mediator.DayCacheLifetime
import com.tcode.pixabayclient.data.mediator.DefaultImagesRemoteMediatorFactory
import com.tcode.pixabayclient.data.mediator.ImagesRemoteMediatorFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MediatorModule {
    @Binds
    abstract fun bindImagesRemoteMediatorFactory(
        imagesRemoteMediatorFactory: DefaultImagesRemoteMediatorFactory,
    ): ImagesRemoteMediatorFactory

    @Binds
    abstract fun bindCacheLifetime(dayCacheLifetime: DayCacheLifetime): CacheLifetime
}
