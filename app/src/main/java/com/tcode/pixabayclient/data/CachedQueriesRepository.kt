package com.tcode.pixabayclient.data

import com.tcode.pixabayclient.data.db.RemoteKeysDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CachedQueriesRepository
    @Inject
    constructor(private val remoteKeysDao: RemoteKeysDao) :
    QueriesRepository {
        override fun getStoredQueries(): Flow<List<String>> = remoteKeysDao.getStoredQueriesStream()
    }
