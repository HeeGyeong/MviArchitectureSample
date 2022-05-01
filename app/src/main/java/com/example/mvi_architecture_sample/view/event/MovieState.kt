package com.example.mvi_architecture_sample.view.event

import com.example.mvi_architecture_sample.base.iface.IState
import com.example.mvi_architecture_sample.model.MovieEntity

data class MovieState(
    val movies: List<MovieEntity> = listOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : IState