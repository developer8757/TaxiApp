package com.rontaxi.view.home.rider.payment

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

class PaymentViewModel (app:Application): AndroidViewModel(app){
    lateinit var paymentListLiveData: PaymentListLiveData
    lateinit var defaultPaymentLiveData: DefaultPaymentLiveData
    lateinit var deleteCardLiveData: DeleteCardLiveData


    fun getCard() {

        paymentListLiveData.addList()
    }

    fun getPaymentMethod(id:String)
    {
        defaultPaymentLiveData.getDefaultPayment(DefaultPaymentModel(id))
    }

    fun deleteSelectedCard(id : String)
    {
        deleteCardLiveData.getDeletePaymentId(id)
    }
}