package com.rontaxi.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.support.annotation.NonNull
import java.io.IOException
import java.util.*


fun getLocationAddress(@NonNull context: Context, @NonNull location: Location): Address? {
    val geocoder: Geocoder
    val addresses: List<Address>?
    geocoder = Geocoder(context, Locale.getDefault())


    try {
        addresses = geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        addresses?.let {

            if (addresses.size > 0)
                return addresses[0]
            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        }
    } catch (ex: IOException) {
        ex.printStackTrace()
    }
    return null
}


fun isGPSEnabled(context: Context): Boolean {
    val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    var gps_enabled = false


    try {
        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    } catch (ex: Exception) {
    }


    return gps_enabled
}


fun getLocationAddress(@NonNull context: Context, @NonNull locationName: String): Address? {
    val geocoder: Geocoder
    val addresses: List<Address>?
    geocoder = Geocoder(context, Locale.getDefault())


    try {
        addresses = geocoder.getFromLocationName(
            locationName,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        addresses?.let {

            if (addresses.size > 0)
                return addresses[0]
            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        }
    } catch (ex: IOException) {
        ex.printStackTrace()
    }
    return null
}











