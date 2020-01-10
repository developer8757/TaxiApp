package com.rontaxi.view.home.rider.schedulebooking

import android.arch.lifecycle.LiveData
import android.content.Context
import com.dizzipay.railsbank.base.BaseResponseModel
import com.google.gson.Gson
import com.rontaxi.model.Booking
import com.rontaxi.model.CreateBookingRequestModel
import com.rontaxi.model.RiderHistoryResponseModel
import com.rontaxi.model.map.Address
import com.rontaxi.model.nearbycabs.CabsDetails
import com.rontaxi.retrofit.ApiService
import com.rontaxi.util.ProgressDialog
import es.dmoral.toasty.Toasty
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.sql.Timestamp
import java.util.*

class ScheduleBookingLiveData(val apiService: ApiService, val context: Context) :
    LiveData<Response<BaseResponseModel>>() {

    fun scheduleBooking(
        selectedCab: CabsDetails,
        pickUpAddress: Address,
        dropOffAddress: Address,
        tentativePrice: String,
        scheduledDate: Date
    ) {


        val createBookingModel = CreateBookingRequestModel()
        createBookingModel.startingPoint.lat = pickUpAddress.lat
        createBookingModel.startingPoint.log = pickUpAddress.lng
        createBookingModel.startingPoint.bearing = 0f

        createBookingModel.endingPoint.lat = dropOffAddress.lat
        createBookingModel.endingPoint.log = dropOffAddress.lng
        createBookingModel.endingPoint.bearing = 0f

        createBookingModel.carId = selectedCab.carId


        createBookingModel.pickupAddress = pickUpAddress.address!!

        createBookingModel.dropAddress = dropOffAddress.address!!

        createBookingModel.tentativeBudget = tentativePrice

        createBookingModel.scheduleDate = scheduledDate.time


        apiService.scheduleBooking(createBookingModel).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object :
                Observer<Response<BaseResponseModel>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<BaseResponseModel>) {
                    value = t
                }

                override fun onError(e: Throwable) {
                    Toasty.info(context, e.localizedMessage).show()
                    ProgressDialog.hideProgressBar()
                }
            })
    }

}