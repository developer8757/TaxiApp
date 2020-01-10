package com.rontaxi.view.home.driver.home.earnings

import android.arch.lifecycle.LiveData
import com.rontaxi.retrofit.ApiService
import com.rontaxi.model.drivingearning.DriverEarningModel
import com.rontaxi.util.ProgressDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class DriverEarningLiveData(val apiService: ApiService):LiveData<Response<DriverEarningModel>>() {

fun driverEarning(type:Int,offSet:Int){
    apiService.driverEarning(type,offSet)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribeWith(object: Observer<Response<DriverEarningModel>> {

            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: Response<DriverEarningModel>) {
                value=t
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideProgressBar()
            }
        })

}

}