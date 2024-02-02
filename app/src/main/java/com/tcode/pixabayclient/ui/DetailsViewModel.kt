package com.tcode.pixabayclient.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tcode.pixabayclient.domain.GetImageDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel
    @Inject
    constructor(getImageDetailsUseCase: GetImageDetailsUseCase, savedStateHandle: SavedStateHandle) :
    ViewModel() {
        private val id: Long = checkNotNull(savedStateHandle["imageId"])

        val imageDetails =
            flow {
                getImageDetailsUseCase.get(id)?.let {
                    emit(it)
                }
            }
    }
