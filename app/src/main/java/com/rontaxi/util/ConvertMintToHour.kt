package com.rontaxi.util

import java.security.Timestamp
import java.text.SimpleDateFormat
import java.util.*



fun convertMintToHour(mint:Int):String{

    val startTime = "00:00"
    val minutes = mint
    val h = minutes / 60 + Integer.parseInt(startTime.substring(0, 1))
    val m = minutes % 60 + Integer.parseInt(startTime.substring(3, 4))
    val newtime = "$h${"h"}:$m${"m"}"
    return newtime
}

