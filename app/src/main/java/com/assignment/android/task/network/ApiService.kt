package com.swenson.android.task.network

import com.swenson.android.task.data.response.*
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

  @GET(Constants.CITY_SEARCH_URL)
  suspend fun searchForCity(
    @Query("q") city: String? = "Dubai",
  ): SearchResponse


  @GET(Constants.FORECAST_URL)
  suspend fun fetchForcastBasedCity(
    @Query("lat") lat: String? = "44.34",
    @Query("lon") lon: String? = "10.99"
  ): ForecastResponse


}