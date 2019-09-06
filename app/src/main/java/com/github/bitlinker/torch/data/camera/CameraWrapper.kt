@file:Suppress("DEPRECATION")

package com.github.bitlinker.torch.data.camera

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import java.io.Closeable
import java.io.IOException

class CameraWrapper(private val context: Context) : Closeable {
    private var camera: Camera? = null

    private fun lazyOpenCamera(): Camera {
        if (camera == null) camera = Camera.open()
        return camera!!
    }

    fun enableFlashlight() {
        synchronized(this) {
            try {
                val cam = lazyOpenCamera()
                val p = cam.parameters
                p.flashMode = Camera.Parameters.FLASH_MODE_TORCH
                cam.parameters = p
                cam.startPreview()
            } catch (e: Exception) {
                throw IOException("Can't open camera with flashlight setting", e)
            }
        }
    }

    fun disableFlashlight() {
        synchronized(this) {
            try {
                val cam = camera
                if (cam != null) {
                    cam.stopPreview()
                    cam.release()
                    camera = null
                }
            } catch (e: Exception) {
                throw IOException("Can't stop camera preview", e)
            }
        }
    }

    fun isSupported() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

    override fun close() {
        synchronized(this) {
            camera?.release()
            camera = null
        }
    }
}