package com.rontaxi.di.modules.locationsearch

import android.arch.lifecycle.ViewModelProviders
import com.rontaxi.view.location.LocationActivity
import com.rontaxi.view.location.LocationSearchAdapter
import com.rontaxi.view.location.LocationViewModel
import com.rontaxi.view.location.RecentSearchAdapter
import dagger.Module
import dagger.Provides


@Module
class LocationSearchModule {

    @Provides
    fun providesLocationViewModel(activity: LocationActivity): LocationViewModel{

        val viewModel=ViewModelProviders.of(activity).get(LocationViewModel::class.java)

        return viewModel

    }

    @Provides
    fun provideLocationSearchAdapter()=LocationSearchAdapter()

    @Provides
    fun provideRecentLocationSearchAdapter()=RecentSearchAdapter()


}