package com.tcode.pixabayclient.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {
    fun getImagesStream(query: String): Flow<PagingData<ImageResult>>

    suspend fun getImage(id: Long): ImageDetails?
}
