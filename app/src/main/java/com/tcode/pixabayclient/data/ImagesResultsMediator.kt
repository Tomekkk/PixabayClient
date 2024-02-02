package com.tcode.pixabayclient.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tcode.pixabayclient.data.db.ImageEntity
import com.tcode.pixabayclient.data.db.ImagesDao
import com.tcode.pixabayclient.data.db.ImagesDatabase
import com.tcode.pixabayclient.data.db.RemoteKey
import com.tcode.pixabayclient.data.db.RemoteKeysDao
import com.tcode.pixabayclient.utils.TimerProvider
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class ImagesResultsMediator(
    private val query: String,
    private val imagesDataSource: ImagesDataSource,
    private val database: ImagesDatabase,
    private val imagesDao: ImagesDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val timeProvider: TimerProvider,
    private val cacheLifetimeMs: Long =
        TimeUnit.MILLISECONDS.convert(
            CACHE_LIFETIME_HOURS,
            TimeUnit.HOURS,
        ),
) : RemoteMediator<Int, ImageEntity>() {
    companion object {
        private const val CACHE_LIFETIME_HOURS = 1L
        private const val FIRST_PAGE_INDEX = 1
        private const val NEXT_PAGE_INCREMENT = 1
    }

    override suspend fun initialize(): InitializeAction =
        if (timeProvider.currentTimeMillis() - (
                remoteKeysDao.getOldestCreationTime(query)
                    ?: 0
            ) < cacheLifetimeMs
        ) {
            // Cached data is up-to-date, so there is no need to re-fetch from the network.
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Need to refresh cached data from network
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageEntity>,
    ): MediatorResult {
        val page =
            when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKey?.nextPage?.minus(NEXT_PAGE_INCREMENT) ?: FIRST_PAGE_INDEX
                }

                LoadType.PREPEND -> {
                    val remoteKey = getRemoteKeyForFirstItem(state)
                    val prevPage =
                        remoteKey?.prevPage
                            ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKey = getRemoteKeyForLastItem(state)
                    val nextPage =
                        remoteKey?.nextPage
                            ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                    nextPage
                }
            }

        try {
            val response =
                imagesDataSource.searchImages(
                    query = query,
                    page = page,
                )
            val noMoreData = response.total == 0 || response.hits.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.delete(query)
                    imagesDao.delete(query)
                }
                if (!noMoreData) {
                    remoteKeysDao.delete(query)
                }
                val insertedIds =
                    imagesDao.insert(
                        response.hits.map {
                            ImageEntity(
                                imageId = it.id,
                                previewURL = it.previewURL,
                                largeImageURL = it.largeImageURL,
                                imageWidth = it.imageWidth,
                                imageHeight = it.imageHeight,
                                downloads = it.downloads,
                                likes = it.likes,
                                comments = it.comments,
                                user = it.user,
                                query = query,
                                previewHeight = it.previewHeight,
                                previewWidth = it.previewWidth,
                                tags = it.tags,
                            )
                        },
                    )

                val prevPage = if (page == FIRST_PAGE_INDEX) null else page - NEXT_PAGE_INCREMENT
                val nextPage = if (noMoreData) null else page + NEXT_PAGE_INCREMENT

                remoteKeysDao.insertAll(
                    insertedIds.map {
                        RemoteKey(
                            id = it,
                            query = query,
                            prevPage = prevPage,
                            nextPage = nextPage,
                            createdAt = timeProvider.currentTimeMillis(),
                        )
                    },
                )
            }
            return MediatorResult.Success(endOfPaginationReached = noMoreData)
        } catch (exception: HttpException) {
            return if (exception.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                database.withTransaction {
                    remoteKeysDao.getLatestKey(query)?.let { remoteKey ->
                        remoteKeysDao.update(
                            remoteKey.copy(
                                nextPage = null,
                            ),
                        )
                    }
                }

                MediatorResult.Success(endOfPaginationReached = true)
            } else {
                MediatorResult.Error(exception)
            }
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        }
    }

    /**
     * Get the remote key closest to the current anchor position.
     */
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ImageEntity>): RemoteKey? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { uniqueId ->
                remoteKeysDao.getKey(uniqueId)
            }
        }

    /**
     * Get the remote key for the first item in the list.
     */
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ImageEntity>): RemoteKey? =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.id?.let { uniqueId ->
            remoteKeysDao.getKey(uniqueId)
        }

    /**
     * Get the remote key for the last item in the list.
     */
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ImageEntity>): RemoteKey? =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.id?.let { uniqueId ->
            remoteKeysDao.getKey(uniqueId)
        }
}
