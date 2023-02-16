package com.swenson.android.task.ui.landing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.swenson.android.task.data.repo.LocalSearchedRepository
import com.swenson.android.task.base.BaseViewModel
import com.swenson.android.task.data.helper.ComplexPreferencesImpl
import com.swenson.android.task.data.repo.WeatherRepository
import com.swenson.android.task.data.response.ForecastResponse
import com.swenson.android.task.data.response.SearchResponse
import com.swenson.android.task.data.response.WeatherResponse
import com.swenson.android.task.network.ResultModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WeatherLandingViewModel @Inject constructor(private val repository: WeatherRepository) :
  BaseViewModel() {


  var list: List<WeatherResponse> = arrayListOf()

  private val TAG = "WeatherViewModel"

  private val _weatherDataObserver = MutableLiveData<ResultModel<SearchResponse>>()
  val weatherDataObserver: LiveData<ResultModel<SearchResponse>> = _weatherDataObserver

  private val _searchTextDataObserver = MutableLiveData<ResultModel<SearchResponse>>()
  val searchTextDataObserver: LiveData<ResultModel<SearchResponse>> = _searchTextDataObserver


  private val _forecastTextDataObserver = MutableLiveData<ResultModel<ForecastResponse>>()
  val forecastTextDataObserver: LiveData<ResultModel<ForecastResponse>> = _forecastTextDataObserver

  private val _planetsFavDataObserver = MutableLiveData<ArrayList<SearchResponse>>()
  val planetsDataObserverFav: LiveData<ArrayList<SearchResponse>> = _planetsFavDataObserver


  var localCity = ""

  private lateinit var localSearchedRepository: LocalSearchedRepository

  fun setComplexPref(complexPreferences: ComplexPreferencesImpl) {
    localSearchedRepository = LocalSearchedRepository(complexPreferences)
  }


  fun fetchWeatherData(city: String) {

    localCity = city

    _weatherDataObserver.postValue(ResultModel.Loading(isLoading = true))
    viewModelScope.launch {
      repository.fetchWeatherBasedCity(city = city)
        .catch { exception ->
          Log.i(TAG, "Exception : ${exception.message}")
          _weatherDataObserver.value = ResultModel.Failure(code = getStatusCode(throwable = exception))
          _weatherDataObserver.postValue(ResultModel.Loading(isLoading = false))
        }
        .collect { response ->
          Log.i(TAG, "Response : $response")
          _weatherDataObserver.postValue(ResultModel.Success(data = response))
          saveToLocalStorage(response)
        }
    }
  }

  fun searchForCity(city: String) {
    localCity = city
    _searchTextDataObserver.postValue(ResultModel.Loading(isLoading = true))
    viewModelScope.launch {
      repository.searchForCity(city = city)
        .catch { exception ->
          Log.i(TAG, "Exception : ${exception.message}")
          _searchTextDataObserver.value =
            ResultModel.Failure(code = getStatusCode(throwable = exception))
          _searchTextDataObserver.postValue(ResultModel.Loading(isLoading = false))
        }
        .collect { response ->
          Log.i(TAG, "Response : $response")
          _searchTextDataObserver.postValue(ResultModel.Success(data = response))
        }
    }
  }


  fun fetchForeCast(lat: String, lon: String) {

    _forecastTextDataObserver.postValue(ResultModel.Loading(isLoading = true))
    viewModelScope.launch {
      repository.fetchForecastBasedCity(lat, lon)
        .catch { exception ->
          Log.i(TAG, "Exception : ${exception.message}")
          _forecastTextDataObserver.value =
            ResultModel.Failure(code = getStatusCode(throwable = exception))
          _forecastTextDataObserver.postValue(ResultModel.Loading(isLoading = false))
        }
        .collect { response ->
          Log.i(TAG, "Response : $response")
          _forecastTextDataObserver.postValue(ResultModel.Success(data = response))
        }
    }
  }


  fun refresh(city: String) {
    fetchWeatherData(city)
  }

  private fun updateFaviconandstatus(list: ArrayList<SearchResponse>) {
    _planetsFavDataObserver.postValue(list)
  }

  fun handleIfAlreadyFavourite() {
    viewModelScope.launch {

      var list = localSearchedRepository.getFavouritePlanetList()
      if (!list.isEmpty()) {
        updateFaviconandstatus(list)
      }

    }
  }


  private fun saveToLocalStorage(planet : SearchResponse) {
    viewModelScope.launch {
      localSearchedRepository.saveFavouritePlanet(planet)
    }
  }

}