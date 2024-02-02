package com.tcode.pixabayclient.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tcode.pixabayclient.data.ImagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        private val imagesRepository: ImagesRepository,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private companion object {
            const val DEFAULT_QUERY = "fruits"
            const val QUERY_KEY = "query"
        }

        val query = savedStateHandle.getStateFlow(QUERY_KEY, DEFAULT_QUERY)

        private val queryToSearch = MutableStateFlow(DEFAULT_QUERY)

        val images =
            queryToSearch.flatMapLatest {
                imagesRepository.getImagesStream(it).cachedIn(viewModelScope)
            }

        init {
            onSearch(DEFAULT_QUERY)
        }

        fun onSearch(query: String) {
            queryToSearch.value = query
        }

        fun onQueryChanged(query: String) {
            savedStateHandle[QUERY_KEY] = query
        }
    }
