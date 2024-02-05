package com.tcode.pixabayclient.domain

import androidx.paging.PagingSource
import com.tcode.pixabayclient.data.ImagesRepository
import com.tcode.pixabayclient.data.db.ImageEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetImageDetailsUseCaseTest {
    @Test
    fun `should return image details mapped from repository's entity`() {
        runTest {
            // Given
            val fakeImageDetails =
                ImageDetails(
                    id = 1,
                    user = "user",
                    downloads = 1,
                    likes = 1,
                    comments = 1,
                    tags = "tags",
                    largeImageURL = "largeImageURL",
                    aspectRatio = 1F,
                )
            val fakeImagesRepository =
                object : ImagesRepository {
                    override fun getPagingSource(query: String): PagingSource<Int, ImageEntity> {
                        throw NotImplementedError()
                    }

                    override suspend fun getImage(id: Long): ImageEntity =
                        ImageEntity(
                            imageId = 1,
                            user = "user",
                            downloads = 1,
                            likes = 1,
                            comments = 1,
                            tags = "tags",
                            largeImageURL = "largeImageURL",
                            previewWidth = 1,
                            previewHeight = 1,
                            imageHeight = 1,
                            imageWidth = 1,
                            previewURL = "previewURL",
                            query = "query",
                        )
                }
            val objectUnderTest = GetImageDetailsUseCase(fakeImagesRepository)
            // When
            val result = objectUnderTest.get(1)
            // Then
            assertEquals(fakeImageDetails, result)
        }
    }
}
