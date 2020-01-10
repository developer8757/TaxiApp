package com.rontaxi.view.location

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.rontaxi.base.BaseActivity
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.rontaxi.R
import com.rontaxi.cache.*
import com.rontaxi.model.map.Address
import com.rontaxi.model.map.RecentSearch
import com.rontaxi.model.map.RecentSearchModel
import com.rontaxi.util.hideKeyBoard
import com.rontaxi.view.home.rider.godview.LocationChildFragment
import kotlinx.android.synthetic.main.activity_location.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class LocationActivity: BaseActivity(), LocationSearchAdapter.LocationSearchAdapterInterface,
    RecentSearchAdapter.RecentSearchAdapterInterface, TextWatcher {

    companion object{

        val CURRENTLOCATION=501
        val AUTO_COMPLETE_CODE_HOME=502
        val AUTO_COMPLETE_CODE_OFFICE=503


    }


    @Inject
    lateinit var locationViewModel: LocationViewModel


    @Inject
    lateinit var locationSearchAdapter: LocationSearchAdapter


    @Inject
    lateinit var locationRecentSearchAdapter: RecentSearchAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        setTextWatcher()
        setAdapter()
        setObserver()

        setClicks()

        setInformation()
        getLastLocation()

        setRecentSearchAdapter()


        if(intent.hasExtra(LocationChildFragment.PICK_UP_KEY)) {

            etCurrentLocation.requestFocus()
        }else{
            etCurrentLocation.setText(LocationChildFragment.pickUpAddress.value?.address)
            etDropOffLocation.requestFocus()

        }

        nestedScrollView.setOnTouchListener(object: View.OnTouchListener{

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {


                hideKeyBoard(this@LocationActivity,v!!)
                return false
            }
        })


        etDropOffLocation.setOnFocusChangeListener { v, hasFocus ->

            if(hasFocus){
                cvSavedAddress.visibility=View.VISIBLE
                cvRecentSearch.visibility=View.VISIBLE
                cvResults.visibility=View.GONE
            }
        }


        etCurrentLocation.setOnFocusChangeListener { v, hasFocus ->

            if(hasFocus){
                cvSavedAddress.visibility=View.VISIBLE
                cvRecentSearch.visibility=View.VISIBLE
                cvResults.visibility=View.GONE
            }
        }

    }

    private fun getLastLocation(){
        locationViewModel.getLastLocation ()

    }


    private fun setInformation(){

        getHomeLocation(this)?.apply{

            tvHomeAddress.text=this.address

        }

        getWorkLocation(this)?.apply {

            tvOfficeAddress.text=this.address
        }
    }

    private fun setClicks(){

        tvChangeHome.setOnClickListener {
            try {
                val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

                val intent = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields
                )
                    .build(this);
                startActivityForResult(intent, AUTO_COMPLETE_CODE_HOME);


                /*  val intent =
                   PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                          .build(this);
                  startActivityForResult(intent, AUTO_COMPLETE_CODE);*/
            } catch (e: GooglePlayServicesRepairableException) {

            } catch (e: GooglePlayServicesNotAvailableException) {

            }
        }


        tvChangeOffice.setOnClickListener {
            try {
                val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);


                val intent = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields
                )
                    .build(this);
                startActivityForResult(intent, AUTO_COMPLETE_CODE_OFFICE);


                /*  val intent =
                   PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                          .build(this);
                  startActivityForResult(intent, AUTO_COMPLETE_CODE);*/
            } catch (e: GooglePlayServicesRepairableException) {

            } catch (e: GooglePlayServicesNotAvailableException) {

            }
        }

        tvHomeAddress.setOnClickListener {

            getHomeLocation(this)?.apply{

                if(etCurrentLocation.hasFocus()){

                    LocationChildFragment.autoUpdatePickUp=false
                    LocationChildFragment.pickUpAddress.postValue(this)

                    etCurrentLocation.removeTextChangedListener(this@LocationActivity)

                    etCurrentLocation.setText(this.address)

                    etCurrentLocation.addTextChangedListener(this@LocationActivity)

                    if(LocationChildFragment.dropOffAddress.value==null){

                        etDropOffLocation.requestFocus()
                        return@setOnClickListener
                    }
                    setResult(Activity.RESULT_OK)
                    finish()

                }else{
                    LocationChildFragment.dropOffAddress.postValue(this)
                    setResult(Activity.RESULT_OK)
                    finish()

                }

            }

        }

        tvOfficeAddress.setOnClickListener {
            getWorkLocation(this)?.apply {

                if(etCurrentLocation.hasFocus()){


                    LocationChildFragment.autoUpdatePickUp=false
                    LocationChildFragment.pickUpAddress.postValue(this)

                    etCurrentLocation.removeTextChangedListener(this@LocationActivity)

                    etCurrentLocation.setText(this.address)

                    etCurrentLocation.addTextChangedListener(this@LocationActivity)


                    if(LocationChildFragment.dropOffAddress.value==null){

                        etDropOffLocation.requestFocus()
                        return@setOnClickListener
                    }


                    setResult(Activity.RESULT_OK)
                    finish()

                }else{
                    LocationChildFragment.dropOffAddress.postValue(this)
                    setResult(Activity.RESULT_OK)
                    finish()

                }
            }
        }

    }


    private fun onPickUpChanged(){



    }

    private fun setObserver(){
        locationViewModel.results.observe(this, Observer {

            if(it!!.isNotEmpty()){
                cvSavedAddress.visibility=View.GONE
                cvRecentSearch.visibility=View.GONE
                cvResults.visibility=View.VISIBLE
            }else{
                cvSavedAddress.visibility=View.VISIBLE
                cvRecentSearch.visibility=View.VISIBLE
                cvResults.visibility=View.GONE
            }

            locationSearchAdapter.data.clear()
            locationSearchAdapter.data.addAll(it!!)
            locationSearchAdapter.notifyDataSetChanged()

        })



        locationViewModel.addressfromPrediction.observe(this, Observer {

            it?.let {

                if(etCurrentLocation.hasFocus()){


                    LocationChildFragment.autoUpdatePickUp=false
                    LocationChildFragment.pickUpAddress.postValue(it)

                    etCurrentLocation.removeTextChangedListener(this@LocationActivity)

                    etCurrentLocation.setText(it.address)

                    etCurrentLocation.addTextChangedListener(this@LocationActivity)

                    if(LocationChildFragment.dropOffAddress.value==null){

                       etDropOffLocation.requestFocus()
                        return@Observer
                    }


                    setResult(Activity.RESULT_OK)
                    finish()

                }else{
                    LocationChildFragment.dropOffAddress.postValue(it)
                    setResult(Activity.RESULT_OK)
                    finish()

                }


            }

        })


    }


    private fun setAdapter(){

        rvResults.post {

            locationSearchAdapter.locationSearchAdapterInterface=this

            rvResults.layoutManager=LinearLayoutManager(this,LinearLayout.VERTICAL,false)
            rvResults.adapter=locationSearchAdapter
        }

    }

    private fun setRecentSearchAdapter() {

        rvRecentSearch.post {

            locationRecentSearchAdapter.locationRecentSearchAdapterInterface = this
            rvRecentSearch.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            rvRecentSearch.adapter = locationRecentSearchAdapter

            if (getRecentSearch(this@LocationActivity) != null) {

                var results=ArrayList<RecentSearchModel>()
                results.addAll(getRecentSearch(this@LocationActivity)!!.result)

                Collections.reverse(results)

                locationRecentSearchAdapter.data = results
                locationRecentSearchAdapter.notifyDataSetChanged()

                cvRecentSearch.visibility=View.VISIBLE
            } else {

                cvRecentSearch.visibility=View.GONE
            }
        }
    }

    private fun setTextWatcher(){

        etCurrentLocation.addTextChangedListener(this)


        etDropOffLocation.addTextChangedListener(this)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == AUTO_COMPLETE_CODE_HOME) {
            if (resultCode == RESULT_OK) {
                var place = Autocomplete.getPlaceFromIntent(data!!)


                var address=Address(place.latLng?.latitude,
                    place.latLng?.longitude,
                    "${place.name} ${place.address}",
                    "","","","")


                runOnUiThread {


                    //val currentLocation = LatLng(place.latLng!!.latitude, place.latLng!!.longitude)

                    saveHomeLocation(this,address)
                    setInformation()


                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                var status = Autocomplete.getStatusFromIntent(data!!)



            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        else  if (requestCode == AUTO_COMPLETE_CODE_OFFICE) {
            if (resultCode == RESULT_OK) {
                var place = Autocomplete.getPlaceFromIntent(data!!);
                var address=Address(place.latLng?.latitude,
                    place.latLng?.longitude,
                    "${place.name} ${place.address}",
                    "","","","")

                runOnUiThread {


                    //val currentLocation = LatLng(place.latLng!!.latitude, place.latLng!!.longitude)

                    saveWorkLocation(this,address)
                    setInformation()


                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                var status = Autocomplete.getStatusFromIntent(data!!)



            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    override fun onAddressTapped(prediction: AutocompletePrediction) {


        addToRecentSearches(prediction)



        locationViewModel.getPlaceDetails(prediction.placeId)

    }


    private fun addToRecentSearches(prediction: AutocompletePrediction){


        var recentSearchModel=RecentSearchModel(prediction.getPrimaryText(null).toString(),
            prediction.getSecondaryText(null).toString(),
            prediction.placeId)



      if(  getRecentSearch(this)!=null){
          getRecentSearch(this)!!.result.forEach {

              if(it.placeId.equals(prediction.placeId)){
                  return
              }

          }

          var result=getRecentSearch(this)!!.result

                if(result.size>5){
                    result.removeAt(0)
                }

                result.add(recentSearchModel)

          var recentSearch=RecentSearch()
                recentSearch.result=result



          saveRecentSearch(this,recentSearch)

      }else{

          var recentSearch=RecentSearch()
                recentSearch.result.add(recentSearchModel)

          saveRecentSearch(this,recentSearch)
      }

    }

    override fun onAddressTapped(prediction: RecentSearchModel) {


        locationViewModel.getPlaceDetails(prediction.placeId!!)
    }

    override fun afterTextChanged(s: Editable?) {


        if(s.toString().trim().isNotEmpty())

            locationViewModel.getAddressPredictions(s.toString())
        else {
            cvSavedAddress.visibility = View.VISIBLE

            if(  getRecentSearch(this)!=null) {

                if (getRecentSearch(this@LocationActivity)?.result!!.isNotEmpty()) {

                    cvRecentSearch.visibility = View.VISIBLE
                }
            }

            cvResults.visibility=View.GONE

        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}