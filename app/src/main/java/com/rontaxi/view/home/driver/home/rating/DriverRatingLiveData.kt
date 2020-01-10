package com.rontaxi.view.home.driver.home.rating

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.model.driverrating.DriverRatingResponseModel
import com.rontaxi.util.ProgressDialog
import es.dmoral.toasty.Toasty
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


class DriverRatingLiveData(val apiService: ApiService, val context: Context):
    LiveData<Response<DriverRatingResponseModel>>()  {

    @SuppressLint("CheckResult")
    fun getDriverRatings(offset : Int){

        apiService.getDriverRatings(offset)

            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: Observer<Response<DriverRatingResponseModel>> {
                override fun onComplete() {
                }


                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<DriverRatingResponseModel>) {
                    value=t
                }

                override fun onError(e: Throwable) {

                    ProgressDialog.hideProgressBar()
                    Toasty.error(context,e.localizedMessage).show()
                }
            })


    }
}