package com.rontaxi.view.initial.rider.otp

import android.arch.lifecycle.LiveData
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.model.otp.OtpResponse
import com.rontaxi.util.ProgressDialog
import es.dmoral.toasty.Toasty
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class OtpVerificationLiveData(val apiService: ApiService, val context: Context): LiveData<Response<OtpResponse>>() {

    fun otpVerification(otp: String,token:String?){
        apiService.otpVerification(otp.toInt(),token)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: Observer<Response<OtpResponse>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<OtpResponse>) {
                    value=t
                }

                override fun onError(e: Throwable) {

                    ProgressDialog.hideProgressBar()
                    Toasty.error(context,e.localizedMessage).show()
                }
            })

    }

}