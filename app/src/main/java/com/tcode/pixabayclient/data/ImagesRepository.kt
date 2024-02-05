package com.tcode.pixabayclient.data

import androidx.paging.PagingSource
import com.tcode.pixabayclient.data.db.ImageEntity

interface ImagesRepository {
    fun getPagingSource(query: String): PagingSource<Int, ImageEntity>

    suspend fun getImage(id: Long): ImageEntity?
}
