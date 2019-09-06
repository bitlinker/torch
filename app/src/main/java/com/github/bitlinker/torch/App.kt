package com.github.bitlinker.torch

import android.app.Application
import com.github.bitlinker.torch.di.Injector

@Suppress("unused")
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Injector.INSTANCE.init(this)
    }
}