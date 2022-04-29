package com.example.mvi_architecture_sample.view.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvi_architecture_sample.api.ApiInterface
import com.example.mvi_architecture_sample.base.iface.IModel
import com.example.mvi_architecture_sample.util.NetworkManager
import com.example.mvi_architecture_sample.view.event.MovieIntent
import com.example.mvi_architecture_sample.view.event.MovieSideEffect
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
    ViewModel(), IModel<MovieState, MovieIntent, MovieSideEffect> {

    override val intents: Channel<MovieIntent> = Channel(Channel.UNLIMITED)
    override val sideEffect: Channel<MovieSideEffect> = Channel(Channel.UNLIMITED)

    private val _state = MutableLiveData<MovieState>().apply { value = MovieState() }
    override val state: LiveData<MovieState>
        get() = _state

    // default 값에 따라서 초기에 1회 호출이 되는 것 주의.
    private val _navigation = MutableLiveData<String>().apply { value = "" }
    val navigation = _navigation

    var searchText: String? = null

    init {
        // Init Consumer setting
        intentConsumer()
        sideEffectConsumer()
    }

    private fun intentConsumer() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect { userIntent ->
                if (!networkManager.checkNetworkState()) {
                    networkError()
                } else {
                    when (userIntent) {
                        MovieIntent.SearchMovie -> fetchData()
                        MovieIntent.NavigateToMainActivity -> sideEffect.send(MovieSideEffect.NavigateToMainActivity)
                    }
                }
            }
        }
    }

    // sideEffect 확장에 따라 호출하는 부분도 변경 필요
    private fun sideEffectConsumer() {
        viewModelScope.launch {
            sideEffect.consumeAsFlow().collect { sideEffect ->
                when (sideEffect) {
                    MovieSideEffect.NavigateToMainActivity -> updateNavigation { "main" }
                    else -> updateNavigation { "movie" }
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

    private suspend fun updateNavigation(handler: suspend (intent: String) -> String) {
        _navigation.postValue(handler(navigation.value.toString()))
    }
}

