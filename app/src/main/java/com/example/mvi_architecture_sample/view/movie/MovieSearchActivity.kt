package com.example.mvi_architecture_sample.view.movie

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.mvi_architecture_sample.R
import com.example.mvi_architecture_sample.base.iface.IView
import com.example.mvi_architecture_sample.view.event.MovieIntent
import com.example.mvi_architecture_sample.view.event.MovieState
import kotlinx.android.synthetic.main.activity_movie_search.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieSearchActivity : AppCompatActivity(), IView<MovieState> {
    private val movieAdapter = MovieAdapter()
    private val viewModel by viewModel<MovieSearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_search)
        rv_movies.adapter = movieAdapter

        viewModel.state.observe(this, Observer {
            render(it)
        })
    }

    fun btnClick(view: View) {
        when (view.id) {
            R.id.btn_search -> {
                viewModel.searchText = et_input.text.toString()
                lifecycleScope.launch {
                    viewModel.intents.send(MovieIntent.SearchUser)
                }
            }
        }
    }

    override fun render(state: MovieState) {
        with(state) {
            movieAdapter.submitList(users)

            if (errorMessage != null) {
                Toast.makeText(this@MovieSearchActivity, "$errorMessage", Toast.LENGTH_SHORT).show()
            }
        }
    }
}