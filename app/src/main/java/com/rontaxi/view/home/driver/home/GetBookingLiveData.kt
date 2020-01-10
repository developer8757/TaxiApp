package com.rontaxi.view.home.driver.home

import android.arch.lifecycle.LiveData
import io.reactivex.Observer
import android.content.Context
import com.rontaxi.model.BookingResponseModel
import com.rontaxi.model.login.LoginResponseModel
import com.rontaxi.retrofit.ApiService
import com.rontaxi.util.ProgressDialog
import com.rontaxi.view.initial.driver.login.LoginRequestModel
import retrofit2.Response
import es.dmoral.toasty.Toasty
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class GetBookingLiveData(val apiService: ApiService, val context: Context) :
    LiveData<Response<BookingResponseModel>>() {


    fun getBookingData(bookingId: String) {

        apiService.getBookingData(bookingId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : Observer<Response<BookingResponseModel>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<BookingResponseModel>) {
                    value = t
                }

                override fun onError(e: Throwable) {
                    ProgressDialog.hideProgressBar()
                    Toasty.error(context, e.localizedMessage).show()
                }
            })
    }
}