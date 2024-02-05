package com.tcode.pixabayclient.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import com.tcode.pixabayclient.data.ImagesRepository
import com.tcode.pixabayclient.data.db.ImageEntity
import com.tcode.pixabayclient.data.mediator.ImagesRemoteMediator
import com.tcode.pixabayclient.data.mediator.ImagesRemoteMediatorFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GetSearchResultsUseCaseTest {
    @ExperimentalPagingApi
    @Test
    fun `should return images results mapped from repository's entities`() =
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

            val fakeImagesRepository =
                object : ImagesRepository {
                    override fun getPagingSource(query: String): PagingSource<Int, ImageEntity> =
                        listOf(
                            ImageEntity(
                                1,
                                1,
                                "tags",
                                "previewURL",
                                1,
                                1,
                                "largeImageURL",
                                1,
                                1,
                                1,
                                1,
                                1,
                                "user",
                                "q",
                            ),
                        ).asPagingSourceFactory().invoke()

                    override suspend fun getImage(id: Long): ImageEntity? = null
                }
            val useCase = GetSearchResultsUseCase(fakeImagesRepository, fakeMediatorFactory)
            // When
            val result = useCase("q")
            // Then
            assertEquals(
                listOf(
                    ImageResult(
                        1,
                        1,
                        "tags",
                        "previewURL",
                        1F,
                        "user",
                    ),
                ),
                result.asSnapshot(),
            )
        }

    @ExperimentalPagingApi
    @Test
    fun `when query has whitespaces and capital letters should trim and lowercase query`() =
        runTest {
            // Given
            val fakeRemoteMediator =
                object : ImagesRemoteMediator("") {
                    override suspend fun load(
                        loadType: LoadType,
                        state: PagingState<Int, ImageEntity>,
                    ): MediatorResult = MediatorResult.Success(endOfPaginationReached = true)
                }
            val mockedMediatorFactory =
                mockk<ImagesRemoteMediatorFactory> {
                    every { create(any()) } returns fakeRemoteMediator
                }

            val mockedImagesRepository =
                mockk<ImagesRepository> {
                    every { getPagingSource(any()) } returns
                        listOf<ImageEntity>().asPagingSourceFactory().invoke()
                }
            val useCase = GetSearchResultsUseCase(mockedImagesRepository, mockedMediatorFactory)
            // When
            useCase(" qUeRy ").first()
            // Then
            verify { mockedMediatorFactory.create("query") }
            verify { mockedImagesRepository.getPagingSource("query") }
        }
}
