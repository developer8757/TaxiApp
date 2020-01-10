package com.rontaxi.view.home.rider.payment

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

class AddCardViewModel (app: Application): AndroidViewModel(app) {
    lateinit var paymentLiveData: PaymentLiveData

        fun saveCardUsingToken(token: String) {
        paymentLiveData.addCard(AddCardModel(token))
    }

}