package com.rontaxi.view.home.rider.godview



import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.rontaxi.R
import com.rontaxi.model.Booking
import com.rontaxi.model.Step
import kotlinx.android.synthetic.main.fragment_home.*


fun GodViewFragment.dropPins(booking: Booking){

    when(booking.bookingStatus){



        1->{

            pickUpMarker?.remove()
            dropOffMarker?.remove()



            val markerOptionsDrop= MarkerOptions()
                .position(LatLng(booking.endingPoint!!.lat,booking.endingPoint!!.log ))
                .flat(false)



            dropOffMarker=mMap.addMarker(markerOptionsDrop)
            dropOffMarker?.setIcon(
                BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                    getString(R.string.drop_off_location_),false)))




            if(booking.routes?.steps.isNullOrEmpty()){


                val markerOptionsPick = MarkerOptions()
                    .position(LatLng(booking.startingPoint!!.lat,booking.startingPoint!!.log ))
                    .flat(false)

                pickUpMarker=mMap.addMarker(markerOptionsPick)
                pickUpMarker?.setIcon(
                    BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                        getString(R.string.pick_up_location_),false)))

            }else{

                drawPolyLine_(booking.routes?.steps!!)

                val list=booking.routes?.steps!!

                val markerOptionsPick = MarkerOptions()
                    .position(LatLng( list[list.size-1].end_location!!.lat,list[list.size-1].end_location!!.lng))
                    .flat(false)

                pickUpMarker=mMap.addMarker(markerOptionsPick)
                pickUpMarker?.setIcon(
                    BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                        getString(R.string.pick_up_location_),false)))

            }

        }

        2->{


            pickUpMarker?.remove()
            dropOffMarker?.remove()



            val markerOptionsDrop= MarkerOptions()
                .position(LatLng(booking.endingPoint!!.lat,booking.endingPoint!!.log ))
                .flat(false)



            dropOffMarker=mMap.addMarker(markerOptionsDrop)
            dropOffMarker?.setIcon(
                BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                    getString(R.string.drop_off_location_),false)))




            if(booking.routes?.steps.isNullOrEmpty()){


                val markerOptionsPick = MarkerOptions()
                    .position(LatLng(booking.startingPoint!!.lat,booking.startingPoint!!.log ))
                    .flat(false)

                pickUpMarker=mMap.addMarker(markerOptionsPick)
                pickUpMarker?.setIcon(
                    BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                        getString(R.string.pick_up_location_),false)))

            }else{

                drawPolyLine_(booking.routes?.steps!!)

                val list=booking.routes?.steps!!

                val markerOptionsPick = MarkerOptions()
                    .position(LatLng( list[list.size-1].end_location!!.lat,list[list.size-1].end_location!!.lng))
                    .flat(false)

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
                .flat(false)




            pickUpMarker=mMap.addMarker(markerOptionsPick)
            pickUpMarker?.setIcon(
                BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                    getString(R.string.pick_up_location_),false)))





            if(booking.routes?.steps.isNullOrEmpty()){


                val markerOptionsDrop= MarkerOptions()
                    .position(LatLng(booking.endingPoint!!.lat,booking.endingPoint!!.log ))
                    .flat(false)



                dropOffMarker=mMap.addMarker(markerOptionsDrop)
                dropOffMarker?.setIcon(
                    BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                        getString(R.string.drop_off_location_),false)))




            }else{

                drawPolyLine_(booking.routes?.steps!!)

                val list=booking.routes?.steps!!



                val markerOptionsDrop= MarkerOptions()
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


                val markerOptionsDrop= MarkerOptions()
                    .position(LatLng(booking.endingPoint!!.lat,booking.endingPoint!!.log ))
                    .flat(true)



                dropOffMarker=mMap.addMarker(markerOptionsDrop)
                dropOffMarker?.setIcon(
                    BitmapDescriptorFactory.fromBitmap(getCustomMarkerView(null,
                        getString(R.string.drop_off_location_),false)))




            }else{

                drawPolyLine_(booking.routes?.steps!!)

                val list=booking.routes?.steps!!



                val markerOptionsDrop= MarkerOptions()
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








fun GodViewFragment.drawPolyLine_(list: ArrayList<Step>) {


    polyLine?.remove()

    var latLng: LatLng? = null

    var polyLineOptions = PolylineOptions()
    var builder = LatLngBounds.Builder()

    for (i in 0..(list.size - 1)) {


        latLng = LatLng(list[i].start_location!!.lat, list[i].start_location!!.lng)

        polyLineOptions.add(latLng)
        latLng = LatLng(list[i].end_location!!.lat, list[i].end_location!!.lng)


        var poly=PolyUtil.decode(list[i].polyline!!.points)
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