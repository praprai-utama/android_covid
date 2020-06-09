package com.codemobiles.myapp.services

import com.codemobiles.myapp.BASE_URL
import com.codemobiles.myapp.IMAGE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object APIClient {
    private var retrofit: Retrofit? = null

    // singleton pattern
    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    fun getImageURL() = BASE_URL + "${IMAGE_URL}/"
}
