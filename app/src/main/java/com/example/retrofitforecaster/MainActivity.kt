package com.example.retrofitforecaster

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitforecaster.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel: WeatherViewModel by viewModels<WeatherViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ставим тулбарчик
        setSupportActionBar(findViewById(R.id.toolbar))

        // Ставим наблюдателя за погодой
        setObserverToWeather()

        // Даем кнопке право менять погоду
        bindFetchButtonToViewModel()

        // Иницализация данных на вьюхе
        initBinding()
    }

    fun initBinding(){
        binding.cityNameTb.setText(viewModel.cityName.value)
        binding.isCelciaSw.setChecked(viewModel.isCelcia.value!!)

        viewModel.fetchWeather(viewModel.isCelcia.value!!, viewModel.cityName.value!!)
    }
    fun bindFetchButtonToViewModel(){
        binding.fetchWeatherBtn.setOnClickListener {
            viewModel.fetchWeather(binding.isCelciaSw.isChecked, binding.cityNameTb.text.toString())
        }
    }

    fun setObserverToWeather(){
        viewModel.weatherData.observe(this, Observer { item ->
            if(item != null){
                Log.d("ListAdapter", "Calling update from MainActivity")

                fetchListAdapter(item.list.toMutableList())

                Toast.makeText(this, "Погода для города" +
                        " ''${binding.cityNameTb.getText()}''", Toast.LENGTH_LONG)
                    .show()
            }
            else{
                Log.d("ListAdapter", "Empty response")
                Toast.makeText(this, "Город с таким названием " +
                        "не существует", Toast.LENGTH_LONG)
                    .show()
            }

        })
    }

    fun fetchListAdapter(list: MutableList<DayPrognosis>){
        val rView: RecyclerView = binding.rView
        rView.layoutManager = LinearLayoutManager(this)

        val adapter : DayListAdapter = DayListAdapter()
        adapter.submitList(list)
        rView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        return true
    }
}



