package com.example.retrofitforecaster

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface DayGetter {
    @GET("forecast?appid=${BuildConfig.API_KEY_OPEN_WEATHER_MAP}")
    suspend fun check(@Query("q") cityName: String,
                      @Query("units") units: String) : Response<DataResponce>
}

object RetrofitHelper {
    val baseUrl = "http://api.openweathermap.org/data/2.5/"
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
