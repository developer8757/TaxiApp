package com.rontaxi.view.home.rider.payment

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import com.dizzipay.railsbank.base.BaseResponseModel
import com.rontaxi.retrofit.ApiService
import com.rontaxi.util.ProgressDialog
import com.rontaxi.view.home.rider.ridehistory.BlockDriverModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

@SuppressLint("CheckResult")
class BlockDriverLiveData (val apiService: ApiService): LiveData<Response<BaseResponseModel>>(){

    fun blockDriver(data: BlockDriverModel){

        apiService.blockDriver(data)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object: Observer<Response<BaseResponseModel>> {

                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<BaseResponseModel>) {
                    postValue(t)
                }

                override fun onError(e: Throwable) {
                    ProgressDialog.hideProgressBar()
                }
            })

    }

}