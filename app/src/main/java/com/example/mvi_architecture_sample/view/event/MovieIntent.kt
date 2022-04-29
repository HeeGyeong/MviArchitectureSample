package com.example.mvi_architecture_sample.view.event

import com.example.mvi_architecture_sample.base.iface.IIntent

sealed class MovieIntent : IIntent {
    object SearchUser : MovieIntent()
}