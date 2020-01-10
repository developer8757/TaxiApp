package com.rontaxi.view.home.driver.home.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.rontaxi.R
import com.rontaxi.model.Booking
import com.rontaxi.model.Step
import kotlinx.android.synthetic.main.fragment_map_view.*
import kotlinx.android.synthetic.main.window_marker_pickup.view.*
import java.lang.Exception


fun MapViewFragment.dropPins(booking: Booking){

    when(booking.bookingStatus){



        1->{

            pickUpMarker?.remove()
            dropOffMarker?.remove()



            val markerOptionsDrop=MarkerOptions()
                .position(LatLng(booking.endingPoint!!.lat,booking.endingPoint!!.log ))
                .flat(true)



            dropOffMarker=mMap.addMarker(markerOptionsDrop)
            dropOffMarker?.setIcon(
                BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                    getString(R.string.drop_off_location_),false)))




            if(booking.routes?.steps.isNullOrEmpty()){


                val markerOptionsPick = MarkerOptions()
                    .position(LatLng(booking.startingPoint!!.lat,booking.startingPoint!!.log ))
                    .flat(true)

                pickUpMarker=mMap.addMarker(markerOptionsPick)
                pickUpMarker?.setIcon(
                    BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                        getString(R.string.pick_up_location_),false)))

            }else{

                drawPolyLine(booking.routes?.steps!!)

               val list=booking.routes?.steps!!

                val markerOptionsPick = MarkerOptions()
                    .position(LatLng( list[list.size-1].end_location!!.lat,list[list.size-1].end_location!!.lng))
                    .flat(true)

                pickUpMarker=mMap.addMarker(markerOptionsPick)
                pickUpMarker?.setIcon(
                    BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                        getString(R.string.pick_up_location_),false)))

            }

        }

        2->{


            pickUpMarker?.remove()
            dropOffMarker?.remove()



            val markerOptionsDrop=MarkerOptions()
                .position(LatLng(booking.endingPoint!!.lat,booking.endingPoint!!.log ))
                .flat(true)



            dropOffMarker=mMap.addMarker(markerOptionsDrop)
            dropOffMarker?.setIcon(
                BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                    getString(R.string.drop_off_location_),false)))




            if(booking.routes?.steps.isNullOrEmpty()){


                val markerOptionsPick = MarkerOptions()
                    .position(LatLng(booking.startingPoint!!.lat,booking.startingPoint!!.log ))
                    .flat(true)

                pickUpMarker=mMap.addMarker(markerOptionsPick)
                pickUpMarker?.setIcon(
                    BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                        getString(R.string.pick_up_location_),false)))

            }else{

                drawPolyLine(booking.routes?.steps!!)

                val list=booking.routes?.steps!!

                val markerOptionsPick = MarkerOptions()
                    .position(LatLng( list[list.size-1].end_location!!.lat,list[list.size-1].end_location!!.lng))
                    .flat(true)

                pickUpMarker=mMap.addMarker(markerOptionsPick)
                pickUpMarker?.setIcon(
                    BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                        getString(R.string.pick_up_location_),false)))

            }



        }

        4->{

            pickUpMarker?.remove()
            dropOffMarker?.remove()


            val markerOptionsPick = MarkerOptions()
                .position(LatLng(booking.startingPoint!!.lat,booking.startingPoint!!.log ))
                .flat(true)




            pickUpMarker=mMap.addMarker(markerOptionsPick)
            pickUpMarker?.setIcon(
                BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                    getString(R.string.pick_up_location_),false)))





            if(booking.routes?.steps.isNullOrEmpty()){


                val markerOptionsDrop=MarkerOptions()
                    .position(LatLng(booking.endingPoint!!.lat,booking.endingPoint!!.log ))
                    .flat(true)



                dropOffMarker=mMap.addMarker(markerOptionsDrop)
                dropOffMarker?.setIcon(
                    BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                        getString(R.string.drop_off_location_),false)))




            }else{

                drawPolyLine(booking.routes?.steps!!)

                val list=booking.routes?.steps!!



                val markerOptionsDrop=MarkerOptions()
                    .position(LatLng( list[list.size-1].end_location!!.lat,list[list.size-1].end_location!!.lng))
                    .flat(true)



                dropOffMarker=mMap.addMarker(markerOptionsDrop)
                dropOffMarker?.setIcon(
                    BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                        getString(R.string.drop_off_location_),false)))

            }






        }


        5->{

            pickUpMarker?.remove()
            dropOffMarker?.remove()


            val markerOptionsPick = MarkerOptions()
                .position(LatLng(booking.startingPoint!!.lat,booking.startingPoint!!.log ))
                .flat(true)




            pickUpMarker=mMap.addMarker(markerOptionsPick)
            pickUpMarker?.setIcon(
                BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                    getString(R.string.pick_up_location_),false)))





            if(booking.routes?.steps.isNullOrEmpty()){


                val markerOptionsDrop=MarkerOptions()
                    .position(LatLng(booking.endingPoint!!.lat,booking.endingPoint!!.log ))
                    .flat(true)



                dropOffMarker=mMap.addMarker(markerOptionsDrop)
                dropOffMarker?.setIcon(
                    BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                        getString(R.string.drop_off_location_),false)))




            }else{

                drawPolyLine(booking.routes?.steps!!)

                val list=booking.routes?.steps!!



                val markerOptionsDrop=MarkerOptions()
                    .position(LatLng( list[list.size-1].end_location!!.lat,list[list.size-1].end_location!!.lng))
                    .flat(true)



                dropOffMarker=mMap.addMarker(markerOptionsDrop)
                dropOffMarker?.setIcon(
                    BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                        getString(R.string.drop_off_location_),false)))

            }




        }

        else->{


                pickUpMarker?.remove()
                dropOffMarker?.remove()
                polyLine?.remove()

        }

    }



}





fun MapViewFragment.getCustomMarkerView(eta: String?, location: String, isPickUp: Boolean): Bitmap {


    eta?.let {
        if(it.trim().isNotEmpty()) {

            val etaArray=eta.split(" ")
            try {
                customInfoWindowPickUpView!!.tvEta.text = "${etaArray[0].toUpperCase()}\n${etaArray[1].toUpperCase()}"
            }catch (e: Exception){
                customInfoWindowPickUpView!!.tvEta.text = it

            }
        }else{
            customInfoWindowPickUpView!!.tvEta.text =getString(R.string.no_cabs)

        }
    }

    if(isPickUp){
        customInfoWindowPickUpView!!.clEta.visibility= View.VISIBLE
    }else{
        customInfoWindowPickUpView!!.clEta.visibility= View.INVISIBLE
    }

    customInfoWindowPickUpView!!.tvPickUp.text=location

    customInfoWindowPickUpView!!.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    customInfoWindowPickUpView!!.layout(0,0,customInfoWindowPickUpView!!.measuredWidth,customInfoWindowPickUpView!!.height)
    customInfoWindowPickUpView!!.buildDrawingCache()

    val returnedBitmap = Bitmap.createBitmap(
        customInfoWindowPickUpView!!.measuredWidth, customInfoWindowPickUpView!!.measuredHeight,
        Bitmap.Config.ARGB_8888)

    val canvas = Canvas(returnedBitmap)
    canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
    val drawable = customInfoWindowPickUpView!!.getBackground()

    if(drawable!=null)
        drawable.draw(canvas)

    customInfoWindowPickUpView!!.draw(canvas)
    return returnedBitmap


}


fun MapViewFragment.drawPolyLine(list: ArrayList<Step>) {


    polyLine?.remove()

    var latLng: LatLng? = null

    var polyLineOptions = PolylineOptions()
    var builder = LatLngBounds.Builder()

    for (i in 0..(list.size - 1)) {


        latLng = LatLng(list[i].start_location!!.lat, list[i].start_location!!.lng)

        polyLineOptions.add(latLng)
        latLng = LatLng(list[i].end_location!!.lat, list[i].end_location!!.lng)


        var poly= PolyUtil.decode(list[i].polyline!!.points)
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
}