package com.tcode.pixabayclient.di

import com.tcode.pixabayclient.utils.SystemTimeProvider
import com.tcode.pixabayclient.utils.TimerProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindTimeProvider(impl: SystemTimeProvider): TimerProvider
}
