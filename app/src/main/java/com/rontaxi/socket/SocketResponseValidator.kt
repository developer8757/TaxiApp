package com.rontaxi.socket

import android.util.Log
import com.dizzipay.railsbank.base.BaseError
import org.json.JSONArray
import org.json.JSONObject


fun isSocketResponseValid(jsonArray: JSONArray): Pair<JSONObject?, BaseError?> {

    var jsonObject = jsonArray[0] as JSONObject

    Log.i("SocketResponseValidator", "" + jsonArray[0])

    if (jsonObject.getInt("statusCode") == 200) {
        var jsonObject = jsonArray[0] as JSONObject
        return Pair(jsonObject, null)


    } else {
        val baseError = BaseError()
        val jsonObject = jsonArray[0] as JSONObject
        baseError.message = jsonObject.getString("message")
        baseError.code = jsonObject.optInt("statusCode")
        return Pair(null, baseError)
    }

}


fun isSocketResponseSuccess(jsonObject: JSONObject): Boolean {

    if (jsonObject.getBoolean("status")) {

        return true
    }


    return false

}

