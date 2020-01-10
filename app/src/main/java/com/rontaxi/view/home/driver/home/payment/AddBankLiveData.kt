package com.rontaxi.view.home.driver.home.payment

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import com.rontaxi.model.login.LoginResponseModel
import com.rontaxi.model.paymentdriver.AddBankModel
import com.rontaxi.retrofit.ApiService
import com.rontaxi.util.ProgressDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


class AddBankLiveData(val apiService: ApiService) : LiveData<Response<LoginResponseModel>>() {

    @SuppressLint("CheckResult")
    fun addBankAccount(bankDetail: AddBankModel) {
        apiService.addBankAccount(bankDetail)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
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
                }
            })

    }

}
