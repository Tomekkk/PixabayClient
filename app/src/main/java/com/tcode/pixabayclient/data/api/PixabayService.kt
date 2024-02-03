package com.tcode.pixabayclient.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayService {
    @GET("/api/")
    suspend fun searchImages(
        @Query("key") key: String,
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): SearchResponse

    @GET("/api/")
    suspend fun getImage(
        @Query("key") key: String,
        @Query("id") id: Long,
    ): SearchResponse
}
