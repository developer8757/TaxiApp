package com.rontaxi.view.home.rider.profile

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import com.rontaxi.base.BaseFragment
import com.fxn.pix.Pix
import com.google.gson.Gson
import com.rontaxi.R
import com.rontaxi.model.registration.Phone
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.*
import com.rontaxi.view.erroraction.isResponseValid
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.driver.home.profile.updatephone.ChangePhoneFragment
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.initial.driver.register.DriverRegisterFragment
import com.vanniktech.rxpermission.Permission
import com.vanniktech.rxpermission.RealRxPermission
import com.vanniktech.rxpermission.RxPermission
import es.dmoral.toasty.Toasty
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_rider_profile.*
import kotlinx.android.synthetic.main.fragment_rider_profile.countryCodePicker
import kotlinx.android.synthetic.main.fragment_rider_profile.etEmail
import kotlinx.android.synthetic.main.fragment_rider_profile.etFirstName
import kotlinx.android.synthetic.main.fragment_rider_profile.etLastName
import kotlinx.android.synthetic.main.fragment_rider_profile.etPhone
import kotlinx.android.synthetic.main.fragment_rider_profile.ivProfile
import okhttp3.MultipartBody
import javax.inject.Inject

class RiderProfileFragment: BaseFragment() {

    var imagePath=""


    var riderImage: MultipartBody.Part?=null


    @Inject
    lateinit var riderProfileViewModel: RiderProfileViewModel

    override fun getLayoutRes()=R.layout.fragment_rider_profile

    override fun showTitleBar()=false

    val compDispPerm: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    val rxPermission: RxPermission by lazy {
        RealRxPermission.getInstance(activity!!.application)
    }

    private var isEditable= MutableLiveData<Boolean>().apply {

        postValue(false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.viewTreeObserver?.addOnGlobalLayoutListener(keyBoardObserver)
        setInformation()
        setClicks()
        setObserver()
    }

    override fun onPause() {
        view?.viewTreeObserver?.removeOnGlobalLayoutListener(keyBoardObserver)
        super.onPause()
    }

    private fun setInformation(){


        val user=riderProfileViewModel.getUserProfile()

        etFirstName.setText(user.firstName)
        etLastName.setText(user.lastName)
        etEmail.setText(user.email)

        countryCodePicker.setCountryForPhoneCode(user.phone!!.code.replace("+","").toInt())
        etPhone.setText(user.phone!!.number)

        ivProfile.loadProfileImageFromURL(context!!,user.profileImage)

        focusable(false)
        btnEditUpdate.text=getString(R.string.edit)



    }

    private fun focusable(boolean: Boolean){

        etFirstName.setEnable(boolean)
        etLastName.setEnable(boolean)
        etEmail.setEnable(boolean)
        etPhone.setEnable(false)
        countryCodePicker.isClickable=false

        if(boolean){
            etFirstName.requestFocus()
            etFirstName.setSelection(etFirstName.text!!.length)
        }


    }

    private fun setClicks(){

        ibProfileEdit.setOnClickListener {
            openCamera()
        }



        etPhone.setOnClickListener {



                val frag=ChangePhoneFragment()

                (activity as HomeRiderActivity).replaceFragment(frag,true)


        }

        btnEditUpdate.setOnClickListener {


            if(btnEditUpdate.text.equals(getString(R.string.edit))){

                isEditable.postValue(true)

                btnEditUpdate.text=getString(R.string.update)

            }else{

                if(Validations.isEmpty(context!!,etFirstName)
                    && Validations.isEmpty(context!!,etLastName)
                    && Validations.isEmpty(context!!,etEmail)
                    && Validations.isValidEmail(context!!, etEmail)

                ){

                    hideKeyBoard(context!!,view!!)


                    ProgressDialog.showProgressBar(context!!,"",false)



                    val updateProfileRequestModel=UpdateProfileRequestModel()


                    updateProfileRequestModel.profilePhoto=riderImage
                    updateProfileRequestModel.firstName= getRequestBody(etFirstName.text.toString().trim())
                    updateProfileRequestModel.lastName= getRequestBody(etLastName.text.toString().trim())
                    updateProfileRequestModel.email= getRequestBody(etEmail.text.toString().trim())

                    riderProfileViewModel.updateProfile(updateProfileRequestModel)

                }


            }

        }

    }

    private fun setObserver(){
        riderProfileViewModel.updateProfileLiveData.observe(this, Observer {
            ProgressDialog.hideProgressBar()
            if(it!!.isSuccessful) {
                if (isResponseValid(activity!!, it.body()!!)) {

                    riderProfileViewModel.saveUser(it.body()!!.data!!.userObj!!)
                    setInformation()


                    (activity as HomeRiderActivity).setUserInformation()

                    Toasty.success(context!!,it.message()).show()

                }
            }else{
                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }
        })

        isEditable.observe(this, Observer {

            focusable(it!!)

            if(it){
                ibProfileEdit.visibility= View.VISIBLE
            }else{
                ibProfileEdit.visibility= View.GONE
            }

        })
    }


    private fun openCamera(){
        compDispPerm.add(rxPermission.requestEach(
            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

            .subscribeWith(object: DisposableObserver<Permission>(){

                override fun onComplete() {
                }

                override fun onNext(t: Permission) {

                    if (t.state() == Permission.State.GRANTED && t.name() == Manifest.permission.CAMERA) {

                        Pix.start(this@RiderProfileFragment, DriverRegisterFragment.CAMERA_REQUEST)

                    }
                }

                override fun onError(e: Throwable) {
                }
            }))

    }

    private fun setProfileImage(isImageLocal: Boolean, path: String) {

        if (isImageLocal) {

            imagePath = path


            ivProfile.loadProfileImageFromURI(context!!, imagePath)

            riderImage= getMultipartImage("profileImage",imagePath)

        } else {

            ivProfile.loadProfileImageFromURL(context!!, path)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){

            DriverRegisterFragment.CAMERA_REQUEST ->{
                if (resultCode == Activity.RESULT_OK) {

                    val data = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)


                    setProfileImage(true, data!![0])
                }
                return


            }
        }
    }


    fun onBackPressed(){

        if(isEditable.value!!){


            setInformation()
            isEditable.postValue(false)


        }else{
            (activity)!!.supportFragmentManager.popBackStackImmediate()
        }
    }

    var keyBoardObserver=object: ViewTreeObserver.OnGlobalLayoutListener{
        override fun onGlobalLayout() {

            val heightDiff = view!!.getRootView().getHeight() - view!!.height
            if (heightDiff > dpToPx(context!!, 200f)) { // if more than 200 dp, it's probably a keyboard...
                // ... do something here
                btnEditUpdate.visibility= View.GONE


            }else{

                btnEditUpdate.visibility= View.VISIBLE

            }
        }
    }
}