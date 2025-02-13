package com.example.retrofitforecaster

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val DATA_KEY = "DATA_KEY"
    private val viewModel: WeatherViewModel by viewModels<WeatherViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        getSupportActionBar()?.setTitle("Shklov")

        val rView: RecyclerView = findViewById<RecyclerView>(R.id.r_view)
        rView.layoutManager = LinearLayoutManager(this)

        viewModel.weatherData.observe(this, Observer { item ->
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



