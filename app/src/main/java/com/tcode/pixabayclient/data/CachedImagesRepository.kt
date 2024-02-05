package com.tcode.pixabayclient.data

import androidx.paging.PagingSource
import com.tcode.pixabayclient.data.db.ImageEntity
import com.tcode.pixabayclient.data.db.ImagesDatabase
import javax.inject.Inject

class CachedImagesRepository
    @Inject
    constructor(
        private val database: ImagesDatabase,
    ) :
    ImagesRepository {
        override fun getPagingSource(query: String): PagingSource<Int, ImageEntity> = database.getImagesDao().getAll(query)

        override suspend fun getImage(id: Long): ImageEntity? = database.getImagesDao().getImage(id)
    }
