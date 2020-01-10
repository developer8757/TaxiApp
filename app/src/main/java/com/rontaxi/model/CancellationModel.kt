package com.rontaxi.model

import com.dizzipay.railsbank.base.BaseResponseModel


class CancellationReasonsResponseModel : BaseResponseModel() {
    var data: ArrayList<ReasonModel>? = null
}

class ReasonModel {
    var reasonId = ""
    var reason = ""
    var isSelected = false
}