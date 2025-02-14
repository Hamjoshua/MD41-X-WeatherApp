package com.example.retrofitforecaster

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
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
    private var _weatherData = MutableLiveData<DataResponce>()
    val weatherData: LiveData<DataResponce> get() = _weatherData
    val binder = Binder()

    class Binder : BaseObservable() {
        private var _isCelcia: Boolean = true

        @Bindable
        fun getCelcia(): Boolean {
            return _isCelcia
        }

        fun setCelcia(value: Boolean) {
            _isCelcia = value
            notifyPropertyChanged(BR.celcia)
        }

        @get:Bindable
        var cityName: String = "Shklov"
            set(value){
                field = value
                notifyPropertyChanged(com.example.retrofitforecaster.BR.cityName)
            }

    }

    fun fetchWeather(){
        val daysApi = RetrofitHelper.getInstance().create(DayGetter::class.java)

        val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
            throwable.printStackTrace()
        }

        viewModelScope.launch{
            var units = "imperial"
            if(binder.getCelcia()){
                units = "metric"
            }

            Log.d("ViewModel", "Requesting with pars - city: ${binder.cityName}," +
                    " celcia: ${binder.getCelcia()}")
            val days = daysApi.check(binder.cityName, units)

            withContext(Dispatchers.Main){
                if(days.body() != null){
                    var dataResponce : DataResponce = days.body()!!
                    dataResponce.list.forEach{
                        it.main.isCelcia = binder.getCelcia()
                    }
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

