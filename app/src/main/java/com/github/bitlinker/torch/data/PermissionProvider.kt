package com.github.bitlinker.torch.data

interface PermissionProvider {
    interface PermissionsCallback {
        fun onGranted()
        fun onDenied()
    }

    fun checkOrRequestPermissions(callback: PermissionsCallback)
}