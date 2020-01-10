package com.rontaxi.view.home.driver.home

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.rontaxi.R
import com.rontaxi.cache.currentSocketState
import com.rontaxi.socket.SocketManager
import com.rontaxi.util.isGPSEnabled
import com.rontaxi.util.showAlert
import com.vanniktech.rxpermission.Permission
import io.reactivex.observers.DisposableObserver

fun HomeDriverFragment.moveToLocationSetting(context: Context) {
    var intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    startActivityForResult(intent, HomeDriverFragment.LOCATION_REQUEST_CODE);
}




fun HomeDriverFragment.getCurrentLocation(){

    compDispPerm.add(rxPermission.requestEach(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    ).subscribeWith(object: DisposableObserver<Permission>(){

        override fun onComplete() {
        }

        override fun onNext(t: Permission) {

            if (t.state() == Permission.State.GRANTED && t.name() == Manifest.permission.ACCESS_FINE_LOCATION) {



                mapViewFragment.onPermissionGranted()


                if (!isGPSEnabled(context!!)) {

                    showAlert(context!!,
                        resources.getString(R.string.alert_enable_gps),
                        resources.getString(R.string.ok),
                        onClick = {
                            moveToLocationSetting(context!!)
                        })

                } /*else {
                    homeDriverViewModel.getLocationUpdates()
                }*/
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


fun HomeDriverFragment.getLocationsUpdate(){
    compDispPerm.add(rxPermission.requestEach(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    ).subscribeWith(object: DisposableObserver<Permission>(){

        override fun onComplete() {
        }

        override fun onNext(t: Permission) {
            if (t.state() == Permission.State.GRANTED && t.name() == Manifest.permission.ACCESS_FINE_LOCATION) {


                homeDriverViewModel.getLocationUpdates()
            }

        }

        override fun onError(e: Throwable) {
        }
    } ))

}


fun HomeDriverFragment.setLocationUpdateObserver(){
    currentSocketState.observe(this, Observer {

        when(it){

            SocketManager.SOCKET_STATE.AUTHENTICATED->{
                homeDriverViewModel.getAvailabilityUpdate()

                homeDriverViewModel.getBookingStatus()
            }
        }

    })

}

