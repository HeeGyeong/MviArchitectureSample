package com.example.mvi_architecture_sample.api

import com.example.mvi_architecture_sample.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("v1/search/movie.json")
    suspend fun getSearchMovieFlow(
        @Query("query") query: String,
        @Query("start") start: Int = 1,
        @Query("display") display: Int = 15
    ): MovieResponse
}