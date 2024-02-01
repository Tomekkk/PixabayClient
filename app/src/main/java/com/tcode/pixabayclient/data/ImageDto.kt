package com.tcode.pixabayclient.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data transfer object for images from Pixabay API.
 * Not all fields from API are used in the app.
 * See https://pixabay.com/api/docs/#api_search_images
 */
@JsonClass(generateAdapter = true)
data class ImageDto(
    @Json(name = "id")
    val id: Long,
    @Json(name = "tags")
    val tags: String,
    @Json(name = "previewURL")
    val previewURL: String,
    @Json(name = "largeImageURL")
    val largeImageURL: String,
    @Json(name = "downloads")
    val downloads: Long,
    @Json(name = "likes")
    val likes: Long,
    @Json(name = "comments")
    val comments: Long,
    @Json(name = "user")
    val user: String,
)
