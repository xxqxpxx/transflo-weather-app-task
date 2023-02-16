package com.swenson.android.task.ui.landing

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swenson.android.task.data.response.SearchResponse
import com.swenson.android.task.databinding.ItemCityBinding


class SearchItemsAdapter(
  private var forecastList: List<SearchResponse>? = arrayListOf(), private val context: Context,
  private val onClickListener: OnClickListener

) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  @SuppressLint("NotifyDataSetChanged")
  fun submitList(playerList: List<SearchResponse>) {
    this.forecastList = playerList
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int {
    return forecastList?.size ?: 0
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return ViewHolderForecast(
      ItemCityBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
      )
    )
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    forecastList?.get(position)
      ?.let {
        (holder as ViewHolderForecast).bind(
          context = context,
          forecast = it,
          onClickListener
        )
      }
  }

  private class ViewHolderForecast(val binding: ItemCityBinding) :
    RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(
      context: Context,
      forecast: SearchResponse,
      onClickListener: OnClickListener
    ) {
      binding.txtCityName.text = "${forecast.name} - "
      binding.txtCountryName.text = "${forecast.sys.country}"

      binding.rootLayout.setOnClickListener {
        onClickListener.clickListener(forecast)
      }

    }
  }


  class OnClickListener(val clickListener: (RepoResponseItem: SearchResponse) -> Unit) {
    fun onClick(RepoResponseItem: SearchResponse) = clickListener(RepoResponseItem)
  }
}