package com.vullpes.testing.data.remote

import com.vullpes.testing.data.remote.responses.ImageResponse
import com.vullpes.testing.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {

    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery:String,
        @Query("key") apiKey:String = BuildConfig.API_KEY
    ): Response<ImageResponse>

}