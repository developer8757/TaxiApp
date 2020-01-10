package com.rontaxi.di.modules


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.dizzipay.railsbank.data.remote.ApiConstants
import com.rontaxi.retrofit.ApiService
import com.rontaxi.retrofit.TaxiInterceptor
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.rontaxi.socket.SocketManager
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class AppModule {

     val TAG="APP_MODULE"


    @Provides
    @Singleton
    fun providerInterceptor(context: Context): TaxiInterceptor {
        return TaxiInterceptor(context)
    }


    @Provides
    @Singleton
    internal fun provideOkHttpClient(interceptor: TaxiInterceptor): OkHttpClient {

        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(ApiConstants.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
        okHttpClient.readTimeout(ApiConstants.READ_TIMEOUT, TimeUnit.MILLISECONDS)
        okHttpClient.writeTimeout(ApiConstants.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level=(HttpLoggingInterceptor.Level.BODY)
        okHttpClient.addInterceptor(interceptor)
        okHttpClient.addInterceptor(logInterceptor)

        return okHttpClient.build()
    }


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): ApiService {

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(ApiService::class.java)

    }

    @Provides
    fun provideSharedPrefrence(context: Context): SharedPreferences{
        return PreferenceManager.getDefaultSharedPreferences(context)
    }


    @Provides
    fun provideSocket(): Socket{
        return IO.socket(ApiConstants.BASE_URL)
    }


    @Singleton
    @Provides
    fun provideSocketManager(socket: Socket,context: Context) : SocketManager{
        return SocketManager(socket,context)
    }

}


