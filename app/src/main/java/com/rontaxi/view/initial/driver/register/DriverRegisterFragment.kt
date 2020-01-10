package com.rontaxi.view.initial.driver.register

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import com.rontaxi.base.BaseFragment
import com.fxn.pix.Pix
import com.google.gson.Gson
import com.rontaxi.BuildConfig
import com.rontaxi.R
import com.rontaxi.model.registration.Phone
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.*
import com.rontaxi.view.initial.driver.InitialDriverActivity
import com.rontaxi.view.initial.driver.upload.UploadDocumentFragment
import kotlinx.android.synthetic.main.fragment_driver_register.*
import kotlinx.android.synthetic.main.fragment_driver_register.cbTerms
import kotlinx.android.synthetic.main.fragment_driver_register.countryCodePicker
import kotlinx.android.synthetic.main.fragment_driver_register.etEmail
import kotlinx.android.synthetic.main.fragment_driver_register.etFirstName
import kotlinx.android.synthetic.main.fragment_driver_register.etLastName
import kotlinx.android.synthetic.main.fragment_driver_register.etPassword
import kotlinx.android.synthetic.main.fragment_driver_register.etPhone
import kotlinx.android.synthetic.main.tool_bar_generic.*
import javax.inject.Inject
import com.vanniktech.rxpermission.Permission
import com.vanniktech.rxpermission.RealRxPermission
import com.vanniktech.rxpermission.RxPermission
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver


class DriverRegisterFragment: BaseFragment() {

    companion object{

        val CAMERA_REQUEST=101

        val registerRequest=DriverRegisterRequest()

        var phone=Phone()

    }

    val compDispPerm: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    val rxPermission: RxPermission by lazy {
        RealRxPermission.getInstance(activity!!.application)
    }

    @Inject
    lateinit var driverRegistrationViewModel: DriverRegisterViewModel

    var imagePath: String?=null



    override fun getLayoutRes()=R.layout.fragment_driver_register

    override fun showTitleBar()=false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //view?.viewTreeObserver?.addOnGlobalLayoutListener(keyBoardObserver)
        if(isLoaded){
            return
        }
        setToolbar()
        setClicks()
        setObservers()
    }

    override fun onPause() {

      //  view?.viewTreeObserver?.removeOnGlobalLayoutListener(keyBoardObserver)
        super.onPause()


    }


    private fun setToolbar() {

        val textTitle = TextView(context)

        textTitle.text = resources.getString(R.string.register)
        textTitle.setTextColor(resources.getColor(R.color.white))
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.sp_20));


        llMiddle.removeAllViews()
        llMiddle.addView(textTitle)
        llMiddle.gravity = (Gravity.START or Gravity.CENTER_VERTICAL)



        llEnd.removeAllViews()
        // llEnd.addView(ibFilter)
        // llEnd.addView(ibBell)


        toolBarGeneric.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)

        toolBarGeneric.navigationIcon?.setTint(ContextCompat.getColor(activity!!, R.color.white))
        toolBarGeneric.setNavigationOnClickListener {
            (activity as InitialDriverActivity).onBackPressed()
        }



    }

    private fun setObservers(){
        driverRegistrationViewModel.validatePhoneLiveData.observe(this, Observer {

            ProgressDialog.hideProgressBar()
            if(it!!.isSuccessful){
                (activity as InitialDriverActivity).replaceFragment(UploadDocumentFragment(),true)

            }else{
                showAlert(context!!, getErrorMessage(it.errorBody()!!).message,getString(R.string.ok),{})
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

                        Pix.start(this@DriverRegisterFragment, CAMERA_REQUEST)

                    }
                }

                override fun onError(e: Throwable) {
                }
            }))

    }

    private fun setClicks(){


        ivProfile.setOnClickListener {

            openCamera()


        }

        ibChangeImage.setOnClickListener {
            openCamera()

        }


        btnNext.setOnClickListener {

            if(InternetCheck.isConnectedToInternet(context!!)
                && Validations.isValidImage(context!!,imagePath)
                && Validations.isEmpty(context!!,etFirstName)
                && Validations.isEmpty(context!!,etLastName)
                && Validations.isValidEmail(context!!,etEmail)
                && Validations.isValidPhoneNumber(context!!, etPhone)
                && Validations.isEmpty(context!!,etPassword)
                && Validations.isValidPassword(context!!,etPassword)
                && Validations.isAlphaNumeric(context!!,etPassword)
                && Validations.isTermsAccepted(cbTerms,context!!)
            ){

                var request=PhoneRequestModel()
                    request.phone.code="+${countryCodePicker.selectedCountryCode}"
                    request.phone.number=etPhone.text.toString().trim()


                registerRequest.userActiveRole= getRequestBody(BuildConfig.ROLE.toString())
                registerRequest.firstName=getRequestBody(etFirstName.text.toString().trim())
                registerRequest.lastName=getRequestBody(etLastName.text.toString().trim())
                registerRequest.email=getRequestBody(etEmail.text.toString().trim())
                registerRequest.phone= getRequestBody(Gson().toJson(request.phone))

                phone=request.phone





                registerRequest.password=getRequestBody(etPassword.text.toString().trim())



                hideKeyBoard(context!!,view!!)

                ProgressDialog.showProgressBar(context!!, getString(R.string.checking_details),false)

                driverRegistrationViewModel.validatePhone(request)




            }

        }


        btnNext.text="${getString(R.string.next)} (${getString(com.rontaxi.R.string.upload_document)})"

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){

            CAMERA_REQUEST->{
                if (resultCode == Activity.RESULT_OK) {

                    val data = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)


                    setProfileImage(true, data!![0])
                }
                return


            }
        }
    }

    private fun setProfileImage(isImageLocal: Boolean, path: String) {

        if (isImageLocal) {

            imagePath = path


            ivProfile.loadProfileImageFromURI(context!!, imagePath!!)

            registerRequest.driverImage= getMultipartImage("driverImage",imagePath!!)

        } else {

            ivProfile.loadProfileImageFromURL(context!!, path)

        }

    }

    var keyBoardObserver=object: ViewTreeObserver.OnGlobalLayoutListener{
        override fun onGlobalLayout() {

            val heightDiff = view!!.getRootView().getHeight() - view!!.height
            if (heightDiff > dpToPx(context!!, 200f)) { // if more than 200 dp, it's probably a keyboard...
                // ... do something here
                btnNext.visibility= View.GONE


            }else{

                btnNext.visibility= View.VISIBLE

            }
        }
    }
}