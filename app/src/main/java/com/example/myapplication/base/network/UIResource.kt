package com.example.myapplication.base.network

import java.lang.Exception

data class UIResource<out T>(val status: Status, val data: T?, val message: String?, val responseCode: Int?, val exception: Exception?) {
    companion object {

        @JvmOverloads
        @JvmStatic
        fun <T> success(data: T? = null, responseCode: Int? = null): UIResource<T> {
            return UIResource(Status.SUCCESS, data, null, responseCode, null)
        }

        @JvmOverloads
        @JvmStatic
        fun <T> error(msg: String? = null, exception: Exception? = null, data: T? = null, responseCode: Int? = null): UIResource<T> {
            return UIResource(Status.ERROR, data, msg, responseCode, exception)
        }

        @JvmOverloads
        @JvmStatic
        fun <T> loading(data: T? = null): UIResource<T> {
            return UIResource(Status.LOADING, data, null, null, null)
        }
    }
}