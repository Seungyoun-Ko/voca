package com.koksy.appinvest.data.remote

import retrofit2.http.GET

interface StockApiService {
    @GET("health")
    suspend fun health(): Unit
}
