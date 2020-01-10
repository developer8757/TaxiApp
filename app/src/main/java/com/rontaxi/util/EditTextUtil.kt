package com.rontaxi.util

import android.widget.EditText

fun EditText.setEnable(boolean: Boolean){


    this.setFocusableInTouchMode(boolean)
    this.isFocusable=boolean
}