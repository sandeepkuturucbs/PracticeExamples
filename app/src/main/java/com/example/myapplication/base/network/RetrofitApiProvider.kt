package com.example.myapplication.base.network

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitApiProvider {
    fun <T> provideApiService(baseUrl: String, modelClass: Class<T>, converterFactories: Array<out Converter.Factory>? = null): T {

        val retrofitBuilder = Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        converterFactories?.forEach { factory ->
            retrofitBuilder.addConverterFactory(factory)
        }
        return retrofitBuilder
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(EmptyToNullConverterFactory())
            .baseUrl(baseUrl)
            .build()
            .create(modelClass)
    }
}