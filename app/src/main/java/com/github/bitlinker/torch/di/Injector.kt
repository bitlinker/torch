package com.github.bitlinker.torch.di

import android.content.Context

class Injector private constructor() {
    companion object {
        val INSTANCE = Injector()
    }

    private lateinit var appScope: AppScope
    private var uiScope: UiScope? = null

    fun init(appContext: Context) {
        appScope = AppScope(appContext)
    }

    fun getOrCreateUiScope(): UiScope {
        if (uiScope == null) {
            uiScope = UiScope(appScope)
        }
        return uiScope!!
    }

    fun deleteUiScope() {
        val scope = uiScope
        if (scope != null) {
            scope.close()
            uiScope = null
        }

    }
}