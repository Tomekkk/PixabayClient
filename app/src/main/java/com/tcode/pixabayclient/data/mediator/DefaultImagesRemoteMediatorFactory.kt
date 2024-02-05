package com.tcode.pixabayclient.data.mediator

import androidx.paging.ExperimentalPagingApi
import com.tcode.pixabayclient.data.ImagesDataSource
import com.tcode.pixabayclient.data.db.ImagesDao
import com.tcode.pixabayclient.data.db.ImagesDatabase
import com.tcode.pixabayclient.data.db.RemoteKeysDao
import com.tcode.pixabayclient.utils.TimerProvider
import javax.inject.Inject

class DefaultImagesRemoteMediatorFactory
    @Inject
    constructor(
        private val imagesDataSource: ImagesDataSource,
        private val database: ImagesDatabase,
        private val timeProvider: TimerProvider,
        private val cacheLifetime: CacheLifetime,
    ) : ImagesRemoteMediatorFactory {
        private val imagesDao: ImagesDao = database.getImagesDao()
        private val remoteKeysDao: RemoteKeysDao = database.getRemoteKeysDao()

        @OptIn(ExperimentalPagingApi::class)
        override fun create(query: String): ImagesRemoteMediator =
            DBCachedImagesResultsMediator(
                query,
                imagesDataSource,
                database,
                imagesDao,
                remoteKeysDao,
                timeProvider,
                cacheLifetime,
            )
    }
