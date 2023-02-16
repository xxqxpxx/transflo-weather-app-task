package com.swenson.android.task.di


import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.swenson.android.task.network.APPInterceptor
import com.swenson.android.task.network.ApiService
import com.swenson.android.task.network.Constants
import com.swenson.android.task.network.Constants.API_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
  @Provides
  @Singleton
  fun provideGson(): Gson {
    return GsonBuilder().create()
  }

  @Provides
  @Singleton
  fun provideOkHttpClient(chuckerInterceptor: ChuckerInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(APPInterceptor())
      .connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
      .readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
      .writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
      .addInterceptor(chuckerInterceptor)
      .build()
  }

  @Provides
  @Singleton
  fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl(Constants.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(CoroutineCallAdapterFactory())
      .build()
  }

  @Singleton
  @Provides
  fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
    return ChuckerInterceptor.Builder(context).build()
  }

  @Provides
  @Singleton
  fun provideApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
  }
}