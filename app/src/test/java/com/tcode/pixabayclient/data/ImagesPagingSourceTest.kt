package com.tcode.pixabayclient.data

import androidx.paging.PagingSource
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.HttpURLConnection.HTTP_BAD_REQUEST

class ImagesPagingSourceTest {
    @Test
    fun `when service has single page available load should return images from first page and indicate there is no more items available`() =
        runTest {
            // given
            val itemsRange = 1L..3
            val mockImages = ImagesFactory.create(itemsRange)
            val fakeDataSource =
                object : ImagesDataSource {
                    override suspend fun searchImages(
                        query: String,
                        page: Int,
                    ): SearchResponse {
                        return SearchResponse(
                            total = mockImages.size,
                            totalHits = mockImages.size,
                            hits = mockImages,
                        )
                    }
                }
            val pagingSource = ImagesPagingSource("query", fakeDataSource)
            // when
            val results =
                pagingSource.load(
                    PagingSource.LoadParams.Refresh(
                        key = null,
                        loadSize = 3,
                        placeholdersEnabled = false,
                    ),
                )
            // then
            assertEquals(
                PagingSource.LoadResult.Page(
                    data = mockImages,
                    prevKey = 1,
                    nextKey = null,
                ),
                results,
            )
        }

    @Test
    fun `when service has no data load should return page with empty list`() =
        runTest {
            // given
            val fakeDataSource =
                object : ImagesDataSource {
                    override suspend fun searchImages(
                        query: String,
                        page: Int,
                    ): SearchResponse =
                        SearchResponse(
                            total = 0,
                            totalHits = 0,
                            hits = emptyList(),
                        )
                }
            val pagingSource = ImagesPagingSource("query", fakeDataSource)
            //  when
            val results =
                pagingSource.load(
                    PagingSource.LoadParams.Refresh(
                        key = null,
                        loadSize = ImagesDataSource.DEFAULT_IMAGES_PER_PAGE,
                        placeholdersEnabled = false,
                    ),
                )
            // then
            assertEquals(
                PagingSource.LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null,
                ),
                results,
            )
        }

    @Test
    fun `when service has more pages available load should return images from first page and indicate there is more items available`() =
        runTest {
            // given
            val fakeDataSource =
                object : ImagesDataSource {
                    override suspend fun searchImages(
                        query: String,
                        page: Int,
                    ): SearchResponse {
                        val images =
                            if (page == 1) {
                                ImagesFactory.create(1L..3L)
                            } else {
                                ImagesFactory.create(4L..6L)
                            }
                        return SearchResponse(
                            total = 6,
                            totalHits = 6,
                            hits = images,
                        )
                    }
                }
            val pagingSource = ImagesPagingSource("query", fakeDataSource)
            // when
            val results =
                pagingSource.load(
                    PagingSource.LoadParams.Refresh(
                        key = null,
                        loadSize = 3,
                        placeholdersEnabled = false,
                    ),
                )
            // then
            assertEquals(
                PagingSource.LoadResult.Page(
                    data = ImagesFactory.create(1L..3L),
                    prevKey = null,
                    nextKey = 2,
                ),
                results,
            )
        }

    @Test
    fun `when service return is out of valid range load should return empty images list page and indicate there is no more items available`() =
        runTest {
            // given
            val fakeDataSource =
                object : ImagesDataSource {
                    @Throws(HttpException::class)
                    override suspend fun searchImages(
                        query: String,
                        page: Int,
                    ): SearchResponse {
                        throw HttpException(
                            Response.error<SearchResponse>(
                                HTTP_BAD_REQUEST,
                                "[ERROR 400] page is out of valid range.".toResponseBody(),
                            ),
                        )
                    }
                }
            val pagingSource = ImagesPagingSource("query", fakeDataSource)
            // when
            val results =
                pagingSource.load(
                    PagingSource.LoadParams.Refresh(
                        key = null,
                        loadSize = 3,
                        placeholdersEnabled = false,
                    ),
                )
            // then
            assertEquals(
                PagingSource.LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null,
                ),
                results,
            )
        }
}
