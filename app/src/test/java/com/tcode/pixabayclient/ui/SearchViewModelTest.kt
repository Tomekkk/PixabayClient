package com.tcode.pixabayclient.ui

import androidx.lifecycle.SavedStateHandle
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import com.tcode.pixabayclient.data.ImagesRepository
import com.tcode.pixabayclient.data.QueriesRepository
import com.tcode.pixabayclient.data.db.ImageEntity
import com.tcode.pixabayclient.data.mediator.ImagesRemoteMediator
import com.tcode.pixabayclient.data.mediator.ImagesRemoteMediatorFactory
import com.tcode.pixabayclient.domain.GetSearchResultsUseCase
import com.tcode.pixabayclient.domain.GetStoredQueryResultsUseCase
import com.tcode.pixabayclient.domain.ImageResult
import com.tcode.pixabayclient.domain.PreviousQuery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when staring collecting queryStream should emit stored in state queries`() =
        runTest {
            // Given
            val objectUnderTest =
                SearchViewModel(
                    mockk(relaxed = true),
                    mockk(relaxed = true),
                    SavedStateHandle(mapOf("query" to "default")),
                )
            // When
            val result = objectUnderTest.queryStream.value
            // Then
            assertEquals("default", result)
        }

    @Test
    fun `when staring collecting queryStream fruits query should be emitted if there is no stored query in state`() =
        runTest {
            // Given
            val objectUnderTest =
                SearchViewModel(
                    mockk(relaxed = true),
                    mockk(relaxed = true),
                    SavedStateHandle(),
                )
            // When
            val result = objectUnderTest.queryStream.value
            // Then
            assertEquals("fruits", result)
        }

    @Test
    fun `when onQueryChanged action happen queryStream should store new value`() =
        runTest {
            // Given
            val objectUnderTest =
                SearchViewModel(
                    mockk(relaxed = true),
                    mockk(relaxed = true),
                    SavedStateHandle(mapOf("query" to "default")),
                )
            // When
            objectUnderTest.onQueryChanged("new query")
            val result = objectUnderTest.queryStream.value
            // Then
            assertEquals("new query", result)
        }

    @Test
    fun `when previous queries are store in database should emit them in getQueriesHistoryStream`() =
        runTest {
            // Given

            val fakeRepository =
                object : QueriesRepository {
                    override fun getStoredQueries(): Flow<List<String>> =
                        flow {
                            emit(listOf("query1", "query2"))
                        }
                }

            val objectUnderTest =
                SearchViewModel(
                    mockk(),
                    GetStoredQueryResultsUseCase(fakeRepository),
                    SavedStateHandle(),
                )
            // When
            // make sure that the repository flow is collected
            testScheduler.runCurrent()
            val results = objectUnderTest.queriesHistoryStream.value
            // Then
            assertEquals(
                listOf(
                    PreviousQuery(
                        query = "query1",
                    ),
                    PreviousQuery(
                        query = "query2",
                    ),
                ),
                results,
            )
        }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `when started collecting images stream should emit items for fruits query`() =
        runTest {
            // Given
            val fakeMediatorFactory =
                object : ImagesRemoteMediatorFactory {
                    override fun create(query: String): ImagesRemoteMediator {
                        return object : ImagesRemoteMediator(query) {
                            override suspend fun load(
                                loadType: LoadType,
                                state: PagingState<Int, ImageEntity>,
                            ): MediatorResult = MediatorResult.Success(endOfPaginationReached = true)
                        }
                    }
                }

            val mockedRepository =
                mockk<ImagesRepository> {
                    every { getPagingSource("fruits") } returns
                        listOf(
                            ImageEntity(
                                1,
                                1,
                                "fruitTags",
                                "previewURL",
                                1,
                                1,
                                "largeImageURL",
                                1,
                                1,
                                1,
                                1,
                                1,
                                "fruitsUser",
                                "fruits",
                            ),
                        ).asPagingSourceFactory().invoke()
                }
            val useCase = GetSearchResultsUseCase(mockedRepository, fakeMediatorFactory)

            val objectUnderTest =
                SearchViewModel(
                    useCase,
                    mockk(relaxed = true),
                    SavedStateHandle(),
                )
            // When
            val images = objectUnderTest.images.asSnapshot()
            // Then
            assertEquals(
                listOf(
                    ImageResult(
                        1,
                        1,
                        "fruitTags",
                        "previewURL",
                        1F,
                        "fruitsUser",
                    ),
                ),
                images,
            )
        }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `when user search query new images page should be emitted`() =
        runTest {
            // Given
            val fakeMediatorFactory =
                object : ImagesRemoteMediatorFactory {
                    override fun create(query: String): ImagesRemoteMediator {
                        return object : ImagesRemoteMediator(query) {
                            override suspend fun load(
                                loadType: LoadType,
                                state: PagingState<Int, ImageEntity>,
                            ): MediatorResult = MediatorResult.Success(endOfPaginationReached = true)
                        }
                    }
                }

            val mockedRepository =
                mockk<ImagesRepository>()
            every { mockedRepository.getPagingSource("fruits") } answers {
                listOf(
                    ImageEntity(
                        1,
                        1,
                        "fruitTags",
                        "previewURL",
                        1,
                        1,
                        "largeImageURL",
                        1,
                        1,
                        1,
                        1,
                        1,
                        "fruitsUser",
                        "fruits",
                    ),
                ).asPagingSourceFactory().invoke()
            }

            every { mockedRepository.getPagingSource("newquery") } answers {
                listOf(
                    ImageEntity(
                        1,
                        1,
                        "newTags",
                        "previewURL",
                        1,
                        1,
                        "largeImageURL",
                        1,
                        1,
                        1,
                        1,
                        1,
                        "newUser",
                        "newquery",
                    ),
                ).asPagingSourceFactory().invoke()
            }

            val useCase = GetSearchResultsUseCase(mockedRepository, fakeMediatorFactory)

            val objectUnderTest =
                SearchViewModel(
                    useCase,
                    mockk(relaxed = true),
                    SavedStateHandle(),
                )
            // When
            val initial = objectUnderTest.images.asSnapshot()
            objectUnderTest.onSearch("newquery")
            val searched = objectUnderTest.images.asSnapshot()
            // Then
            assertEquals(
                listOf(
                    ImageResult(
                        1,
                        1,
                        "fruitTags",
                        "previewURL",
                        1F,
                        "fruitsUser",
                    ),
                ),
                initial,
            )

            assertEquals(
                listOf(
                    ImageResult(
                        1,
                        1,
                        "newTags",
                        "previewURL",
                        1F,
                        "newUser",
                    ),
                ),
                searched,
            )
        }
}
