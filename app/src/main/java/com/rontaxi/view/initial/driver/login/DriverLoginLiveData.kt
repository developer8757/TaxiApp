package com.rontaxi.view.initial.driver.login

import android.arch.lifecycle.LiveData
import android.content.Context
import com.google.gson.Gson
import com.rontaxi.retrofit.ApiService
import com.rontaxi.model.login.LoginResponseModel
import com.rontaxi.util.ProgressDialog
import es.dmoral.toasty.Toasty
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class DriverLoginLiveData(val apiService: ApiService, val context: Context) :
    LiveData<Response<LoginResponseModel>>() {


    fun login(loginRequestModel: LoginRequestModel) {

        apiService.login(loginRequestModel)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : Observer<Response<LoginResponseModel>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<LoginResponseModel>) {
                    value = t
                }

                override fun onError(e: Throwable) {
                    ProgressDialog.hideProgressBar()
                    Toasty.error(context, e.localizedMessage).show()
                }
            })
    }
}