package com.tcode.pixabayclient.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tcode.pixabayclient.data.db.ImageEntity
import com.tcode.pixabayclient.data.db.ImagesDatabase
import com.tcode.pixabayclient.utils.TimerProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CachedImagesRepository
    @Inject
    constructor(
        private val imagesDataSource: ImagesDataSource,
        private val database: ImagesDatabase,
        private val timeProvider: TimerProvider,
    ) :
    ImagesRepository {
        @OptIn(ExperimentalPagingApi::class)
        override fun getImagesStream(query: String): Flow<PagingData<ImageEntity>> {
            val unifiedQuery = query.trim().lowercase()
            return Pager(
                config =
                    PagingConfig(
                        pageSize = ImagesDataSource.DEFAULT_IMAGES_PER_PAGE,
                        maxSize = ImagesDataSource.DEFAULT_IMAGES_PER_PAGE * 3,
                        enablePlaceholders = false,
                    ),
                remoteMediator =
                    ImagesResultsMediator(
                        unifiedQuery,
                        imagesDataSource,
                        database,
                        database.getImages(),
                        database.getRemoteKeys(),
                        timeProvider,
                    ),
            ) {
                database.getImages().getAll(unifiedQuery)
            }.flow
        }

        override suspend fun getImage(id: Long): ImageEntity? = database.getImages().getImage(id)
    }
