package com.tcode.pixabayclient.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "hits")
    val hits: List<ImageDto>,
    @Json(name = "total")
    val total: Int,
    @Json(name = "totalHits")
    val totalHits: Int,
)
