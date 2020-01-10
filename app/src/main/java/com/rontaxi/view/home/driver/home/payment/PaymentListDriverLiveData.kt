package com.rontaxi.view.home.driver.home.payment


import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.util.ProgressDialog
import com.rontaxi.view.home.rider.payment.PaymentListDriverModel
import es.dmoral.toasty.Toasty
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class PaymentListDriverLiveData(val apiService: ApiService, val context: Context) : LiveData<Response<PaymentListDriverModel>>() {

    @SuppressLint("CheckResult")
    fun addList() {
        apiService.getCardListDriver().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
            .subscribeWith(object : Observer<Response<PaymentListDriverModel>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<PaymentListDriverModel>) {
                    value = t
                }

                override fun onError(e: Throwable) {
                    Toasty.info(context, e.localizedMessage).show()
                    ProgressDialog.hideProgressBar()
                }
            })
    }




}