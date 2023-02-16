package com.swenson.android.task.data.repo

import com.swenson.android.task.data.helper.ComplexPreferencesImpl
import com.swenson.android.task.data.response.SearchResponse
import com.swenson.android.task.network.Constants.REPOS_LIST_PREF_NAME

class LocalSearchedRepository(private val complexPreferences: ComplexPreferencesImpl) {
    private val TAG = "LocalFavouritePlanetsRepository"

    fun saveFavouritePlanet(searchResponse: SearchResponse ) {
        val list = getFavouritePlanetList()
        list.add(searchResponse)
        complexPreferences.saveArrayList( list , REPOS_LIST_PREF_NAME)
    }

     fun getFavouritePlanetList(): ArrayList<SearchResponse> {
         val list = complexPreferences.getArrayList(REPOS_LIST_PREF_NAME)

         return if (list.isNullOrEmpty()){
             (arrayListOf())
         }else
             list
    }

    fun removeFavouritePlanet(searchResponse: SearchResponse) {
        val list = getFavouritePlanetList()
        list.remove(searchResponse)
        complexPreferences.saveArrayList( list , REPOS_LIST_PREF_NAME)
    }

}