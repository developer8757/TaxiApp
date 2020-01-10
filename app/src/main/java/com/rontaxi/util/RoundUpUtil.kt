package com.rontaxi.util

import java.text.DecimalFormat

fun <T> roundUpTo2(t: T): String {
    val f = DecimalFormat("##.00")
    var result = f.format(t)

    if (result.toDouble() == 0.0) {
        return "0.00"
    } else if (result.toDouble() < 1) {
        return "0" + result
    }
    return result
}