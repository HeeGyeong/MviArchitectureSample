package com.example.mvi_architecture_sample.base

import android.app.Application
import com.example.mvi_architecture_sample.di.apiModule
import com.example.mvi_architecture_sample.di.networkModule
import com.example.mvi_architecture_sample.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)

            modules(
                apiModule,
                networkModule,
                viewModelModule
            )
        }
    }
}