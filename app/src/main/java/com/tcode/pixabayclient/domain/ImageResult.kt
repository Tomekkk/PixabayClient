package com.tcode.pixabayclient.domain

import com.tcode.pixabayclient.data.UniqueId

data class ImageResult(
    val uniqueId: UniqueId,
    val id: Long,
    val tags: String,
    val previewURL: String,
    val user: String,
)
