package com.tcode.pixabayclient.domain

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.tcode.pixabayclient.data.ImagesRepository
import com.tcode.pixabayclient.data.db.ImageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GetSearchResultsUseCaseTest {
    @Test
    fun `should return images results mapped from repository's entities`() =
        runTest {
            val fakeImagesRepository =
                object : ImagesRepository {
                    override fun getImagesStream(query: String): Flow<PagingData<ImageEntity>> =
                        flow {
                            emit(
                                PagingData.from(
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
                                    ),
                                ),
                            )
                        }

                    override suspend fun getImage(id: Long): ImageEntity? = null
                }
            val useCase = GetSearchResultsUseCase(fakeImagesRepository)
            val result = useCase.getResults("q")
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
}
