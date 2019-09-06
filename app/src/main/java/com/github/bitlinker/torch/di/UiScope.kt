package com.github.bitlinker.torch.di

import com.github.bitlinker.torch.business.MainInteractor
import com.github.bitlinker.torch.data.TorchRepo
import com.github.bitlinker.torch.data.camera.CameraWrapper
import com.github.bitlinker.torch.ui.MainPresenter
import java.io.Closeable

class UiScope(private val appScope: AppScope) : Closeable {
    private val cameraWrapperDelegate = lazy { CameraWrapper(appScope.appContext) }
    private val cameraWrapper by cameraWrapperDelegate
    private val repo: TorchRepo by lazy { TorchRepo(cameraWrapper) }
    private val interactor by lazy { MainInteractor(repo) }
    val presenter by lazy { MainPresenter(interactor) }

    override fun close() {
        if (cameraWrapperDelegate.isInitialized()) {
            cameraWrapper.close()
        }
    }
}