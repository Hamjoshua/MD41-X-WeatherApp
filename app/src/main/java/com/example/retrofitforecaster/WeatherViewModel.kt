package com.example.retrofitforecaster

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class WeatherViewModel : ViewModel() {
    private val _weatherData = MutableLiveData<DataResponce>()
    val weatherData: LiveData<DataResponce> get() = _weatherData

    fun fetchWeather(){
        val daysApi = RetrofitHelper.getInstance().create(DayGetter::class.java)

        val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
            throwable.printStackTrace()
        }

        viewModelScope.launch{
            val days = daysApi.check()

            withContext(Dispatchers.Main){
                if(days.body() != null){
                    var dataResponce : DataResponce = days.body()!!
                    Log.d("Days go by", days.body().toString())
                    _weatherData.value = dataResponce

                }
            }
        }

    }
}

interface DayGetter {
    @GET("forecast?q=Shklov,by&appid=${BuildConfig.API_KEY_OPEN_WEATHER_MAP}&units=metric")
    suspend fun check() : Response<DataResponce>
}

object RetrofitHelper {
    val baseUrl = "http://api.openweathermap.org/data/2.5/"
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

