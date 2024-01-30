package com.tcode.pixabayclient.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PixabaySearchResponse(
    @Json(name = "hits")
    val hits: List<PixabayPhoto>,
    @Json(name = "total")
    val total: Int,
    @Json(name = "totalHits")
    val totalHits: Int,
)
