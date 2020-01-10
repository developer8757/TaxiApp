package com.rontaxi.view.home.rider.payment

import com.dizzipay.railsbank.base.BaseResponseModel


class PaymentListModel: BaseResponseModel(){

    var data: ArrayList<Payment>?=null

}

class PaymentListDriverModel:  BaseResponseModel(){

    var data: PaymentData?=null

}

class PaymentData{

    var payment: ArrayList<Payment>?=null
    var isAccountAdded=0
}

class Payment {
    var id: String? = null
    var category: Int? = null
    var type: String? = null
    var isSelected = false
    val brand: String? = null
    val exp_month: Int? = null
    val exp_year: Int? = null
    val last4: String? = null
    val bank_name:String?=null
}
