package com.tcode.pixabayclient.data.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.tcode.pixabayclient.BuildConfig
import com.tcode.pixabayclient.data.api.PixabayService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingInterceptorProvider

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://pixabay.com/api/"

    @LoggingInterceptorProvider
    @Provides
    fun providesLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor()
            .apply {
                if (BuildConfig.DEBUG) {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            }

    private const val CACHE_SIZE_BYTES = 10 * 1024 * 1024L

    @Provides
    @Singleton
    fun provideNetworkCache(
        @ApplicationContext appContext: Context,
    ) = Cache(appContext.cacheDir, CACHE_SIZE_BYTES)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @LoggingInterceptorProvider loggingInterceptor: Interceptor,
        cache: Cache,
    ) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .cache(cache)
        .build()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun providePixabayService(retrofit: Retrofit): PixabayService = retrofit.create(PixabayService::class.java)
}
