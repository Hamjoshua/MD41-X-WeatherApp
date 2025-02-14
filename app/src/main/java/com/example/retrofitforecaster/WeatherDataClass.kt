package com.example.retrofitforecaster

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("temp") val temp: Double,
    var isCelcia: Boolean
){
    fun getTempAsString() : String{
        if(isCelcia){
            return "${temp}° C"
        }
        else{
            return "${temp}° F"
        }

    }
}
data class Weather(
    @SerializedName("main") val main: String,
    @SerializedName("icon") val icon: String
)
data class DayPrognosis (
    @SerializedName("dt_txt") val dt_txt: String,
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: ArrayList<Weather>
)

data class DataResponce(
    @SerializedName("list") val list: ArrayList<DayPrognosis>
)
