package com.rontaxi.retrofit

import com.dizzipay.railsbank.base.BaseError
import okhttp3.ResponseBody
import org.json.JSONObject

fun getErrorMessage(responseBody: ResponseBody): BaseError {

    try {
        val jsonObject = JSONObject(responseBody.string())

        return BaseError(jsonObject.getInt("statusCode"), jsonObject.getString("message"))

    } catch (e: Exception) {
        return BaseError(101, e.message!!)
    }

}


fun getErrorMessage(jsonObject: JSONObject): BaseError {


    try {
        return BaseError(jsonObject.getInt("statusCode"), jsonObject.getString("message"))

    } catch (e: Exception) {
        return BaseError(101, e.message!!)
    }
}