package com.example.mvi_architecture_sample.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mvi_architecture_sample.R
import com.example.mvi_architecture_sample.view.movie.MovieSearchActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btnClick(view: View) {
        when (view.id) {
            R.id.btnRefresh -> {
                startActivity(Intent(this@MainActivity, MovieSearchActivity::class.java))
            }
        }
    }
}

