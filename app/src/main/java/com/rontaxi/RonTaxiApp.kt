package com.rontaxi

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ProcessLifecycleOwner
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.Log
import com.rontaxi.di.component.AppComponent
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.rontaxi.di.component.DaggerAppComponent
import com.rontaxi.location.LocationUpdateService
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class RonTaxiApp : Application(), HasActivityInjector  {


    companion object {

        lateinit var appComponent: AppComponent
        lateinit var context: Context
    }

    @Inject
    lateinit var dispatchActivityInjector:
            DispatchingAndroidInjector<Activity>


    override fun onCreate() {
        initDagger()
        super.onCreate()



        context = this

        // setFontStyle()
        FirebaseApp.initializeApp(this)

        if (!Places.isInitialized()) {

            //Places.initialize(this, BuildConfig.PLACES_KEY)

            Places.initialize(this, BuildConfig.PLACES_KEY)

        }

    }


    private fun setFontStyle() {

    }

    private fun initDagger() {

        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

        appComponent.inject(this)

    }


    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchActivityInjector
    }








}