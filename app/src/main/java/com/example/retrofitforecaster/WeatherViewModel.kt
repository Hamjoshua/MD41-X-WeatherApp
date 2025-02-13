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
import retrofit2.http.Query

class WeatherViewModel : ViewModel() {
    private val _weatherData = MutableLiveData<DataResponce>()
    private val _cityName = MutableLiveData<String>("Shklov")
    private val _isCelcia = MutableLiveData<Boolean>(true)
    val weatherData: LiveData<DataResponce> get() = _weatherData
    val cityName: LiveData<String> = _cityName
    val isCelcia: LiveData<Boolean> = _isCelcia

    fun fetchWeather(){
        val daysApi = RetrofitHelper.getInstance().create(DayGetter::class.java)

        val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
            throwable.printStackTrace()
        }

        viewModelScope.launch{
            var units = "standard"
            if(isCelcia.value!!){
                units = "metrics"
            }

            val days = daysApi.check(cityName.value!!, units)

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

