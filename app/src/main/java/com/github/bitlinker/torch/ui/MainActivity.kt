package com.github.bitlinker.torch.ui

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.bitlinker.torch.R
import com.github.bitlinker.torch.di.Injector
import kotlinx.android.synthetic.main.activity_main_layout.*
import pub.devrel.easypermissions.EasyPermissions


class MainActivity : AppCompatActivity(), MainView, EasyPermissions.PermissionCallbacks {
    companion object {
        const val REQUEST_CODE_CAMERA_PERMISSIONS = 1
    }

    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = Injector.INSTANCE.getOrCreateUiScope().presenter
        setContentView(R.layout.activity_main_layout)
        btnOnOff.setOnClickListener { presenter.toggle() }
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        presenter.detachView()
        super.onPause()
    }

    override fun onDestroy() {
        if (isFinishing) {
            presenter.finish()
            Injector.INSTANCE.deleteUiScope()
        }
        super.onDestroy()
    }

    override fun setButtonState(state: Boolean) {
        btnOnOff.isSelected = state
    }

    override fun showError(error: ErrorType) {
        Toast.makeText(this, getErrorText(error), Toast.LENGTH_LONG).show()
    }

    private fun getErrorText(error: ErrorType) = getString(
        when (error) {
            ErrorType.NOT_SUPPORTED -> R.string.error_not_supported
            ErrorType.PERMISSION -> R.string.error_no_permissions
            ErrorType.UNKNOWN -> R.string.error_generic
        }
    )

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    override fun requestPermissions() {
        val perms = Manifest.permission.CAMERA
        if (EasyPermissions.hasPermissions(this, perms)) {
            presenter.onPermissionsResult(true)
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.camera_permissions_rationale),
                REQUEST_CODE_CAMERA_PERMISSIONS,
                perms
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) =
        presenter.onPermissionsResult(false)

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) =
        presenter.onPermissionsResult(true)
}