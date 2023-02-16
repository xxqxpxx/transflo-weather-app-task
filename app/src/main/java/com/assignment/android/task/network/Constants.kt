package com.swenson.android.task.network

import com.swenson.android.task.BuildConfig

object Constants {

  const val API_TIMEOUT: Long = 60

  const val BASE_URL = BuildConfig.BASE_URL

  const val CITY_SEARCH_URL = "weather?appid=996967a2f96cba826f0e62700c0e45a5&units=metric"

  const val FORECAST_URL = "forecast?appid=996967a2f96cba826f0e62700c0e45a5&units=metric"


  const val PREFERENCE_NAME = "pref_swenson"

  const val REPOS_LIST_PREF_NAME = "repos_list"

  const val NUMBER_OF_REPOS_PER_PAGE = 15

  const val STARTING_KEY = 1

}