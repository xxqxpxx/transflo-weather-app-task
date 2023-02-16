package com.swenson.android.task.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ForecastResponse(
  val city: City,
  val cnt: Int,
  val cod: String,
  val list: List<Forecast>,
  val message: Int
) : Parcelable

@Parcelize

data class City(
  val coord: Coord,
  val country: String,
  val id: Int,
  val name: String,
  val population: Int,
  val sunrise: Int,
  val sunset: Int,
  val timezone: Int
) : Parcelable

@Parcelize
data class Forecast(
  val clouds: Clouds,
  val dt: Int,
  val dt_txt: String,
  val main: Main,
  val pop: Double,
  val rain: Rain,
  val sys: Sys,
  val visibility: Int,
  val weather: List<Weather>,
  val wind: Wind
) : Parcelable


@Parcelize

data class Rain(
  val `3h`: Double
) : Parcelable


