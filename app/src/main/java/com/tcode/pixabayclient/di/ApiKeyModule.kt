package com.tcode.pixabayclient.di

import com.tcode.pixabayclient.api.ApiKeyProvider
import com.tcode.pixabayclient.api.ConfigApiKeyProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiKeyModule {
    @Binds
    abstract fun bindApiKeyProvider(impl: ConfigApiKeyProvider): ApiKeyProvider
}
