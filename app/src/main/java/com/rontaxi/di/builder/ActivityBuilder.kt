package com.rontaxi.di.builder

import com.rontaxi.di.modules.home.HomeRiderModule
import com.rontaxi.di.modules.locationsearch.LocationSearchModule
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.initial.driver.InitialDriverActivity
import com.rontaxi.view.initial.rider.InitialRiderActivity
import com.rontaxi.view.location.LocationActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [AndroidSupportInjectionModule::class,InitialFragmentBuilder::class])
    abstract  fun bindInitialRiderActivity(): InitialRiderActivity

    @ContributesAndroidInjector(modules = [AndroidSupportInjectionModule::class,InitialFragmentBuilder::class])
    abstract  fun bindInitialDriverActivity(): InitialDriverActivity

    @ContributesAndroidInjector(modules = [AndroidSupportInjectionModule::class,HomeFragmentBuilder::class,HomeRiderModule::class])
    abstract  fun bindHomeRiderActivity(): HomeRiderActivity

    @ContributesAndroidInjector(modules = [AndroidSupportInjectionModule::class, LocationSearchModule::class])
    abstract fun bindLocationActivity(): LocationActivity

    @ContributesAndroidInjector(modules = [AndroidSupportInjectionModule::class, HomeFragmentBuilder::class])
    abstract fun bindHomeDriverActivity(): HomeDriverActivity

}