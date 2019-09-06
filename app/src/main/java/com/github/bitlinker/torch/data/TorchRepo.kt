package com.github.bitlinker.torch.data

import com.github.bitlinker.torch.data.camera.CameraWrapper

class TorchRepo(private val cameraWrapper: CameraWrapper) {
    var state = false
        private set

    fun isSupported() = cameraWrapper.isSupported()

    fun toggle() {
        state = if (state) {
            cameraWrapper.disableFlashlight()
            false
        } else {
            cameraWrapper.enableFlashlight()
            true
        }
    }

    fun turnOff() {
        if (state) cameraWrapper.disableFlashlight()
    }
}