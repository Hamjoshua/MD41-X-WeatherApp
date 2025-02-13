package com.example.retrofitforecaster

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("temp") val temp: Double
){
    fun getTempAsString() : String{
        return "${temp}° C"
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
