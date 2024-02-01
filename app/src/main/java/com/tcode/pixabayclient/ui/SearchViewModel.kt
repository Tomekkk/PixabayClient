package com.tcode.pixabayclient.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tcode.pixabayclient.data.ImagesRepository
import com.tcode.pixabayclient.domain.ImageResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        private val imagesRepository: ImagesRepository,
    ) : ViewModel() {
        private companion object {
            const val DEFAULT_QUERY = "fruits"
        }

        val defaultQuery = DEFAULT_QUERY

        private val _images = MutableStateFlow<PagingData<ImageResult>?>(null)
        val images: Flow<PagingData<ImageResult>> = _images.filterNotNull()

        init {
            onSearch(DEFAULT_QUERY)
        }

        fun onSearch(query: String) {
            viewModelScope.launch {
                _images.value = imagesRepository.getImagesStream(query).cachedIn(viewModelScope).first()
            }
        }
    }
