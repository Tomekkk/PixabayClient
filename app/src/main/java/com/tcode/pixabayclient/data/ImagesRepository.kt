package com.tcode.pixabayclient.data

import androidx.paging.PagingData
import com.tcode.pixabayclient.domain.ImageResult
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {
    fun getImagesStream(query: String): Flow<PagingData<ImageResult>>
}
