package com.example.mvi_architecture_sample.view.event

import com.example.mvi_architecture_sample.base.iface.ISideEffect

sealed class MovieSideEffect : ISideEffect {
    object NavigateToMainActivity : MovieSideEffect()
}