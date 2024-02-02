package com.tcode.pixabayclient.data

data class ImageDetails(
    val id: Long,
    val tags: String,
    val largeImageURL: String,
    val aspectRatio: Float,
    val downloads: Long,
    val likes: Long,
    val comments: Long,
    val user: String,
)
