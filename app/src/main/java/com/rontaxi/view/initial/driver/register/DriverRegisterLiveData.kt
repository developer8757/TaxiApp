package com.rontaxi.view.initial.driver.register

import android.arch.lifecycle.LiveData
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.model.registration.RegistrationResponseModel
import com.rontaxi.util.ProgressDialog
import es.dmoral.toasty.Toasty
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class DriverRegisterLiveData(val apiService: ApiService, val context: Context): LiveData<Response<RegistrationResponseModel>>()  {

    fun registerDriver(driverRegister: DriverRegisterRequest){

        apiService.registerDriver(driverRegister.firstName!!, driverRegister.lastName!!,
            driverRegister.email!!,driverRegister.phone!!,driverRegister.licenseNumber!!, driverRegister.licenseIssuedOn!!,
            driverRegister.licenseExpiryDate!!,driverRegister.carNumberId!!, driverRegister.brand!!, driverRegister.model!!, driverRegister.year!!, driverRegister.color!!,
            driverRegister.carId!!, driverRegister.device!!, driverRegister.password!!,driverRegister.userActiveRole!!,
            driverRegister.driverImage!!,driverRegister.permitDoc!!,driverRegister.insuranceDoc!!, driverRegister.licenseDoc!!,
            driverRegister.regDoc!!,driverRegister.vehicleImage!!)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: Observer<Response<RegistrationResponseModel>>{
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<RegistrationResponseModel>) {
                    value=t
                }

                override fun onError(e: Throwable) {

                    ProgressDialog.hideProgressBar()
                    Toasty.error(context,e.localizedMessage).show()
                }
            })
    }

}