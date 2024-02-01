package com.tcode.pixabayclient.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tcode.pixabayclient.domain.ImageResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultImagesRepository
    @Inject
    constructor(
        private val imagesDataSource: ImagesDataSource,
        private val uniqueIdProvider: UniqueIdProvider,
    ) :
    ImagesRepository {
        override fun getImagesStream(query: String): Flow<PagingData<ImageResult>> =
            Pager(
                config =
                    PagingConfig(
                        pageSize = ImagesDataSource.DEFAULT_IMAGES_PER_PAGE,
                        enablePlaceholders = false,
                    ),
                pagingSourceFactory = {
                    ImagesResultsPagingSource(
                        query,
                        imagesDataSource,
                        uniqueIdProvider,
                    )
                },
            ).flow
    }
