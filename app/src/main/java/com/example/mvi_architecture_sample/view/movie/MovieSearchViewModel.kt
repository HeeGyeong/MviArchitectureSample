package com.example.mvi_architecture_sample.view.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvi_architecture_sample.api.ApiInterface
import com.example.mvi_architecture_sample.base.iface.IModel
import com.example.mvi_architecture_sample.util.NetworkManager
import com.example.mvi_architecture_sample.view.event.MovieIntent
import com.example.mvi_architecture_sample.view.event.MovieState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * MovieSearchActivity 에 사용되는 VM
 *
 * 해당 Activity 에서 사용되는 UseCase 를 모두 파라미터로 받는다.
 */
class MovieSearchViewModel(
    private val userApi: ApiInterface,
    private val networkManager: NetworkManager,
) :
    ViewModel(), IModel<MovieState, MovieIntent> {

    override val intents: Channel<MovieIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableLiveData<MovieState>().apply { value = MovieState() }
    override val state: LiveData<MovieState>
        get() = _state

    var searchText: String? = null

    init {
        intentConsumer()
    }

    private fun intentConsumer() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect { userIntent ->
                if (!networkManager.checkNetworkState()) {
                    networkError()
                } else {
                    when (userIntent) {
                        MovieIntent.SearchUser -> fetchData()
                    }
                }
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateState { it.copy(isLoading = true, errorMessage = null) }

                flow {
                    emit(userApi.getSearchMovieFlow(searchText ?: "null"))
                }.collect { movie ->
                    if (movie.movies.isNotEmpty()) {
                        updateState {
                            it.copy(isLoading = false, users = movie.movies, errorMessage = null)
                        }
                    } else {
                        updateState { it.copy(isLoading = false, errorMessage = "Do not found") }
                    }
                }
            } catch (e: Exception) {
                updateState { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    private fun networkError() {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(isLoading = false, errorMessage = "Network Error") }
        }
    }

    private suspend fun updateState(handler: suspend (intent: MovieState) -> MovieState) {
        _state.postValue(handler(state.value!!))
    }
}

