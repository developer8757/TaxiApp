package com.rontaxi.view.home.rider.payment

import android.annotation.SuppressLint
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

class PaymentListLiveData(val apiService: ApiService, val context: Context) : LiveData<Response<PaymentListModel>>() {

    @SuppressLint("CheckResult")
    fun addList() {
        apiService.getCardList().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
            .subscribeWith(object : Observer<Response<PaymentListModel>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<PaymentListModel>) {
                    value = t
                }

                override fun onError(e: Throwable) {
                    Toasty.info(context, e.localizedMessage).show()
                    ProgressDialog.hideProgressBar()
                }
            })
    }




}