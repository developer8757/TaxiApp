package com.rontaxi.util

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.rontaxi.RonTaxiApp
import es.dmoral.toasty.Toasty
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

fun getTime(textView: TextView, context: Context) {

    val cal = Calendar.getInstance()

    val FIFTEEN_MINUTE_IN_MILLIS = 15 * 60000



    textView.text =
        formatDate(
            Date(
                cal.timeInMillis + (2 * FIFTEEN_MINUTE_IN_MILLIS)
            ), "HH:mm"
        ) + "-" + formatDate(
            Date(
                cal.timeInMillis + (3 * FIFTEEN_MINUTE_IN_MILLIS)
            ), "HH:mm"
        )

    try {
        cal.set(
            Calendar.MINUTE, formatDate(
                Date(
                    cal.timeInMillis + (2 * FIFTEEN_MINUTE_IN_MILLIS)
                ), "HH:mm"
            ).split(":")[1].toInt()
        )

    } catch (e: Exception) {
        e.printStackTrace()
    }


    val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->

        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)

        if (cal.timeInMillis > System.currentTimeMillis() + (2 * FIFTEEN_MINUTE_IN_MILLIS)) {

            textView.text =
                SimpleDateFormat("HH:mm").format(cal.time) + "-" + SimpleDateFormat("HH:mm").format(
                    Date(
                        cal.timeInMillis + (1 * FIFTEEN_MINUTE_IN_MILLIS)
                    )
                )
        } else {
            Toasty.error(
                RonTaxiApp.context,
                "Scheduled Time should be atleast 30 minutes later from the current time",
                Toast.LENGTH_LONG,
                true
            ).show()
        }


    }

    textView.setOnClickListener {
        TimePickerDialog(
            context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                Calendar.MINUTE
            ), true
        ).show()
    }
}

fun getDate(textView: TextView, context: Context) {

    var cal = Calendar.getInstance()

    textView.text = formatDate(cal.time)

    val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            textView.text = formatDate(cal.time)

        }

    textView.setOnClickListener {
        var dialog = DatePickerDialog(
            context, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        dialog.datePicker.minDate = System.currentTimeMillis() - 1000
        dialog.show()
    }
}