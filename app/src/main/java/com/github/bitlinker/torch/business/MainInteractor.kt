package com.github.bitlinker.torch.business

import com.github.bitlinker.torch.data.PermissionProvider
import com.github.bitlinker.torch.data.TorchRepo
import com.github.bitlinker.torch.domain.exception.NotSupportedException
import com.github.bitlinker.torch.domain.exception.PermissionDeniedException

class MainInteractor(private val repo: TorchRepo) {
    interface ToggleCallback {
        fun result(errorType: Throwable?)
    }

    fun getButtonState(): Boolean = repo.state

    fun toggle(callback: ToggleCallback, permissionProvider: PermissionProvider) {
        if (!repo.isSupported()) callback.result(NotSupportedException("Camera not supported"))
        else {
            permissionProvider.checkOrRequestPermissions(object :
                PermissionProvider.PermissionsCallback {
                override fun onGranted() {
                    try {
                        repo.toggle()
                        callback.result(null)
                    } catch (e: Throwable) {
                        callback.result(e)
                    }
                }

                override fun onDenied() {
                    callback.result(PermissionDeniedException("No camera permission"))
                }
            })
        }
    }

    fun turnOff() {
        repo.turnOff()
    }
}