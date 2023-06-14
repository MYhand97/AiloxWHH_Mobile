package com.example.myapplication.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServer {
    val baseURL: String = "http://23.98.69.156/ai-one-old/core/whh-test/"
    var retro: Retrofit? = null

    fun koneksiRetrofit(): Retrofit {
        if (retro == null) {
            retro = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseURL)
                .build()
        }
        return retro!!
    }
}