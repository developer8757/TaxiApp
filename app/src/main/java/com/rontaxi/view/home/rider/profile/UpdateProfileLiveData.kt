package com.rontaxi.view.home.rider.profile

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.content.Context
import com.rontaxi.retrofit.ApiService
import com.rontaxi.model.login.LoginResponseModel
import com.rontaxi.util.ProgressDialog
import es.dmoral.toasty.Toasty
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class UpdateProfileLiveData(val apiService: ApiService, val context: Context):LiveData<Response<LoginResponseModel>>() {


    @SuppressLint("CheckResult")
    fun updateProfile(user: UpdateProfileRequestModel){

        var observable: Observable<Response<LoginResponseModel>>
        if(user.profilePhoto!=null){


            observable=    apiService.updateRiderProfileWithPhoto(user.profilePhoto!!,user.firstName!!, user.lastName!!, user.email!!)

        }else {

            observable=  apiService.updateRiderProfile(user.firstName!!, user.lastName!!, user.email!!)

        }


        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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