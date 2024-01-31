package com.tcode.pixabayclient.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {
    suspend fun getImagesStream(query: String): Flow<PagingData<ImageDto>>
}
