package com.example.mvi_architecture_sample.base.iface

import androidx.lifecycle.LiveData
import kotlinx.coroutines.channels.Channel

interface IModel<S: IState, I: IIntent, SE: ISideEffect> {
    val intents: Channel<I>
    val sideEffect: Channel<SE>
    val state: LiveData<S>
}