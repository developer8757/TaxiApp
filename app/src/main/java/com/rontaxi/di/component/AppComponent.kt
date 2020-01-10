package com.rontaxi.di.component

import android.app.Application
import com.rontaxi.di.modules.AppModule
import com.di.module.ContextModule
import com.rontaxi.RonTaxiApp
import com.rontaxi.di.builder.ActivityBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class, AppModule::class, ActivityBuilder::class,ContextModule::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance fun application(app: Application): Builder
        fun build(): AppComponent
    }

    fun inject(app: RonTaxiApp)
}