package com.rontaxi.view.home.rider.godview

import android.app.Activity
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.rontaxi.R

class CustomInfoWindowPickUp(var activity: Activity): GoogleMap.InfoWindowAdapter {

    // var windowData: WindowData?=null


    var view: View?=null
    override fun getInfoContents(p0: Marker?): View {

       // if(view==null)
            view=activity.layoutInflater.inflate(R.layout.window_marker_pickup,null)



        return view!!

    }





    fun setData(eta: String, location: String){
/*
        if(eta.trim().isNotEmpty()){
            val etaArray= eta.split(" ")


            view!!.tvEta.text=etaArray[0]
            view!!.tvEtaUnit.text=etaArray[1]
        }else{

            view!!.tvEta.text="--"

        }


        view!!.tvPickUp.text=location*/


    }

    override fun getInfoWindow(p0: Marker?): View? {

        return null
    }


    inner class WindowData{

        var eta=""
        var location=""

    }
}