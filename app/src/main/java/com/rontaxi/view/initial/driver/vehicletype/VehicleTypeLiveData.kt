package com.rontaxi.view.initial.driver.vehicletype

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

class VehicleTypeLiveData(val apiService: ApiService, val context: Context): LiveData<Response<CarTypeResponse>>() {

    fun getCarType(){
        apiService.getCarType()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: Observer<Response<CarTypeResponse>>{


                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<CarTypeResponse>) {
                        value=t

                }

                override fun onError(e: Throwable) {
                    ProgressDialog.hideProgressBar()
                    Toasty.error(context,e.localizedMessage).show()

                }
            })

    }

}