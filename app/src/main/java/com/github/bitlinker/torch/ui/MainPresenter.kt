package com.github.bitlinker.torch.ui

import com.github.bitlinker.torch.business.MainInteractor
import com.github.bitlinker.torch.data.PermissionProvider
import com.github.bitlinker.torch.domain.exception.NotSupportedException
import com.github.bitlinker.torch.domain.exception.PermissionDeniedException

class MainPresenter(private val interactor: MainInteractor) : PermissionProvider {
    private var permissionsCallback: PermissionProvider.PermissionsCallback? = null
    private var view: MainView? = null

    fun attachView(view: MainView) {
        this.view = view
        updateButtonState()
    }

    private fun updateButtonState() {
        view?.setButtonState(interactor.getButtonState())
    }

    fun detachView() {
        this.view = null
    }

    fun toggle() {
        interactor.toggle(object : MainInteractor.ToggleCallback {
            override fun result(errorType: Throwable?) {
                updateButtonState()
                if (errorType != null) view?.showError(mapError(errorType))
            }
        }, this)
    }

    private fun mapError(error: Throwable): ErrorType {
        return when (error) {
            is NotSupportedException -> ErrorType.NOT_SUPPORTED
            is PermissionDeniedException -> ErrorType.PERMISSION
            else -> ErrorType.UNKNOWN
        }
    }

    fun onPermissionsResult(isGranted: Boolean) {
        if (isGranted) permissionsCallback?.onGranted() else permissionsCallback?.onDenied()
        permissionsCallback = null
    }

    override fun checkOrRequestPermissions(callback: PermissionProvider.PermissionsCallback) {
        val v = view
        if (v != null) {
            permissionsCallback = callback
            v.requestPermissions()
        } else {
            callback.onDenied()
        }
    }

    fun finish() {
        try {
            interactor.turnOff()
        } catch (e: Throwable) {
        }
    }
}