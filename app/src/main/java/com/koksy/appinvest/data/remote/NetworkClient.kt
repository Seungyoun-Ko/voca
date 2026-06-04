package com.koksy.appinvest.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {
    private const val BASE_URL = "https://api.example.com/"

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val stockApiService: StockApiService by lazy {
        retrofit.create(StockApiService::class.java)
    }
}
