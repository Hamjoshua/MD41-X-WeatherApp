package com.example.retrofitforecaster

import android.os.Bundle
import android.util.Log
import android.view.Menu
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
            Log.d("ListAdapter", "Calling update from MainActivity")
            val adapter : DayListAdapter = DayListAdapter()
            adapter.submitList(item.list.toMutableList())
            rView.adapter = adapter
        })

        viewModel.fetchWeather()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        return true
    }
}



