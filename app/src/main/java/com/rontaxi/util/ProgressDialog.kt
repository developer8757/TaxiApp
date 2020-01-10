package com.rontaxi.util

import android.app.AlertDialog
import android.content.Context
import dmax.dialog.SpotsDialog

object ProgressDialog {

    var dialog: AlertDialog?=null

    fun showProgressBar(context: Context, message: String, isCancellable: Boolean){

        dialog=SpotsDialog.Builder()
            .setContext(context)
            .setMessage(message)
            .setCancelable(isCancellable)
            .build()
            .apply {


                show()
            }

    }

    fun hideProgressBar(){

        dialog?.apply {

            dismiss()
        }


    }


}