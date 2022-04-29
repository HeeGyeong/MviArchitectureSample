package com.example.mvi_architecture_sample.di

import com.example.mvi_architecture_sample.util.NetworkManager
import org.koin.core.module.Module
import org.koin.dsl.module

val networkModule: Module = module {
    single { NetworkManager(get()) }
}