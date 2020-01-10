package com.rontaxi.view.home.driver.home.profile.basicinfo

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.model.login.LoginResponseModel
import com.rontaxi.util.ProgressDialog
import com.rontaxi.view.home.rider.profile.UpdateProfileRequestModel
import com.rontaxi.view.initial.driver.register.DriverRegisterRequest
import es.dmoral.toasty.Toasty
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class UpdateDriverProfileLiveData(val apiService: ApiService, val context: Context) :
    LiveData<Response<LoginResponseModel>>() {

    @SuppressLint("CheckResult")
    fun updateDriverProfile(updateUserInfoModel: DriverRegisterRequest) {

        apiService.updateDriverProfile(
            updateUserInfoModel.firstName,
            updateUserInfoModel.lastName,
            updateUserInfoModel.email,
            updateUserInfoModel.phone,
            updateUserInfoModel.licenseNumber,
            updateUserInfoModel.licenseIssuedOn,
            updateUserInfoModel.licenseExpiryDate,
            updateUserInfoModel.carNumberId,
            updateUserInfoModel.brand,
            updateUserInfoModel.model,
            updateUserInfoModel.year,
            updateUserInfoModel.color,
            updateUserInfoModel.carId,
            updateUserInfoModel.driverImage,
            updateUserInfoModel.permitDoc,
            updateUserInfoModel.insuranceDoc,
            updateUserInfoModel.licenseDoc,
            updateUserInfoModel.regDoc,
            updateUserInfoModel.vehicleImage

        )

            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : Observer<Response<LoginResponseModel>> {
                override fun onComplete() {
                }


                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<LoginResponseModel>) {
                    value = t
                }

                override fun onError(e: Throwable) {

                    ProgressDialog.hideProgressBar()
                    Toasty.error(context, e.localizedMessage).show()
                }
            })


    }
}