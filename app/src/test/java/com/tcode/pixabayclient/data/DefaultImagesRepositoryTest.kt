package com.tcode.pixabayclient.data

import androidx.paging.testing.asSnapshot
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DefaultImagesRepositoryTest {
    @Test
    fun `when getImagesStream invoked should call service with given query and first page params`() =
        runTest {
            // given
            val imagesDataSource = mockk<ImagesDataSource>(relaxed = true)
            val uniqueIdProvider = mockk<UniqueIdProvider>(relaxed = true)
            val objectUnderTest = DefaultImagesRepository(imagesDataSource, uniqueIdProvider)
            // when
            objectUnderTest.getImagesStream("query").asSnapshot()
            // then
            coVerify { imagesDataSource.searchImages("query", 1) }
        }

    @Test
    fun `when getImage invoked should return image details object mapped from service response`() =
        runTest {
            // given
            val fakeImagesDataSource =
                object : ImagesDataSource {
                    override suspend fun searchImages(
                        query: String,
                        page: Int,
                    ): SearchResponse {
                        return SearchResponse(emptyList(), 0, 0)
                    }

                    override suspend fun getImage(id: Long): SearchResponse {
                        return SearchResponse(
                            listOf(
                                ImageDto(
                                    id = 1,
                                    tags = "tags",
                                    previewURL = "previewURL",
                                    previewWidth = 1,
                                    previewHeight = 1,
                                    largeImageURL = "largeImageURL",
                                    imageWidth = 1,
                                    imageHeight = 1,
                                    downloads = 1,
                                    likes = 1,
                                    comments = 1,
                                    user = "user",
                                ),
                            ),
                            1,
                            1,
                        )
                    }
                }
            val uniqueIdProvider = mockk<UniqueIdProvider>(relaxed = true)
            val objectUnderTest = DefaultImagesRepository(fakeImagesDataSource, uniqueIdProvider)
            // when
            val result = objectUnderTest.getImage(1)
            // then
            assertEquals(
                ImageDetails(
                    id = 1,
                    tags = "tags",
                    largeImageURL = "largeImageURL",
                    aspectRatio = 1F,
                    downloads = 1,
                    likes = 1,
                    comments = 1,
                    user = "user",
                ),
                result,
            )
        }
}
