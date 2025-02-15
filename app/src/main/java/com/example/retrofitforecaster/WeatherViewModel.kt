package com.example.retrofitforecaster

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel : ViewModel() {
    private var _cityName = MutableLiveData<String>()
    private var _isCelcia = MutableLiveData<Boolean>()
    private var _weatherData = MutableLiveData<DataResponce?>()
    val weatherData: LiveData<DataResponce?> get() = _weatherData
    val cityName: LiveData<String> = _cityName
    val isCelcia: LiveData<Boolean> = _isCelcia

    init{
        _cityName.value = "Shklov"
        _isCelcia.value = true
        // fetchWeather(isCelcia.value!!, _cityName.value!!)
    }

    fun updateValues(externalIsCelcia: Boolean, externalCityName: String){
        _isCelcia.value = externalIsCelcia
        _cityName.value = externalCityName
    }

    fun fetchWeather(isCelcia: Boolean, cityName: String){
        val daysApi = RetrofitHelper.getInstance().create(DayGetter::class.java)

        viewModelScope.launch{
            var units = "imperial"
            if(isCelcia){
                units = "metric"
            }

            Log.d("ViewModel", "Requesting with pars - city: ${cityName}," +
                    " celcia: ${isCelcia}")
            val days = daysApi.check(cityName, units)

            withContext(Dispatchers.Main){
                var dataResponce: DataResponce? = null
                if(days.body() != null){
                    dataResponce = days.body()
                    dataResponce?.list?.forEach{
                        it.main.isCelcia = isCelcia
                    }
                    _weatherData.value = dataResponce!!

                    // Если запрос прошел, значит, данные можно сохранить
                    updateValues(isCelcia, cityName)
                }
                else{
                    _weatherData.value = null
                }
            }
        }

    }
}

