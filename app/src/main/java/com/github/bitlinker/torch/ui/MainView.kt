package com.github.bitlinker.torch.ui

interface MainView {
    fun requestPermissions()

    fun setButtonState(state: Boolean)

    fun showError(error: ErrorType)
}
