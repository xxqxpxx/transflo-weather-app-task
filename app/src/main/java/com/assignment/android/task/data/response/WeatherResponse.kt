package com.swenson.android.task.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class WeatherResponse(
  val base: String,
  val clouds: Clouds,
  val cod: Double,
  val coord: Coord,
  val dt: Double,
  val id: Double,
  val main: Main,
  val name: String,
  val sys: Sys,
  val timezone: Double,
  val visibility: Double,
  val weather: List<Weather>,
  val wind: Wind
) : Parcelable

@Parcelize

data class Clouds(
  val all: Double
) : Parcelable

@Parcelize
data class Coord(
  val lat: Double,
  val lon: Double
) : Parcelable


@Parcelize

data class Main(
  val feels_like: Double,
  val grnd_level: Double,
  val humidity: Double,
  val pressure: Double,
  val sea_level: Double,
  val temp: Double,
  val temp_max: Double,
  val temp_min: Double
) : Parcelable

@Parcelize

data class Sys(
  val country: String,
  val id: Double,
  val sunrise: Double,
  val sunset: Double,
  val type: Double
) : Parcelable

@Parcelize

data class Weather(
  val description: String,
  val icon: String,
  val id: Double,
  val main: String
) : Parcelable

@Parcelize

data class Wind(
  val deg: Double,
  val gust: Double,
  val speed: Double
) : Parcelable

