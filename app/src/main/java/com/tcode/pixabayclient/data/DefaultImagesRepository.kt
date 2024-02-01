package com.tcode.pixabayclient.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultImagesRepository
    @Inject
    constructor(private val imagesDataSource: ImagesDataSource) :
    ImagesRepository {
        override fun getImagesStream(query: String): Flow<PagingData<ImageDto>> =
            Pager(
                config =
                    PagingConfig(
                        pageSize = ImagesDataSource.DEFAULT_IMAGES_PER_PAGE,
                        enablePlaceholders = false,
                    ),
                pagingSourceFactory = { ImagesPagingSource(query, imagesDataSource) },
            ).flow
    }
