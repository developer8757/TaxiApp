package com.rontaxi.view.home.rider.godview

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.rontaxi.R
import com.rontaxi.util.ZOOM_LEVEL_10_KMS
import com.rontaxi.util.isGPSEnabled
import com.rontaxi.util.showAlert
import com.vanniktech.rxpermission.Permission
import io.reactivex.observers.DisposableObserver

fun GodViewFragment.moveToLocationSetting(context: Context) {
    var intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    startActivityForResult(intent, GodViewFragment.LOCATION_REQUEST_CODE);
}

fun GodViewFragment.initMap(){



  childFragmentManager
        .beginTransaction()
        .replace(R.id.map,supportMapFragment).commit()

    supportMapFragment.getMapAsync(this)
}



fun GodViewFragment.getLocationUpdates() {
    godViewModel?.currentLocation?.observe(this, Observer {

        it?.let {
            val currentLocation = LatLng(it.latitude, it.longitude)

          //  Toast.makeText(context!!,"$it",Toast.LENGTH_LONG).show()


         //   var address=getLocationAddress(context!!,it)



            /*address?.apply {
                locationChildFragment.updateLocation(subLocality,locality)

            }*/






            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, ZOOM_LEVEL_10_KMS))

            godViewModel?.getLocationUpdates()
        }

    })


    godViewModel?.address?.observe(this, Observer {

        it?.apply {

            locationChildFragment.updateLocation(subLocality,locality)
        }
    })


    godViewModel?.getLastLocation()


}




fun GodViewFragment.getCurrentLocation(){
    compDispPerm.add(rxPermission.requestEach(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    ).subscribeWith(object: DisposableObserver<Permission>(){

        override fun onComplete() {
        }

        override fun onNext(t: Permission) {

            if (t.state() == Permission.State.GRANTED && t.name() == Manifest.permission.ACCESS_FINE_LOCATION) {


                mMap.uiSettings.isCompassEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = false



                try {
                    mMap.isMyLocationEnabled = (true)

                } catch (ex: SecurityException) {

                }


                // replaceFragment(R.id.mapContainer,locationFragment as Fragment,true)

                //  changeFragment(R.id.mapContainer,locationFragment,true)

                if (!isGPSEnabled(context!!)) {

                    showAlert(context!!,
                        resources.getString(R.string.alert_enable_gps),
                        resources.getString(R.string.ok),
                        onClick = {
                            moveToLocationSetting(context!!)
                        })

                } else {
                    getLocationUpdates()
                }
            } else if (t.state() == (Permission.State.DENIED) && t.name() == Manifest.permission.ACCESS_FINE_LOCATION) {

                activity!!.finish()


            } else if (t.state() == (Permission.State.DENIED_NOT_SHOWN) && t.name() == Manifest.permission.ACCESS_FINE_LOCATION) {
                showAlert(context!!,
                    getString(R.string.enable_location_permission),
                    resources.getString(R.string.ok),
                    onClick = {


                        var intent = Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        var uri = Uri.fromParts("package", activity!!.getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        activity!!.finish()

                    })

            }
        }
        override fun onError(e: Throwable) {
        }
    }))

}



