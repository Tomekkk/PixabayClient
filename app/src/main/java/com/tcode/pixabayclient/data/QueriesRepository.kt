package com.tcode.pixabayclient.data

import kotlinx.coroutines.flow.Flow

interface QueriesRepository {
    fun getStoredQueries(): Flow<List<String>>
}
