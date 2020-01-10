package com.rontaxi.view.home.driver.home

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Window
import com.rontaxi.BuildConfig
import com.rontaxi.R
import com.rontaxi.model.NotifyDriverResponseModel
import com.rontaxi.util.loadImage
import kotlinx.android.synthetic.main.alert_dialog_request.*
import android.content.res.AssetFileDescriptor
import com.rontaxi.model.Booking


class NotifyDriverDialog(context: Context) : Dialog(context) {


    lateinit var url: String

    lateinit var pickUpAddress: String

    lateinit var tentativePrice: String

    lateinit var notifyDriverActionInterface: NotifyDriverActionInterface

    var mediaPlayer: MediaPlayer? = null

    var time=15000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.alert_dialog_request)

        setCancelable(false)


        mediaPlayer = MediaPlayer.create(context, R.raw.beep)

        mediaPlayer!!.isLooping = true

        mediaPlayer!!.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {

            override fun onPrepared(mp: MediaPlayer?) {
                mediaPlayer!!.start()

            }
        })

        setOnClicks()

        ivPickUpStaticMap.loadImage(context,url)

        tvPickUpAddress.text=pickUpAddress

        tvEarning.text=tentativePrice


    }


    fun setData(bookingObj: Booking) {

        url =
            "https://maps.googleapis.com/maps/api/staticmap?center=${bookingObj!!.startingPoint!!.lat},${bookingObj!!.startingPoint!!.log}&zoom=13&size=600x300&maptype=roadmap&markers=color:red%7C${bookingObj!!.startingPoint!!.lat},${bookingObj!!.startingPoint!!.log}&key=${BuildConfig.GOOGLE_MAP_KEY}"

        pickUpAddress = bookingObj!!.pickupAddress

        tentativePrice = bookingObj!!.tentativeBudget

    }


    override fun onStart() {


        countDownTimer=object: CountDownTimer(time,1000){

            override fun onFinish() {

                this.cancel()

                countDownTimer = null
                this@NotifyDriverDialog.dismiss()

            }

            override fun onTick(millisUntilFinished: Long) {

                tvPendingTime.text = (millisUntilFinished / 1000).toString()

            }
        }

        countDownTimer!!.start()

    }

    override fun onStop() {


        countDownTimer?.onFinish()

        mediaPlayer?.stop()
        mediaPlayer=null
        super.onStop()
    }


    fun setOnClicks(){

        btnAccept.setOnClickListener {

            countDownTimer?.onFinish()

            this.dismiss()
            notifyDriverActionInterface.onAcceptTapped()


        }

        btnReject.setOnClickListener {


         countDownTimer?.onFinish()
         this.dismiss()

         notifyDriverActionInterface.onRejectTapped()


        }

    }





    var countDownTimer :CountDownTimer?=null


    interface NotifyDriverActionInterface{

        fun onAcceptTapped()
        fun onRejectTapped()

    }


}