package com.rontaxi.view.home.rider.ridehistory

import android.arch.lifecycle.LiveData
import android.content.Context
import com.rontaxi.model.Booking
import com.rontaxi.model.RiderHistoryResponseModel
import com.rontaxi.retrofit.ApiService
import com.rontaxi.util.ProgressDialog
import es.dmoral.toasty.Toasty
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class RideHistoryLiveData(val apiService: ApiService, val context: Context) :
    LiveData<Response<RiderHistoryResponseModel>>() {

    /**
     * type : 0 - Past Rides
     *        1 - Upcoming Rides
     */
    fun rideHistory(type: Int, offset: Int) {
        apiService.rideHistory(type, offset).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object :
                Observer<Response<RiderHistoryResponseModel>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<RiderHistoryResponseModel>) {
                    value = t
                }

                override fun onError(e: Throwable) {
                    Toasty.info(context, e.localizedMessage).show()
                    ProgressDialog.hideProgressBar()
                }
            })
    }

}