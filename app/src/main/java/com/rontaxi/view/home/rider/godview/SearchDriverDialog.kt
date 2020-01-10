package com.rontaxi.view.home.rider.godview

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.rontaxi.R
import kotlinx.android.synthetic.main.dialog_searching_driver.*

class SearchDriverDialog(context: Context): Dialog(context) {

    lateinit var searchDriverDialogInterface:SearchDriverDialogInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_searching_driver)

        setCancelable(false)

        setOnClick()


        animationView.setAnimation(R.raw.anim_search)
        animationView.loop(true)
        animationView.playAnimation()

    }

    private fun setOnClick() {

        btnCancel.setOnClickListener {

            searchDriverDialogInterface.onCancelBooking()
            dismiss()

        }


    }

    interface SearchDriverDialogInterface{

        fun onCancelBooking()

    }


}