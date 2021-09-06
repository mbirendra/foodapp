package com.example.foodbook.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
   private const val BASE_URL = "http://10.0.2.2:88/"
//    private const val BASE_URL = "http://localhost:88/"
//        private const val BASE_URL = "http://192.168.1.101:88/"

    var token: String? = null

    private val okHttp = OkHttpClient.Builder()

    private val retrofitBuilder =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .client(okHttp.build())

    private val retrofit = retrofitBuilder.build()

    // Load image path in Service Builder class
    fun loadImagePath(): String {
       var array = BASE_URL.split("/")


        return array[0]+"/"+array[2]+"/"
    }

    var toggle : Boolean = false

    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }

}