package com.rontaxi.view.home.driver.home.payment

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.cache.saveUser
import com.rontaxi.model.paymentdriver.AddBankModel

class AddBankViewModel(val app: Application) : AndroidViewModel(app) {
    lateinit var addBankLiveData: AddBankLiveData

    fun addBankAccount(accountToken: String) {
        addBankLiveData.addBankAccount(AddBankModel(accountToken))
    }

    fun saveUser() {
        saveUser(app, addBankLiveData.value!!.body()!!.data!!.userObj!!)
    }

}