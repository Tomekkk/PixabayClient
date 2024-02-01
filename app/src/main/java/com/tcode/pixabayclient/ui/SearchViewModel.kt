package com.tcode.pixabayclient.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tcode.pixabayclient.data.ImagesRepository
import com.tcode.pixabayclient.domain.ImageResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
    @Inject
    constructor(val imagesRepository: ImagesRepository) : ViewModel() {
        private val _query = MutableStateFlow("fruits")
        val query = _query.asStateFlow()

        @OptIn(ExperimentalCoroutinesApi::class)
        val images: Flow<PagingData<ImageResult>> =
            _query.flatMapConcat { imagesRepository.getImagesStream(it) }
                .cachedIn(viewModelScope)

        fun onSearch(query: String) {
            _query.value = query
        }
    }
