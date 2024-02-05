package com.tcode.pixabayclient.data

import com.tcode.pixabayclient.data.api.ApiKeyProvider
import com.tcode.pixabayclient.data.api.PixabayService
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RemoteImagesDataSourceTest {
    @Test
    fun `when searchImages invoked should invoke api service function with api key provided by ApiKeyProvider`() =
        runTest {
            // Given
            val fakeKeyProvider =
                object : ApiKeyProvider {
                    override val apiKey: String = "fakeKey"
                }
            val pixabayService = mockk<PixabayService>(relaxed = true)
            val objectUnderTest =
                RemoteImagesDataSource(
                    pixabayService = pixabayService,
                    apiKeyProvider = fakeKeyProvider,
                )
            // When
            objectUnderTest.searchImages(
                query = "fakeQuery",
                page = 1,
            )
            // Then
            coVerify {
                pixabayService.searchImages(
                    key = "fakeKey",
                    query = any(),
                    page = any(),
                    perPage = any(),
                )
            }
        }

    @Test
    fun `when searchImages invoked should invoke api service function with provided query, page index and use default value perPage`() =
        runTest {
            // Given
            val fakeKeyProvider =
                object : ApiKeyProvider {
                    override val apiKey: String = "fakeKey"
                }
            val pixabayService = mockk<PixabayService>(relaxed = true)
            val objectUnderTest =
                RemoteImagesDataSource(
                    pixabayService = pixabayService,
                    apiKeyProvider = fakeKeyProvider,
                )
            // When
            objectUnderTest.searchImages(
                query = "query",
                page = 1,
            )
            // Then
            coVerify {
                pixabayService.searchImages(
                    key = "fakeKey",
                    query = "query",
                    page = 1,
                    perPage = ImagesDataSource.DEFAULT_IMAGES_PER_PAGE,
                )
            }
        }

    @Test
    fun `when getImage invoked should invoke getImage api service function with image id`() =
        runTest {
            // Given
            val fakeKeyProvider =
                object : ApiKeyProvider {
                    override val apiKey: String = "fakeKey"
                }
            val pixabayService = mockk<PixabayService>(relaxed = true)
            val objectUnderTest =
                RemoteImagesDataSource(
                    pixabayService = pixabayService,
                    apiKeyProvider = fakeKeyProvider,
                )
            // When
            objectUnderTest.getImage(123)
            // Then
            coVerify {
                pixabayService.getImage(
                    key = "fakeKey",
                    id = 123,
                )
            }
        }
}
