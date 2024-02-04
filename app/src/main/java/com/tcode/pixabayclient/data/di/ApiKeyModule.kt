package com.tcode.pixabayclient.data.di

import com.tcode.pixabayclient.data.api.ApiKeyProvider
import com.tcode.pixabayclient.data.api.ConfigApiKeyProvider
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
