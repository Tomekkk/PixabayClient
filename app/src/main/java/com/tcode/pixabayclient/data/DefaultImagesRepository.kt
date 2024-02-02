package com.tcode.pixabayclient.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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

        override suspend fun getImage(id: Long): ImageDetails? {
            return imagesDataSource.getImage(id).hits.firstOrNull()?.let {
                ImageDetails(
                    id = it.id,
                    tags = it.tags,
                    user = it.user,
                    likes = it.likes,
                    comments = it.comments,
                    downloads = it.downloads,
                    largeImageURL = it.largeImageURL,
                    aspectRatio = it.imageWidth / it.imageHeight.toFloat(),
                )
            }
        }
    }
