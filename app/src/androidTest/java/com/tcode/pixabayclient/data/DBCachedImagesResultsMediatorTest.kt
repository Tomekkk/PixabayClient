package com.tcode.pixabayclient.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tcode.pixabayclient.data.api.SearchResponse
import com.tcode.pixabayclient.data.db.ImageEntity
import com.tcode.pixabayclient.data.db.ImagesDatabase
import com.tcode.pixabayclient.data.mediator.CacheLifetime
import com.tcode.pixabayclient.data.mediator.DBCachedImagesResultsMediator
import com.tcode.pixabayclient.utils.TimerProvider
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class DBCachedImagesResultsMediatorTest {
    private val fakeImages = ImagesFactory.createDto(1..10)
    private val fakeDataSource = FakeImagesDataSource()
    private val fakeCacheLifetime =
        object : CacheLifetime {
            override val lifetimeMs: Long = 1L
        }
    private lateinit var mockDatabase: ImagesDatabase
    private val fakeTimerProvider =
        object : TimerProvider {
            override fun currentTimeMillis(): Long = 1
        }

    @Before
    fun setUp() {
        mockDatabase =
            Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().targetContext,
                ImagesDatabase::class.java,
            ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        mockDatabase.clearAllTables()
        fakeDataSource.tearDown()
    }

    @Test
    fun when_more_images_present_refresh_load_should_return_success_with_end_page_not_reached() =
        runTest {
            // Given
            fakeDataSource.setImages(fakeImages)
            val objectUnderTest =
                DBCachedImagesResultsMediator(
                    "q",
                    fakeDataSource,
                    mockDatabase,
                    mockDatabase.getImagesDao(),
                    mockDatabase.getRemoteKeysDao(),
                    fakeTimerProvider,
                    fakeCacheLifetime,
                )

            val pagingState =
                PagingState<Int, ImageEntity>(
                    listOf(),
                    null,
                    PagingConfig(10),
                    0,
                )
            // When
            val result = objectUnderTest.load(LoadType.REFRESH, pagingState)
            // Then
            assertTrue(result is RemoteMediator.MediatorResult.Success)
            assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        }

    @Test
    fun when_no_more_images_present_refresh_load_should_return_success_with_end_page_reached() =
        runTest {
            // Given
            fakeDataSource.setImages(emptyList())
            val objectUnderTest =
                DBCachedImagesResultsMediator(
                    "q",
                    fakeDataSource,
                    mockDatabase,
                    mockDatabase.getImagesDao(),
                    mockDatabase.getRemoteKeysDao(),
                    fakeTimerProvider,
                    fakeCacheLifetime,
                )

            val pagingState =
                PagingState<Int, ImageEntity>(
                    listOf(),
                    null,
                    PagingConfig(10),
                    0,
                )
            // When
            val result = objectUnderTest.load(LoadType.REFRESH, pagingState)
            // Then
            assertTrue(result is RemoteMediator.MediatorResult.Success)
            assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        }

    @Test
    fun when_refresh_load_throw_io_exception_should_return_error() =
        runTest {
            // Given
            fakeDataSource.exception = IOException()
            val objectUnderTest =
                DBCachedImagesResultsMediator(
                    "q",
                    fakeDataSource,
                    mockDatabase,
                    mockDatabase.getImagesDao(),
                    mockDatabase.getRemoteKeysDao(),
                    fakeTimerProvider,
                    fakeCacheLifetime,
                )

            val pagingState =
                PagingState<Int, ImageEntity>(
                    listOf(),
                    null,
                    PagingConfig(10),
                    0,
                )
            // When
            val result = objectUnderTest.load(LoadType.REFRESH, pagingState)
            // Then
            assertTrue(result is RemoteMediator.MediatorResult.Error)
        }

    @Test
    fun when_refresh_load_throw_400http_exception_with_body_should_return_error_with_readable_body() =
        runTest {
            // Given
            fakeDataSource.exception =
                HttpException(
                    Response.error<SearchResponse>(
                        400,
                        "[ERROR 400] \"page\" is out of valid range.".toResponseBody("text/plain".toMediaType()),
                    ),
                )
            val objectUnderTest =
                DBCachedImagesResultsMediator(
                    "q",
                    fakeDataSource,
                    mockDatabase,
                    mockDatabase.getImagesDao(),
                    mockDatabase.getRemoteKeysDao(),
                    fakeTimerProvider,
                    fakeCacheLifetime,
                )

            val pagingState =
                PagingState<Int, ImageEntity>(
                    listOf(),
                    null,
                    PagingConfig(10),
                    0,
                )
            // When
            val result = objectUnderTest.load(LoadType.REFRESH, pagingState)
            // Then
            assertTrue(result is RemoteMediator.MediatorResult.Error)
            assertEquals(
                "[ERROR 400] \"page\" is out of valid range.",
                (result as RemoteMediator.MediatorResult.Error).throwable.message,
            )
        }
}
