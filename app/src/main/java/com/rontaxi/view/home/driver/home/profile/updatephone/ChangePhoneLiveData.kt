package com.rontaxi.view.home.driver.home.profile.updatephone

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import com.rontaxi.retrofit.ApiService
import com.rontaxi.model.login.LoginResponseModel
import com.rontaxi.util.ProgressDialog
import com.rontaxi.view.initial.driver.register.PhoneRequestModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class ChangePhoneLiveData(val apiService: ApiService): LiveData<Response<LoginResponseModel>>() {

    @SuppressLint("CheckResult")
    fun changePhone(request: PhoneRequestModel){

        apiService.changePhone(request)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object: Observer<Response<LoginResponseModel>> {

                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }


                override fun onNext(t: Response<LoginResponseModel>) {
                    postValue(t)
                }

                override fun onError(e: Throwable) {
                    ProgressDialog.hideProgressBar()
                }
            })

    }
}