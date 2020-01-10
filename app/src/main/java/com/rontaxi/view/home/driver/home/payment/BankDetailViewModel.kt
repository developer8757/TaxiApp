package com.rontaxi.view.home.driver.home.payment

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.cache.saveUser
import com.rontaxi.view.home.rider.payment.DeleteCardLiveData
import com.rontaxi.view.home.rider.payment.PaymentListLiveData

class BankDetailViewModel(val app: Application) : AndroidViewModel(app) {


    lateinit var paymentListLiveData: PaymentListDriverLiveData

    fun getBankAccount() {

        paymentListLiveData.addList()
    }

    lateinit var deleteCardLiveData: DeleteCardLiveData

    fun deleteAccount(id: String) {
        deleteCardLiveData.getDeletePaymentId(id)
    }

    fun saveUser() {
        saveUser(app, deleteCardLiveData.value!!.body()!!.data!!.userObj!!)
    }


}