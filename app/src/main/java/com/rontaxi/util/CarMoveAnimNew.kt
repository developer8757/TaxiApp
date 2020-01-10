package com.rontaxi.util

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.rontaxi.BuildConfig
import com.rontaxi.R
import com.rontaxi.model.manager.BookingManager


class CarMoveAnimNew {

    interface LatLngInterpolatorNew {
        fun interpolate(var1: Float, var2: LatLng, var3: LatLng): LatLng

        class LinearFixed : CarMoveAnimNew.LatLngInterpolatorNew {

            override fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {
                val lat = (b.latitude - a.latitude) * fraction.toDouble() + a.latitude
                var lngDelta = b.longitude - a.longitude
                if (Math.abs(lngDelta) > 180.0) {
                    lngDelta -= Math.signum(lngDelta) * 360.0
                }

                val lng = lngDelta * fraction.toDouble() + a.longitude
                return LatLng(lat, lng)
            }
        }
    }

    companion object {

        fun startcarAnimation(
            carMarker: Marker,
            googleMap: GoogleMap,
            startPosition: LatLng,
            endPosition: LatLng,
            duration: Int,
            callback: GoogleMap.CancelableCallback?
        ) {
            var duration = duration
            val valueAnimator = ValueAnimator.ofFloat(*floatArrayOf(0.0f, 1.0f))
            if (duration == 0 || duration < 3000) {
                duration = 3000
            }

            valueAnimator.duration = duration.toLong()
            val latLngInterpolator = CarMoveAnimNew.LatLngInterpolatorNew.LinearFixed()
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener { valueAnimator ->
                val v = valueAnimator.animatedFraction
                var var10000 =
                    v.toDouble() * endPosition.longitude + (1.0f - v).toDouble() * startPosition.longitude
                var10000 =
                    v.toDouble() * endPosition.latitude + (1.0f - v).toDouble() * startPosition.latitude
                val newPos = latLngInterpolator.interpolate(v, startPosition, endPosition)
                carMarker.position = newPos
                carMarker.setAnchor(0.5f, 0.5f)
                carMarker.rotation =
                    CarMoveAnimNew.bearingBetweenLocations(startPosition, endPosition).toFloat()

                if (BookingManager.currentBooking == null && BuildConfig.ROLE == 1) {

                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            newPos,
                            googleMap.maxZoomLevel - 3
                        )
                    )


                }


            }
            valueAnimator.start()
        }

        private fun bearingBetweenLocations(latLng1: LatLng, latLng2: LatLng): Double {
            val PI = 3.14159
            val lat1 = latLng1.latitude * PI / 180.0
            val long1 = latLng1.longitude * PI / 180.0
            val lat2 = latLng2.latitude * PI / 180.0
            val long2 = latLng2.longitude * PI / 180.0
            val dLon = long2 - long1
            val y = Math.sin(dLon) * Math.cos(lat2)
            val x =
                Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon)
            var brng = Math.atan2(y, x)
            brng = Math.toDegrees(brng)
            brng = (brng + 360.0) % 360.0
            return brng
        }
    }
}