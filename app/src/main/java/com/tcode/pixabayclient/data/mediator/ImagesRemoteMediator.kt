package com.tcode.pixabayclient.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.tcode.pixabayclient.data.db.ImageEntity

@OptIn(ExperimentalPagingApi::class)
abstract class ImagesRemoteMediator(val query: String) : RemoteMediator<Int, ImageEntity>()
