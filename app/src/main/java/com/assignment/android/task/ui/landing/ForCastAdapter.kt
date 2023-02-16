package com.swenson.android.task.ui.landing

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.swenson.android.task.data.response.Forecast
import com.swenson.android.task.data.response.ForecastResponse
import com.swenson.android.task.databinding.ItemWeatherBinding

class ForCastAdapter(
  private var forecastList: List<Forecast>? = arrayListOf(),
  private val context: Context
) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  @SuppressLint("NotifyDataSetChanged")
  fun submitList(playerList: ForecastResponse) {
    this.forecastList = playerList.list
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int {
    return forecastList?.size ?: 0
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return ViewHolderForecast(
      ItemWeatherBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
      )
    )
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    forecastList?.get(position)
      ?.let { (holder as ViewHolderForecast).bind(context = context, forecast = it) }
  }

  private class ViewHolderForecast(val binding: ItemWeatherBinding) :
    RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(context: Context, forecast: Forecast) {
      binding.txtDay.text = forecast.dt_txt
      binding.txtTemp.text = "${forecast.main.temp} °C "

      Glide.with(context)
        .applyDefaultRequestOptions(RequestOptions().centerCrop())
        .load("https://openweathermap.org/img/w/${forecast.weather[0].icon}.png")
        .into(binding.imgCondition)
    }
  }
}