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
import com.tcode.pixabayclient.utils.TimerProvider
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class ImagesResultsMediatorTest {
    private val fakeImages = ImagesFactory.createDto(1..10)
    private val fakeDataSource = FakeImagesDataSource()
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
                ImagesResultsMediator(
                    "q",
                    fakeDataSource,
                    mockDatabase,
                    mockDatabase.getImages(),
                    mockDatabase.getRemoteKeys(),
                    fakeTimerProvider,
                    1,
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
                ImagesResultsMediator(
                    "q",
                    fakeDataSource,
                    mockDatabase,
                    mockDatabase.getImages(),
                    mockDatabase.getRemoteKeys(),
                    fakeTimerProvider,
                    1,
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
    fun when_refresh_load_throw_http400_should_return_success_with_end_page_reached() =
        runTest {
            // Given
            fakeDataSource.exception =
                HttpException(Response.error<SearchResponse>(400, "".toResponseBody(null)))
            val objectUnderTest =
                ImagesResultsMediator(
                    "q",
                    fakeDataSource,
                    mockDatabase,
                    mockDatabase.getImages(),
                    mockDatabase.getRemoteKeys(),
                    fakeTimerProvider,
                    1,
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
    fun when_refresh_load_throw_non_400_exception_should_return_error() =
        runTest {
            // Given
            fakeDataSource.exception =
                HttpException(Response.error<SearchResponse>(500, "".toResponseBody(null)))
            val objectUnderTest =
                ImagesResultsMediator(
                    "q",
                    fakeDataSource,
                    mockDatabase,
                    mockDatabase.getImages(),
                    mockDatabase.getRemoteKeys(),
                    fakeTimerProvider,
                    1,
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
}
