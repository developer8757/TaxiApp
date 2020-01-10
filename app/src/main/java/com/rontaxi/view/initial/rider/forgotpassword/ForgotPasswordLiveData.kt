package com.rontaxi.view.initial.rider.forgotpassword

import android.arch.lifecycle.LiveData
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.util.ProgressDialog
import es.dmoral.toasty.Toasty
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class ForgotPasswordLiveData(val apiService: ApiService, val context: Context): LiveData<Response<ForgotResponse>>() {


    fun forgotPassword(request: ForgotRequest){
        apiService.forgotPassword(request)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: Observer<Response<ForgotResponse>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<ForgotResponse>) {
                    value=t
                }

                override fun onError(e: Throwable) {


                    ProgressDialog.hideProgressBar()
                    Toasty.error(context,e.localizedMessage).show()
                }
            })
    }



}