package com.rontaxi.view.home.driver.home.map

import android.content.Context
import android.location.Location
import com.google.android.gms.maps.model.*
import com.rontaxi.R
import com.rontaxi.model.manager.BookingManager
import com.rontaxi.util.CarMoveAnimNew
import com.rontaxi.util.getMarkerIconFromDrawable


fun MapViewFragment.getSedanMarker(context: Context): BitmapDescriptor {
    if (sedanMarker == null) {


        sedanMarker = getMarkerIconFromDrawable(
            context.getDrawable(R.drawable.sedan_marker_v),
            mMap.cameraPosition.zoom
        )
    }

    return sedanMarker!!
}


fun MapViewFragment.moveCabOnMap(location: Location) {


    /**
     * ground overlays to resize the image or icon with the zoom in and zoom out
     */

//    mMap.addGroundOverlay(
//        GroundOverlayOptions()
//            .image(BitmapDescriptorFactory.fromResource(R.drawable.sedan_marker))
//            .position(LatLng(30.682323, 76.745184), 100f)
//            .clickable(true)
//    )
//
//
//    return


    val newLatLng = LatLng(location.latitude, location.longitude)
    if (myLocationMarker == null) {


        try {

            var markerOptions = MarkerOptions()
                .position(newLatLng)
                .flat(true)
                .zIndex(1f)


            myLocationMarker = mMap.addMarker(markerOptions)
            myLocationMarker!!.rotation = location.bearing
            myLocationMarker!!.setIcon(getSedanMarker(context!!))
        } catch (e: Exception) {

        }


    } else {


        var sourcelocation = Location("")
        sourcelocation.latitude = myLocationMarker!!.position.latitude
        sourcelocation.longitude = myLocationMarker!!.position.longitude
        val movementDistance = sourcelocation.distanceTo(location)


        /**
         * if vehicle is moved some distance only than animate the cab
         * lets assume if vehicle is moved atleast 5 meters for now
         * only than update and animate the cab position
         */

        if (movementDistance > 5) {

//            myLocationMarker!!.setIcon(getMarkerIconFromDrawable(context!!.getDrawable(R.drawable.sedan_marker)))


            /**
             * animate cab to the new position with turns
             */


            CarMoveAnimNew.startcarAnimation(
                myLocationMarker!!,
                mMap,
                myLocationMarker!!.position,
                LatLng(location.latitude, location.longitude),
                3000,
                null
            )


        }


        //  animateCab(myLocationMarker!!, LatLng(location.latitude, location.longitude))


        /* val contains = mMap.getProjection()
             .getVisibleRegion()
             .latLngBounds
             .contains(LatLng(location.latitude, location.longitude))
         if (!contains) {
             mMap.animateCamera(CameraUpdateFactory.newLatLng(newLatLng))
         }*/


        /*myLocationMarker!!.position=LatLng(location.latitude, location.longitude)
        myLocationMarker!!.rotation=location.bearing*/

    }

}





