package com.rontaxi.view.home.rider.help

import android.arch.lifecycle.LiveData
import com.dizzipay.railsbank.base.BaseResponseModel
import com.rontaxi.retrofit.ApiService
import com.rontaxi.model.SubmitQueryRequest
import com.rontaxi.util.ProgressDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class SubmitQueryLiveData(val apiService: ApiService): LiveData<Response<BaseResponseModel>>() {

    fun submitQuery(request: SubmitQueryRequest){
        apiService.sendQuery(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: Observer<Response<BaseResponseModel>>{

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