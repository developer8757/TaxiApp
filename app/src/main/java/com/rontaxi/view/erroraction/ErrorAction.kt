package com.rontaxi.view.erroraction

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.dizzipay.railsbank.base.BaseError
import com.dizzipay.railsbank.base.BaseResponseModel
import com.rontaxi.R
import com.rontaxi.cache.getPhone
import com.rontaxi.cache.getUser
import com.rontaxi.cache.saveToken
import com.rontaxi.model.login.LoginResponseModel
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.showAlert
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.view.home.driver.home.HomeDriverFragment
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.initial.driver.InitialDriverActivity
import com.rontaxi.view.initial.driver.otp.DriverOtpFragment
import com.rontaxi.view.initial.rider.InitialRiderActivity
import com.rontaxi.view.initial.rider.otp.OtpFragment
import es.dmoral.toasty.Toasty
import okhttp3.ResponseBody


fun showErrorMessage(context: Context,message: String){
    Toasty.error(context,message).show()
}


fun <T: BaseResponseModel>isResponseValid(activity: FragmentActivity,baseResponseModel: T): Boolean{

   // var error= getErrorMessage(errorBody)

    when(baseResponseModel.statusCode){

        471->{
            showAlert(activity, baseResponseModel.message,activity.getString(R.string.verify)) {
                var phone = getPhone(activity)


                when(baseResponseModel){

                    is LoginResponseModel->{
                        saveToken(activity,baseResponseModel.data!!.loginToken)
                    }
                }


                when (activity){

                    is InitialRiderActivity ->{

                        var fragment=OtpFragment()
                        fragment.phone=phone!!
                        fragment.navigation=OtpFragment.NAVIGATION.UNVERIFIED

                        activity.replaceFragment(fragment,true)
                    }

                    is InitialDriverActivity->{
                        var fragment=DriverOtpFragment()
                        fragment.phone=phone!!
                        fragment.navigation=DriverOtpFragment.NAVIGATION.UNVERIFIED

                        activity.replaceFragment(fragment,true)
                    }

                    is HomeRiderActivity->{
                        var fragment=OtpFragment()
                        fragment.phone=phone!!
                        fragment.navigation=OtpFragment.NAVIGATION.UNVERIFIED

                        activity.replaceFragment(fragment,true)


                    }


                    is HomeDriverActivity->{
                        var fragment=DriverOtpFragment()
                        fragment.phone=phone!!
                        fragment.navigation=DriverOtpFragment.NAVIGATION.UNVERIFIED

                        activity.replaceFragment(fragment,true)


                    }
                }

            }
            return false
        }


    }


    return true


    }



fun tackleError(activity: Activity,errorBody: BaseError){




    when(errorBody.code){

        401->{

            showAlert(activity, errorBody.message,activity.getString(R.string.ok)) {

                when (activity){
                    is HomeRiderActivity->{

                        activity.startActivity(Intent(activity!!,InitialRiderActivity::class.java))
                        activity.finish()
                    }

                }


            }

        }

        400->{
            showAlert(activity, errorBody.message,activity.getString(R.string.ok),{})


        }



    }
   // when(errorBody.)



}

