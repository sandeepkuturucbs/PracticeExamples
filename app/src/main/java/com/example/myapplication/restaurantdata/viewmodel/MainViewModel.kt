package com.example.myapplication.restaurantdata.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.base.network.UIResource
import com.example.myapplication.restaurantdata.model.RestaurantDTO
import com.example.myapplication.restaurantdata.network.RestaurantsAPI
import com.example.myapplication.restaurantdata.view.fragment.MainFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class MainViewModel : ViewModel() {
    companion object {
        const val TAG: String = "MainViewModel"
    }

    // Fragment navigation
    private val replaceFragmentMutableLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val replaceFragmentLiveData: LiveData<String> = replaceFragmentMutableLiveData

    private val showProgressMutableLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val showProgressLiveData: LiveData<Boolean> = showProgressMutableLiveData


    init {
        replaceFragmentMutableLiveData.value = MainFragment.TAG
        showProgressMutableLiveData.value = true
    }

    // Restaurant Data
    private val restaurantMLData: MutableLiveData<UIResource<List<RestaurantDTO?>?>> by lazy {
        MutableLiveData<UIResource<List<RestaurantDTO?>?>>()
    }

    val restaurantLData: LiveData<UIResource<List<RestaurantDTO?>?>>
        get() = restaurantMLData

    fun getRestaurantsList(lat: Double, lng: Double) {
        restaurantMLData.postValue(UIResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RestaurantsAPI.getStoreListAsync(lat, lng)
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let { restaurantDTO ->
                        restaurantMLData.postValue(UIResource.success(restaurantDTO))
                    }
                } else {
                    restaurantMLData.postValue(UIResource.error(response.message(), null, null, response.code()))
                }
            } catch (e: HttpException) {
                Log.e(TAG, e.toString())
                restaurantMLData.postValue(UIResource.error("Error Occurred while fetching Stores list: $e", e, null))
            } catch (e: IOException) {
                Log.e(TAG, e.toString())
                restaurantMLData.postValue(UIResource.error("Network not available error message: $e", e, null))
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                restaurantMLData.postValue(UIResource.error("Error Occurred while fetching Stores List: $e", e, null))
            }
        }
    }

    fun setShowProgress(show: Boolean){
        showProgressMutableLiveData.postValue(show)
    }
}