package com.tcode.pixabayclient.data

import com.tcode.pixabayclient.api.ApiKeyProvider
import com.tcode.pixabayclient.api.PixabayService
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ImagesRemoteDataSourceTest {
    @Test
    fun `when searchImages invoked should invoke api service function with api key provided by ApiKeyProvider`() =
        runTest {
            // given
            val fakeKeyProvider =
                object : ApiKeyProvider {
                    override val apiKey: String = "fakeKey"
                }
            val pixabayService = mockk<PixabayService>(relaxed = true)
            val objectUnderTest =
                ImagesRemoteDataSource(
                    dispatcher = StandardTestDispatcher(testScheduler),
                    pixabayService = pixabayService,
                    apiKeyProvider = fakeKeyProvider,
                )
            // when
            objectUnderTest.searchImages(
                query = "fakeQuery",
                page = 1,
            )
            // then
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
    fun `when searchImages invoked should invoke api service function with provided query and page index and use default value perPage`() =
        runTest {
            // given
            val fakeKeyProvider =
                object : ApiKeyProvider {
                    override val apiKey: String = "fakeKey"
                }
            val pixabayService = mockk<PixabayService>(relaxed = true)
            val objectUnderTest =
                ImagesRemoteDataSource(
                    dispatcher = StandardTestDispatcher(testScheduler),
                    pixabayService = pixabayService,
                    apiKeyProvider = fakeKeyProvider,
                )
            // when
            objectUnderTest.searchImages(
                query = "query",
                page = 1,
            )
            // then
            coVerify {
                pixabayService.searchImages(
                    key = "fakeKey",
                    query = "query",
                    page = 1,
                    perPage = ImagesRemoteDataSource.DEFAULT_PER_PAGE,
                )
            }
        }
}
