package com.example.retrofitforecaster

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitforecaster.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel: WeatherViewModel by viewModels<WeatherViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        setSupportActionBar(findViewById(R.id.toolbar))

        val rView: RecyclerView = findViewById<RecyclerView>(R.id.r_view)
        rView.layoutManager = LinearLayoutManager(this)

        viewModel.weatherData.observe(this, Observer { item ->
            if(item != null){
                Log.d("ListAdapter", "Calling update from MainActivity")
                val adapter : DayListAdapter = DayListAdapter()
                adapter.submitList(item.list.toMutableList())
                rView.adapter = adapter
                Toast.makeText(this, "Погода для города" +
                        " ''${viewModel.binder.cityName}''", Toast.LENGTH_LONG)
                    .show()
            }
            else{
                Log.d("ListAdapter", "Empty response")
                Toast.makeText(this, "Город с таким названием " +
                        "не существует", Toast.LENGTH_LONG)
                    .show()
            }

        })

        viewModel.fetchWeather()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        return true
    }
}



