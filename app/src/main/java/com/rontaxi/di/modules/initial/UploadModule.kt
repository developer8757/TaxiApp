package com.rontaxi.di.modules.initial

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.socket.SocketManager
import com.rontaxi.view.home.driver.home.profile.basicinfo.UpdateDriverProfileLiveData
import com.rontaxi.view.home.logout.LogoutLiveData
import com.rontaxi.view.initial.driver.register.DriverRegisterLiveData
import com.rontaxi.view.initial.driver.upload.UploadDocsViewModel
import com.rontaxi.view.initial.driver.upload.UploadDocumentFragment
import dagger.Module
import dagger.Provides


@Module
class UploadModule {


    @Provides
    fun provideUploadViewModel(
        fragment: UploadDocumentFragment,
        socketManager: SocketManager,
        apiService: ApiService,
        context: Context
    ): UploadDocsViewModel {

        val viewModel = ViewModelProviders.of(fragment).get(UploadDocsViewModel::class.java)
        viewModel.registerLiveData = DriverRegisterLiveData(apiService, context)
        viewModel.updateDriverProfileLiveData = UpdateDriverProfileLiveData(apiService, context)
        viewModel.logoutLiveData = LogoutLiveData(context, apiService)
        viewModel.socketManager = socketManager



        return viewModel

    }
}