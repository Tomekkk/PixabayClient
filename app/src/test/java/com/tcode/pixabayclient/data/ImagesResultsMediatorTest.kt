package com.tcode.pixabayclient.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.tcode.pixabayclient.data.db.ImagesDatabase
import com.tcode.pixabayclient.data.db.RemoteKeysDao
import com.tcode.pixabayclient.data.mediator.CacheLifetime
import com.tcode.pixabayclient.data.mediator.DBCachedImagesResultsMediator
import com.tcode.pixabayclient.utils.TimerProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ImagesResultsMediatorTest {
    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `when no creation date stored for query in remote keys should initialize with LAUNCH_INITIAL_REFRESH`() =
        runTest {
            // Given
            val database = mockk<ImagesDatabase>(relaxed = true)
            val imagesDao =
                mockk<RemoteKeysDao> {
                    coEvery { getOldestCreationTime("q") } returns null
                }
            val fakeTimeProvider =
                object : TimerProvider {
                    override fun currentTimeMillis(): Long = 1
                }
            val fakeCacheLifetime =
                object : CacheLifetime {
                    override val lifetimeMs: Long = 1
                }
            val objectUnderTest =
                DBCachedImagesResultsMediator(
                    "q",
                    mockk(),
                    database,
                    mockk(),
                    imagesDao,
                    fakeTimeProvider,
                    fakeCacheLifetime,
                )
            // When
            val result = objectUnderTest.initialize()
            // Then
            assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, result)
        }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `when creation date stored for query has exceed cache lifetime should initialize with LAUNCH_INITIAL_REFRESH`() =
        runTest {
            // Given
            val database = mockk<ImagesDatabase>(relaxed = true)
            val imagesDao =
                mockk<RemoteKeysDao> {
                    coEvery { getOldestCreationTime("q") } returns 1
                }
            val fakeTimeProvider =
                object : TimerProvider {
                    override fun currentTimeMillis(): Long = 2
                }
            val fakeCacheLifetime =
                object : CacheLifetime {
                    override val lifetimeMs: Long = 1
                }
            val objectUnderTest =
                DBCachedImagesResultsMediator(
                    "q",
                    mockk(),
                    database,
                    mockk(),
                    imagesDao,
                    fakeTimeProvider,
                    fakeCacheLifetime,
                )
            // When
            val result = objectUnderTest.initialize()
            // Then
            assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, result)
        }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `when creation date stored for query has not exceed cache lifetime should initialize with LAUNCH_INITIAL_REFRESH`() =
        runTest {
            // Given
            val database = mockk<ImagesDatabase>(relaxed = true)
            val imagesDao =
                mockk<RemoteKeysDao> {
                    coEvery { getOldestCreationTime("q") } returns 1
                }
            val fakeTimeProvider =
                object : TimerProvider {
                    override fun currentTimeMillis(): Long = 2
                }
            val fakeCacheLifetime =
                object : CacheLifetime {
                    override val lifetimeMs: Long = 2
                }
            val objectUnderTest =
                DBCachedImagesResultsMediator(
                    "q",
                    mockk(),
                    database,
                    mockk(),
                    imagesDao,
                    fakeTimeProvider,
                    fakeCacheLifetime,
                )
            // When
            val result = objectUnderTest.initialize()
            // Then
            assertEquals(RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH, result)
        }
}
