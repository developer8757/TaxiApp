package com.rontaxi.view.home.rider.godview

import android.content.Context
import android.view.View
import com.google.android.gms.maps.model.*
import com.rontaxi.R
import com.rontaxi.util.animateCab
import com.rontaxi.util.getMarkerIconFromDrawable
import com.google.android.gms.maps.CameraUpdateFactory
import kotlinx.android.synthetic.main.window_marker_pickup.view.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import com.google.maps.android.PolyUtil
import com.rontaxi.model.Step
import com.rontaxi.model.manager.BookingManager
import com.rontaxi.util.CarMoveAnimNew
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception


fun GodViewFragment.moveCabs() {

    nearByCabsArray.forEach {


        var existingMarker: Marker? = null

        if (markerArray.containsKey(it.driverId)) {

            existingMarker = markerArray.get(it.driverId)



            CarMoveAnimNew.startcarAnimation(
                existingMarker!!,
                mMap,
                existingMarker!!.position,
                LatLng(it.location!![0].lat!!, it.location!![0].log!!),
                3000,
                null
            )

            // animateCab(existingMarker!!,LatLng(it.location!![0].lat!!,it.location!![0].log!!))


            // existingMarker!!.position= LatLng(it.location!![0].lat!!, it.location!![0].log!!)
            // existingMarker!!.rotation=it.location!![0].bearing!!
        } else {


            val markerOptions = MarkerOptions()
                .position(LatLng(it.location!![0].lat!!, it.location!![0].log!!))
                .flat(true)


            val marker = mMap.addMarker(markerOptions)
            // marker.rotation=it.location!![0].bearing!!
            marker.setIcon(getSedanMarker(context!!))
            markerArray.put(it.driverId, marker)
        }

    }

    try {
        markerArray.forEach {

            val entry = it

            val cab = nearByCabsArray.singleOrNull() {

                it.driverId.equals(entry.key)
            }

            if (cab == null) {
                markerArray.remove(entry.key)
                entry.value.remove()
            }


        }
    } catch (e: Exception) {

    }


    // change eta while cab motion if route is shown and not booking available

    if(BookingManager.currentBooking==null)
        setPickUpMarker()


}


fun GodViewFragment.getSedanMarker(context: Context): BitmapDescriptor {
    if (sedanMarker == null) {
        sedanMarker = getMarkerIconFromDrawable(
            context.getDrawable(R.drawable.sedan_marker_v)!!,
            mMap.cameraPosition.zoom
        )
    }

    return sedanMarker!!
}


fun GodViewFragment.drawPolyLine(list: ArrayList<Step>) {

    polyLine?.remove()

    var latLng: LatLng? = null

    var polyLineOptions = PolylineOptions()
    var builder = LatLngBounds.Builder()

    for (i in 0..(list.size - 1)) {


        latLng = LatLng(list[i].start_location!!.lat, list[i].start_location!!.lng)

        polyLineOptions.add(latLng)
        latLng = LatLng(list[i].end_location!!.lat, list[i].end_location!!.lng)


        var poly = PolyUtil.decode(list[i].polyline!!.points)
        polyLineOptions.addAll(poly)

        builder.include(latLng)

    }

    polyLine = mMap.addPolyline(polyLineOptions)

    val bounds = builder.build()

    val width = map.width//resources.displayMetrics.widthPixels
    val height = map.height//resources.displayMetrics.heightPixels
    val padding = (width * 0.18).toInt()

    val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)

    mMap.animateCamera(cu)


    val markerOptionsPick = MarkerOptions()
        .position(LatLng(list[0].start_location!!.lat, list[0].start_location!!.lng))
        .flat(true)



    pickUpMarker = mMap.addMarker(markerOptionsPick)


    setPickUpMarker()
    val markerOptionsDrop = MarkerOptions()
        .position(
            LatLng(
                list[list.size - 1].end_location!!.lat,
                list[list.size - 1].end_location!!.lng
            )
        )
        .flat(true)

    dropOffMarker = mMap.addMarker(markerOptionsDrop)
    dropOffMarker?.setIcon(
        BitmapDescriptorFactory.fromBitmap(
            getCustomMarkerView(
                null,
                LocationChildFragment.dropOffAddress.value!!.address!!,
                false
            )
        )
    )

    //dropOffMarker!!.setIcon(getMarkerIconFromDrawable(context!!.getDrawable(R.drawable.ic_pin)!!))

}

fun GodViewFragment.setPickUpMarker() {
    pickUpMarker?.setIcon(
        BitmapDescriptorFactory.fromBitmap(
            getCustomMarkerView(
                selectedCab!!.ETA,
                LocationChildFragment.pickUpAddress.value!!.address!!,
                true
            )
        )
    )

}


fun GodViewFragment.getCustomMarkerView(eta: String?, location: String, isPickUp: Boolean): Bitmap {


    eta?.let {
        if (it.trim().isNotEmpty()) {

            val etaArray = eta.split(" ")
            try {
                customInfoWindowPickUpView!!.tvEta.text =
                    "${etaArray[0].toUpperCase()}\n${etaArray[1].toUpperCase()}"
            } catch (e: Exception) {
                customInfoWindowPickUpView!!.tvEta.text = it

            }
        } else {
            customInfoWindowPickUpView!!.tvEta.text = getString(R.string.no_cabs)

        }
    }

    if (isPickUp) {
        customInfoWindowPickUpView!!.clEta.visibility = View.VISIBLE
    } else {
        customInfoWindowPickUpView!!.clEta.visibility = View.INVISIBLE
    }

    customInfoWindowPickUpView!!.tvPickUp.text = location

    customInfoWindowPickUpView!!.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    customInfoWindowPickUpView!!.layout(
        0,
        0,
        customInfoWindowPickUpView!!.measuredWidth,
        customInfoWindowPickUpView!!.height
    )
    customInfoWindowPickUpView!!.buildDrawingCache()

    val returnedBitmap = Bitmap.createBitmap(
        customInfoWindowPickUpView!!.measuredWidth, customInfoWindowPickUpView!!.measuredHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(returnedBitmap)
    canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
    val drawable = customInfoWindowPickUpView!!.getBackground()

    if (drawable != null)
        drawable.draw(canvas)

    customInfoWindowPickUpView!!.draw(canvas)
    return returnedBitmap


}

fun GodViewFragment.getRouteAndEta() {


    selectedCab?.let {
        godViewModel.getFairWithEta(
            selectedCab!!,
            LocationChildFragment.pickUpAddress.value!!,
            LocationChildFragment.dropOffAddress.value!!
        )

    }

}
