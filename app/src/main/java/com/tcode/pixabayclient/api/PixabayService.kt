package com.tcode.pixabayclient.api

import com.tcode.pixabayclient.data.SearchResponse
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
}
