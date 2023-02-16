package com.swenson.android.task.data.repo

import android.util.Log
import com.swenson.android.task.data.response.ForecastResponse
import com.swenson.android.task.data.response.SearchResponse
import com.swenson.android.task.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apiService: ApiService) {

  private val TAG = "Weather Repository"

  fun fetchWeatherBasedCity(city: String): Flow<SearchResponse> {
    return flow {
      val response = apiService.searchForCity(city)
      Log.i(TAG, "fetchWeatherBasedCity response $response")
      emit(response)
    }
  }


  fun searchForCity(city: String): Flow<SearchResponse> {
    return flow {
      val response = apiService.searchForCity(city = city)
      Log.i(TAG, "searchForCity response $response")
      emit(response)
    }
  }


  fun fetchForecastBasedCity(lat: String, lon: String): Flow<ForecastResponse> {
    return flow {
      val response = apiService.fetchForcastBasedCity(lat = lat, lon = lon)
      Log.i(TAG, "fetchForecastBasedCity response $response")
      emit(response)
    }
  }

}