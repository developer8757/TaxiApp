package com.rontaxi.view.home.rider.help

import android.arch.lifecycle.LiveData
import android.util.Log
import com.dizzipay.railsbank.base.BaseResponseModel
import com.rontaxi.retrofit.ApiService
import com.rontaxi.model.RaiseIssueRequest
import com.rontaxi.util.ProgressDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class RaiseIssueLiveData(val apiService: ApiService) : LiveData<Response<BaseResponseModel>>() {

    fun raiseIssueQuery(request: RaiseIssueRequest) {
        apiService.raiseIssue(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : Observer<Response<BaseResponseModel>> {

                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<BaseResponseModel>) {
                    postValue(t)
                }

                override fun onError(e: Throwable) {
                    Log.i("TAG", "The Raise Issue Live Data: $e")
                    ProgressDialog.hideProgressBar()
                }
            })

    }

}