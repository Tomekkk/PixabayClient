package com.tcode.pixabayclient.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tcode.pixabayclient.domain.GetSearchResultsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        private val getSearchResultsUseCase: GetSearchResultsUseCase,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private companion object {
            const val DEFAULT_QUERY = "fruits"
            const val QUERY_KEY = "query"
        }

        val query = savedStateHandle.getStateFlow(QUERY_KEY, DEFAULT_QUERY)

        private val queryToSearch = MutableSharedFlow<String>(replay = 1, extraBufferCapacity = 1)

        val images =
            queryToSearch.flatMapLatest { query ->
                getSearchResultsUseCase.getResults(query).cachedIn(viewModelScope)
            }

        init {
            onSearch(DEFAULT_QUERY)
        }

        fun onSearch(query: String) {
            queryToSearch.tryEmit(query)
        }

        fun onQueryChanged(query: String) {
            savedStateHandle[QUERY_KEY] = query
        }
    }
