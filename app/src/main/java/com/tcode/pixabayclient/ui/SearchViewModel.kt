package com.tcode.pixabayclient.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tcode.pixabayclient.domain.GetSearchResultsUseCase
import com.tcode.pixabayclient.domain.GetStoredQueryResultsUseCase
import com.tcode.pixabayclient.domain.PreviousQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        private val getSearchResultsUseCase: GetSearchResultsUseCase,
        getStoredQueryResultsUseCase: GetStoredQueryResultsUseCase,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private companion object {
            const val DEFAULT_QUERY = "fruits"
            const val QUERY_KEY = "query"
        }

        val queryStream = savedStateHandle.getStateFlow(QUERY_KEY, DEFAULT_QUERY)

        private val queryToSearch = MutableSharedFlow<String>(replay = 1, extraBufferCapacity = 1)

        private val _queriesHistoryStream = MutableStateFlow(emptyList<PreviousQuery>())
        val queriesHistoryStream = _queriesHistoryStream.asStateFlow()

        val images =
            queryToSearch.flatMapLatest { query ->
                getSearchResultsUseCase.getResults(query).cachedIn(viewModelScope)
            }

        private val _snackBarMessage = MutableSharedFlow<String>(extraBufferCapacity = 1)
        val snackBarMessage = _snackBarMessage.asSharedFlow()

        init {
            onSearch(DEFAULT_QUERY)
            getStoredQueryResultsUseCase().onEach {
                _queriesHistoryStream.value = it
            }.launchIn(viewModelScope)
        }

        fun onSearch(query: String) {
            queryToSearch.tryEmit(query)
        }

        fun onQueryChanged(query: String) {
            savedStateHandle[QUERY_KEY] = query
        }
    }
