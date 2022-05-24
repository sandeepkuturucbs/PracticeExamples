package com.example.myapplication.restaurantdata.network

import androidx.annotation.WorkerThread
import com.example.myapplication.base.network.RetrofitApiProvider
import com.example.myapplication.restaurantdata.model.RestaurantDTO
import com.example.myapplication.restaurantdata.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

class RestaurantsAPI {
    private interface Api {
        @GET("/android/v1/feed")
        suspend fun getStoreListAsync(
            @Query("lat") latitude: Double,
            @Query("lng") longitude: Double
        ): Response<List<RestaurantDTO?>?>
    }

    companion object {
        @WorkerThread
        suspend fun getStoreListAsync(lat: Double, lng: Double): Response<List<RestaurantDTO?>?> {
            return getService().getStoreListAsync(lat, lng)
        }

        private fun getService(): Api {
            return RetrofitApiProvider.provideApiService(Constants.RESTAURANTS_BASE_URL, Api::class.java)
        }
    }
}