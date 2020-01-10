package com.rontaxi.view.home.rider.godview

import android.arch.lifecycle.LiveData
import com.rontaxi.retrofit.ApiService
import com.rontaxi.model.CancellationReasonsResponseModel
import com.rontaxi.util.ProgressDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class GetCancelReasonsLiveData(val apiService: ApiService): LiveData<Response<CancellationReasonsResponseModel>>() {

    fun getCancellationReasons(type: Int){
        apiService.getCancellationReasons(type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: Observer<Response<CancellationReasonsResponseModel>>{

                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<CancellationReasonsResponseModel>) {

                    postValue(t)
                }

                override fun onError(e: Throwable) {
                    ProgressDialog.hideProgressBar()

                }
            })


    }

}