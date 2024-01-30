package com.tcode.pixabayclient.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "id")
    val id: Long,
    @Json(name = "pageURL")
    val pageURL: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "tags")
    val tags: String,
    @Json(name = "previewURL")
    val previewURL: String,
    @Json(name = "previewWidth")
    val previewWidth: Int,
    @Json(name = "previewHeight")
    val previewHeight: Int,
    @Json(name = "webformatURL")
    val webformatURL: String,
    @Json(name = "webformatWidth")
    val webformatWidth: Int,
    @Json(name = "webformatHeight")
    val webformatHeight: Int,
    @Json(name = "largeImageURL")
    val largeImageURL: String,
    @Json(name = "imageWidth")
    val imageWidth: Int,
    @Json(name = "imageHeight")
    val imageHeight: Int,
    @Json(name = "imageSize")
    val imageSize: Int,
    @Json(name = "views")
    val views: Long,
    @Json(name = "downloads")
    val downloads: Long,
    @Json(name = "collections")
    val collections: Int,
    @Json(name = "likes")
    val likes: Long,
    @Json(name = "comments")
    val comments: Long,
    @Json(name = "user_id")
    val userId: Long,
    @Json(name = "user")
    val user: String,
    @Json(name = "userImageURL")
    val userImageURL: String,
)