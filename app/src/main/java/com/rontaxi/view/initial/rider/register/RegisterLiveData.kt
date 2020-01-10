package com.rontaxi.view.initial.rider.register

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

class RegisterLiveData(val apiService: ApiService, val context: Context): LiveData<Response<RegistrationResponseModel>>() {

    fun registerRider(request: RegisterRequestModel){

        apiService.registerRider(request.firstName!!,request.lastName!!,
            request.email!!,request.phone!!,request.password!!,request.device!!,
            request.userActiveRole!!)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: Observer<Response<RegistrationResponseModel>> {
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