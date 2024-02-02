package com.tcode.pixabayclient.domain

import androidx.paging.PagingData
import com.tcode.pixabayclient.data.ImageDetails
import com.tcode.pixabayclient.data.ImageResult
import com.tcode.pixabayclient.data.ImagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetImageDetailsUseCaseTest {
    @Test
    fun `should return image details from repository`() {
        runTest {
            // given
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
                    override fun getImagesStream(query: String): Flow<PagingData<ImageResult>> = throw NotImplementedError()

                    override suspend fun getImage(id: Long): ImageDetails = fakeImageDetails
                }
            val objectUnderTest = GetImageDetailsUseCase(fakeImagesRepository)
            // when
            val result = objectUnderTest.get(1)
            // then
            assertEquals(fakeImageDetails, result)
        }
    }
}
