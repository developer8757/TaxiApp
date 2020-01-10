package com.rontaxi.view.home.rider.payment


import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.content.Context
import com.dizzipay.railsbank.base.BaseResponseModel
import com.rontaxi.model.login.LoginResponseModel
import com.rontaxi.retrofit.ApiService
import com.rontaxi.util.ProgressDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class DeleteCardLiveData(val apiService: ApiService) : LiveData<Response<LoginResponseModel>>() {

    @SuppressLint("CheckResult")
    fun getDeletePaymentId(cardId : String) {
        apiService.deleteCard(cardId)
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

