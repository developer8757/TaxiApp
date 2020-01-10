package com.rontaxi.di.modules.initial

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.view.initial.driver.vehicletype.VehicleTypeAdapter
import com.rontaxi.view.initial.driver.vehicletype.VehicleTypeFragment
import com.rontaxi.view.initial.driver.vehicletype.VehicleTypeLiveData
import com.rontaxi.view.initial.driver.vehicletype.VehicleTypeViewModel
import dagger.Module
import dagger.Provides


@Module
class VehicleTypeModule {


    @Provides
    fun provideVehicleTypeViewModel(fragment: VehicleTypeFragment, apiService: ApiService, context: Context):  VehicleTypeViewModel{
        val viewModel=ViewModelProviders.of(fragment).get(VehicleTypeViewModel::class.java)
        viewModel.vehicleTypeLiveData= VehicleTypeLiveData(apiService, context)

        return viewModel
    }


    @Provides
    fun provideVehicleTypeAdapter()=VehicleTypeAdapter()


}