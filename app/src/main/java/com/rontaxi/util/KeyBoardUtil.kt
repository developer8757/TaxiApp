package com.rontaxi.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.util.TypedValue
import android.util.DisplayMetrics



fun hideKeyBoard(context: Context, v: View) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(v!!.getWindowToken(), 0);
}

fun dpToPx(context: Context, valueInDp: Float): Float {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
}