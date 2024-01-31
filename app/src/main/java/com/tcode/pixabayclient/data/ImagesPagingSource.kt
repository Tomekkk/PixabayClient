package com.tcode.pixabayclient.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection.HTTP_BAD_REQUEST

class ImagesPagingSource(
    private val query: String,
    private val imagesDataSource: ImagesDataSource,
) : PagingSource<Int, ImageDto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageDto> {
        val currentPage = params.key ?: 1
        return try {
            val response =
                imagesDataSource.searchImages(
                    query = query,
                    page = currentPage,
                )
            LoadResult.Page(
                data = response.hits,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey =
                    if (response.totalHits == 0 ||
                        response.totalHits == response.hits.size ||
                        response.hits.isEmpty()
                    ) {
                        null
                    } else {
                        currentPage + 1
                    },
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return if (exception.code() == HTTP_BAD_REQUEST) {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = null,
                )
            } else {
                LoadResult.Error(exception)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ImageDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}
