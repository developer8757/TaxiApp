package com.rontaxi.view.initial.driver.upload

import android.arch.lifecycle.LiveData
import android.content.Context
import com.dizzipay.railsbank.base.BaseResponseModel
import com.rontaxi.retrofit.ApiService
import com.rontaxi.util.ProgressDialog
import es.dmoral.toasty.Toasty
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class ValidateDocsLiveData(val apiService: ApiService, val context: Context): LiveData<Response<BaseResponseModel>>() {


    fun validDocs(docNumber: String, type: Int, phoneNumber: String?){

        apiService.validateDocs(docNumber,type,phoneNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: Observer<Response<BaseResponseModel>> {


                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<BaseResponseModel>) {
                    value=t

                }

                override fun onError(e: Throwable) {
                    ProgressDialog.hideProgressBar()
                    Toasty.error(context,e.localizedMessage).show()

                }
            })

    }
}