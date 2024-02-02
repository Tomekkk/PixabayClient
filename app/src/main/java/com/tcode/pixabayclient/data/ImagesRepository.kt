package com.tcode.pixabayclient.data

import androidx.paging.PagingData
import com.tcode.pixabayclient.data.db.ImageEntity
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {
    fun getImagesStream(query: String): Flow<PagingData<ImageEntity>>

    suspend fun getImage(id: Long): ImageEntity?
}
