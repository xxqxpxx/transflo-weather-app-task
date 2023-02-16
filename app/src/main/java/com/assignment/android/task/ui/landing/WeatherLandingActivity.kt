package com.swenson.android.task.ui.landing

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.swenson.android.task.base.BaseActivity
import com.swenson.android.task.data.helper.ComplexPreferencesImpl
import com.swenson.android.task.data.response.*
import com.swenson.android.task.databinding.ActivityWeatherLandingBinding
import com.swenson.android.task.network.ResultModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import kotlin.properties.Delegates


@AndroidEntryPoint
class WeatherLandingActivity : BaseActivity<ActivityWeatherLandingBinding>() {

  private lateinit var mFusedLocationClient: FusedLocationProviderClient
  private val permissionId = 2


  private var tempC = 0.0

  var lat by Delegates.notNull<Double>()
  var lon by Delegates.notNull<Double>()


  private val viewModel: WeatherLandingViewModel by viewModels()

  private lateinit var foreCastAdapter: ForCastAdapter

  private lateinit var searchItemsAdapter: SearchItemsAdapter

  private lateinit var complexPreferences: ComplexPreferencesImpl


  override fun getViewBinding() = ActivityWeatherLandingBinding.inflate(layoutInflater)


  var cityOnClickListener = (SearchItemsAdapter.OnClickListener { item ->
    handleCitySelection(item)
  })


  override fun setupView() {
    initListeners()
    initFusedLocationClient()
    initForeCastList()

    complexPreferences = ComplexPreferencesImpl(this)
    viewModel.setComplexPref(complexPreferences)
    handleFavouriteState()


  }

  override fun onResume() {
    super.onResume()
    handleFavouriteState()
  }

  private fun handleFavouriteState() {
    viewModel.handleIfAlreadyFavourite()
  }

  @SuppressLint("SetTextI18n")
  private fun initListeners() {
    binding.layoutError.button.setOnClickListener {
      viewModel.refresh(viewModel.localCity)
      hideErrorAndRefresh()
    }

    binding.icSearch.setOnClickListener {
      handleSearchLayout(true)
    }

  }

  private fun handleSearchLayout(showLayout: Boolean) {
    if (showLayout) {
      binding.searchLayout.searchLayout.visibility = View.VISIBLE

      binding.searchLayout.imageView2.setOnClickListener {
        handleSearchLayout(false)
      }

      binding.searchLayout.imgClose.setOnClickListener {
        handleSearchLayout(false)
      }

      binding.searchLayout.bottomLayoutClose.setOnClickListener {
        handleSearchLayout(false)
      }

      binding.searchLayout.etSearch.addTextChangedListener {

        if (!it.isNullOrEmpty()) {
          if (it.length >= 4) {
            viewModel.searchForCity(it.toString().trim())
          }
        }

      }

    } else {
      binding.searchLayout.searchLayout.visibility = View.GONE

      if (searchItemsAdapter.itemCount > 0)
        searchItemsAdapter.submitList(emptyList())

      binding.searchLayout.etSearch.text.clear()
    }
  }


  private fun hideErrorAndRefresh() {
    handleError(isError = false)
  }


  private fun handleError(isError: Boolean) {
    if (isError) {
      binding.layoutError.layoutError.visibility = View.VISIBLE
    } else {
      binding.layoutError.layoutError.visibility = View.GONE
    }
  }


  private fun initForeCastList() {
    foreCastAdapter = ForCastAdapter(context = this)
    binding.recyclerViewWeeklyWeather.layoutManager =
      LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
    binding.recyclerViewWeeklyWeather.adapter = foreCastAdapter
  }


  private fun initCitiesList() {
    searchItemsAdapter = SearchItemsAdapter(context = this, onClickListener = cityOnClickListener)
    binding.searchLayout.recyclerView.layoutManager =
      LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    binding.searchLayout.recyclerView.adapter = searchItemsAdapter
  }


  override fun setupViewModelObservers() {
    viewModel.weatherDataObserver.observe(this, weatherDataObserver)
    viewModel.searchTextDataObserver.observe(this, searchDataObserver)
    viewModel.forecastTextDataObserver.observe(this, forecastDataObserver)

    viewModel.planetsDataObserverFav.observe(this) {
      handleFavouriteIconState(it)
    }

  }


  private fun handleFavouriteIconState(list: ArrayList<SearchResponse>) {
    if (list.isNotEmpty()) {
      initCitiesList()

      searchItemsAdapter.submitList(list)

      searchItemsAdapter.notifyDataSetChanged()

    } else {
      //     binding.txtMyfavourites.visibility = View.GONE
      //     binding.rcvFavourites.visibility = View.GONE
    }
  }



  private fun initFusedLocationClient() {
    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    getLocation()
  }

  private fun isLocationEnabled(): Boolean {
    val locationManager: LocationManager =
      getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
      LocationManager.NETWORK_PROVIDER
    )
  }

  private fun checkPermissions(): Boolean {
    if (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
      ) == PackageManager.PERMISSION_GRANTED &&
      ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
      ) == PackageManager.PERMISSION_GRANTED
    ) {
      return true
    }
    return false
  }

  private fun requestPermissions() {
    ActivityCompat.requestPermissions(
      this,
      arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
      ),
      permissionId
    )
  }

  @SuppressLint("MissingSuperCall")
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    if (requestCode == permissionId) {
      if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
        getLocation()
      }
    }
  }

  @SuppressLint("MissingPermission", "SetTextI18n")
  private fun getLocation() {
    if (checkPermissions()) {
      if (isLocationEnabled()) {
        mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
          val location: Location? = task.result
          if (location != null) {
            val geocoder = Geocoder(this, Locale.getDefault())
            val list: List<Address> =
              geocoder.getFromLocation(location.latitude, location.longitude, 1)

            lat = location.latitude
            lon = location.longitude

            binding.txtAddress.text = list[0].adminArea

            Log.i(TAG, "City : ${list[0].adminArea}")
            viewModel.fetchWeatherData(city = list[0].adminArea)
            viewModel.fetchForeCast(lat.toString(), lon.toString())
          }
        }
      } else {
        Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
        viewModel.fetchWeatherData("dubai")
        viewModel.fetchForeCast("25.076", "54.947")

      }
    } else {
      requestPermissions()
      viewModel.fetchWeatherData("dubai")
      viewModel.fetchForeCast("25.076", "54.947")

    }
  }


  private val weatherDataObserver = Observer<ResultModel<SearchResponse>> { result ->
    lifecycleScope.launch {
      when (result) {
        is ResultModel.Loading -> {
          handleProgress(isLoading = result.isLoading ?: false)
        }
        is ResultModel.Success -> {
          onSuccessweather(data = result.data)
        }
        is ResultModel.Failure -> {
          onFail()
        }
      }
    }
  }


  private fun handleCitySelection(item: SearchResponse) {
    viewModel.fetchWeatherData(item.name)
    handleSearchLayout(false)
    hideErrorAndRefresh()

  }


  private val searchDataObserver = Observer<ResultModel<SearchResponse>> { result ->
    lifecycleScope.launch {
      when (result) {
        is ResultModel.Loading -> {
          handleProgress(isLoading = result.isLoading ?: false)
        }
        is ResultModel.Success -> {
          onSuccess(data = result.data)
        }
        is ResultModel.Failure -> {
         // onFail()
        }
      }
    }
  }


  private val forecastDataObserver = Observer<ResultModel<ForecastResponse>> { result ->
    lifecycleScope.launch {
      when (result) {
        is ResultModel.Loading -> {
          handleProgress(isLoading = result.isLoading ?: false)
        }
        is ResultModel.Success -> {
          forcastnSuccess(data = result.data)
        }
        is ResultModel.Failure -> {
          onFail()
        }
      }
    }
  }

  private fun forcastnSuccess(data: ForecastResponse) {

    foreCastAdapter.submitList(data)
  }


  private fun handleProgress(isLoading: Boolean) {
    if (isLoading)
      binding.progressBar.visibility = View.VISIBLE
    else
      binding.progressBar.visibility = View.GONE

  }

  @SuppressLint("SetTextI18n")
  private fun onSuccess(data: SearchResponse) {
    showCitiesRecycler()
    initCitiesList()

    var list: ArrayList<SearchResponse> = arrayListOf()
    list.add(data)
    searchItemsAdapter.submitList(list)
  }

  private fun showCitiesRecycler() {
    binding.searchLayout.recyclerView.visibility = View.VISIBLE
    binding.searchLayout.bottomLayoutClose.visibility = View.VISIBLE
  }

  @SuppressLint("SetTextI18n")
  private fun onSuccessweather(data: SearchResponse) {
    handleProgress(isLoading = false)


    binding.txtAddress.text = viewModel.localCity

    tempC = data.main.temp

    binding.txtTemp.text = "$tempC°C"

    binding.txtCondition.text = data.weather[0].description


    Glide.with(this)
      .applyDefaultRequestOptions(RequestOptions().centerCrop())
      .load("https://openweathermap.org/img/w/${data.weather[0].icon}.png")

      .into(binding.imgCondition)

    binding.txtDate.text = data.weather[0].main


    binding.txtHumidity.text = "${data.main.humidity} %"
    binding.txtWindSpeed.text = "${data.wind.speed} kph"

  }

  private fun onFail() {
    handleProgress(isLoading = false)
    handleError(isError = true)

  }


}
