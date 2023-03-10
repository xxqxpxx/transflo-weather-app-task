package com.swenson.android.task.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize

data class SearchResponse(
  val base: String,
  val clouds: Clouds,
  val cod: Int,
  val coord: Coord,
  val dt: Int,
  val id: Int,
  val main: Main,
  val name: String,
  val sys: Sys,
  val timezone: Int,
  val visibility: Int,
  val weather: List<Weather>,
  val wind: Wind
) : Parcelable