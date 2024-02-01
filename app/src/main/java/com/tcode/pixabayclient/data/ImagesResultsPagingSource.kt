package com.tcode.pixabayclient.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tcode.pixabayclient.domain.ImageResult
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection.HTTP_BAD_REQUEST

class ImagesResultsPagingSource(
    private val query: String,
    private val imagesDataSource: ImagesDataSource,
    private val uniqueIdProvider: UniqueIdProvider,
) : PagingSource<Int, ImageResult>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageResult> {
        val currentPage = params.key ?: 1
        return try {
            val response =
                imagesDataSource.searchImages(
                    query = query,
                    page = currentPage,
                )
            val results =
                response.hits.map {
                    ImageResult(
                        uniqueId = uniqueIdProvider.provideUniqueId(),
                        id = it.id,
                        tags = it.tags,
                        previewURL = it.previewURL,
                        user = it.user,
                    )
                }
            LoadResult.Page(
                data = results,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey =
                    if (response.totalHits == 0 ||
                        response.totalHits == results.size ||
                        results.isEmpty()
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

    override fun getRefreshKey(state: PagingState<Int, ImageResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}
