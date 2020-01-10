package com.dizzipay.railsbank.data.remote

import com.dizzipay.railsbank.base.BaseResponseModel

object ResponseValidator {

    fun <T : BaseResponseModel>isResponseValid(model: T): Boolean{

        if(model.statusCode==200){
            return true
        }

        return false

    }

}