package com.tcode.pixabayclient.domain

import com.tcode.pixabayclient.data.QueriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetStoredQueryResultsUseCase
    @Inject
    constructor(private val queriesRepository: QueriesRepository) {
        operator fun invoke(): Flow<List<PreviousQuery>> =
            queriesRepository.getStoredQueries().map {
                    query ->
                query.map { PreviousQuery(it) }
            }
    }
