package com.rontaxi.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker


fun getMarkerIconFromDrawable(drawable: Drawable, zoomLevel: Float): BitmapDescriptor {


    /**
     * assume scaling factor is 10 against the zoom level for the map
     */

    val canvas = Canvas()
    val bitmap = Bitmap.createBitmap(
        (drawable.getIntrinsicWidth() * (zoomLevel / 10)).toInt(),
        (drawable.getIntrinsicHeight() * (zoomLevel / 10)).toInt(),
        Bitmap.Config.ARGB_8888
    );
    canvas.setBitmap(bitmap);
    drawable.setBounds(
        0,
        0,
        (drawable.getIntrinsicWidth() * (zoomLevel / 10)).toInt(),
        (drawable.getIntrinsicHeight() * (zoomLevel / 10)).toInt()
    );
    drawable.draw(canvas);
    return BitmapDescriptorFactory.fromBitmap(bitmap);
}


fun getBearing(oldPosition: LatLng, newPosition: LatLng): Double {


    val deltaLng = (newPosition.longitude - oldPosition.longitude)
    val y: Double = Math.sin(deltaLng) * Math.cos(newPosition.latitude)
    val x: Double =
        Math.cos(oldPosition.latitude) * Math.sin(newPosition.latitude) - Math.sin(oldPosition.latitude) * Math.cos(
            newPosition.latitude
        ) * Math.cos(deltaLng)
    var bearing: Double = Math.toDegrees((Math.atan2(y, x)))
    bearing = (360 - ((bearing + 360) % 360))

    return bearing

}

fun bearingBetweenLocations(latLng1: LatLng, latLng2: LatLng): Double {

    val PI = 3.14159
    val lat1 = latLng1.latitude * PI / 180
    val long1 = latLng1.longitude * PI / 180
    val lat2 = latLng2.latitude * PI / 180
    val long2 = latLng2.longitude * PI / 180

    val dLon = long2 - long1

    val y = Math.sin(dLon) * Math.cos(lat2)
    val x = Math.cos(lat1) * Math.sin(lat2) - (Math.sin(lat1)
            * Math.cos(lat2) * Math.cos(dLon))

    var brng = Math.atan2(y, x)

    brng = Math.toDegrees(brng)
    brng = (brng + 360) % 360

    return brng
}


interface LatLngInterpolator {

    fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng

    class LinearFixed : LatLngInterpolator {

        override fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {

            val lat = (b.latitude - a.latitude) * fraction + a.latitude
            var lngDelta = b.longitude - a.longitude

            if (Math.abs(lngDelta) > 180) {
                lngDelta -= Math.signum(lngDelta) + 360
            }

            val lng = lngDelta * fraction + a.longitude
            return LatLng(lat, lng)

        }
    }

}


fun animateCab(marker: Marker, endPosition: LatLng) {

    val startPosition = marker.position


    val latLngInterpolator = LatLngInterpolator.LinearFixed()

    val valueAnimator = ValueAnimator.ofInt(0, 1)
    valueAnimator.duration = 3000
    valueAnimator.interpolator = LinearInterpolator()
    valueAnimator.addUpdateListener { animation ->
        try {
            val v = animation!!.animatedFraction
            val newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition)

            marker.rotation = getBearing(marker.position, newPosition).toFloat()

            marker.position = (newPosition)


        } catch (e: Exception) {

        }
    }

    valueAnimator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
        }
    })

    valueAnimator.start()


}



