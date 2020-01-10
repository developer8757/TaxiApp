package com.rontaxi.util

import android.widget.Toast
import com.rontaxi.RonTaxiApp
import es.dmoral.toasty.Toasty
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

val myFormat = "dd.MM.yyyy" // mention the format you need
val sdf = SimpleDateFormat(myFormat)

fun formatDate(date: Date, formatPattern: String = myFormat): String {
    sdf.applyPattern(formatPattern)

    return sdf.format(date)
}


fun parseDate(formattedDate: String, formatPattern: String = myFormat): Date? {
    try {
        sdf.applyPattern(formatPattern)

        return sdf.parse(formattedDate)
    } catch (e: Exception) {
        Toasty.error(
            RonTaxiApp.context,
            "Invalid Date, Parsing Exception",
            Toast.LENGTH_SHORT,
            true
        ).show()
    }

    return null
}