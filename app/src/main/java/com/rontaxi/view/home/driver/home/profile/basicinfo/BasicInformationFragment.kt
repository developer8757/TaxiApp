package com.rontaxi.view.home.driver.home.profile.basicinfo

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import com.fxn.pix.Pix
import com.rontaxi.R
import com.rontaxi.base.BaseFragment
import com.rontaxi.cache.getToken
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.*
import com.rontaxi.view.erroraction.isResponseValid
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.view.home.driver.home.profile.updatephone.ChangePhoneFragment
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.home.rider.profile.UpdateProfileRequestModel
import com.rontaxi.view.initial.driver.register.DriverRegisterFragment
import com.rontaxi.view.initial.driver.register.DriverRegisterRequest
import com.vanniktech.rxpermission.Permission
import com.vanniktech.rxpermission.RealRxPermission
import com.vanniktech.rxpermission.RxPermission
import es.dmoral.toasty.Toasty
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_basic_information.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import javax.inject.Inject

class BasicInformationFragment : BaseFragment(), View.OnClickListener {


    var imagePath = ""

    @Inject
    lateinit var basicInformationViewModel: BasicInformationViewModel

    override fun getLayoutRes() = R.layout.fragment_basic_information

    override fun showTitleBar() = true

    val compDispPerm: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    val rxPermission: RxPermission by lazy {
        RealRxPermission.getInstance(activity!!.application)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.viewTreeObserver?.addOnGlobalLayoutListener(keyBoardObserver)

        if(isLoaded){

            setData()
            return
        }
        setData()
        clickListners()
        addObsever()
    }

    private fun setToolbar() {
        toolbarTitle.text = resources.getString(R.string.basic_information)
        toolBarGeneric.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolBarGeneric.setNavigationOnClickListener {
            activity!!.onBackPressed()
        }
    }
    override fun onPause() {

        view?.viewTreeObserver?.removeOnGlobalLayoutListener(keyBoardObserver)
        super.onPause()


    }

    private fun clickListners() {
        btnEditUpdate.setOnClickListener(this)
        etPhone.setOnTouchListener { view, motionEvent ->


            if(motionEvent.action==MotionEvent.ACTION_DOWN) {
                (activity as HomeDriverActivity).replaceFragment(ChangePhoneFragment(), true)
            }
            true
        }
    }

    override fun onClick(v: View?) {
        when (v) {



            btnEditUpdate -> {

                if (btnEditUpdate.text.equals(getString(R.string.edit))) {
                    editable(true)
                    btnEditUpdate.text = getString(R.string.update)
                } else {
                    //calling update driver profile api
                    updateProfileApi()
                }
            }

            ivProfile -> {
                openCamera()
            }
        }
    }


    private fun updateProfileApi() {

        if (Validations.isEmpty(context!!, etFirstName)
            && Validations.isEmpty(context!!, etLastName)
            && Validations.isEmpty(context!!, etEmail)
            && Validations.isValidEmail(context!!, etEmail)

        ) {
            val updateProfileRequestModel = UpdateProfileRequestModel()
            val updateUserInfoModel = DriverRegisterRequest()


//            DriverRegisterFragment.registerRequest.


            updateUserInfoModel.firstName = getRequestBody(etFirstName.text.toString().trim())
            updateUserInfoModel.lastName = getRequestBody(etLastName.text.toString().trim())
            updateUserInfoModel.email = getRequestBody(etEmail.text.toString().trim())


//            updateProfileRequestModel.firstName = getRequestBody(etFirstName.text.toString().trim())
//            updateProfileRequestModel.lastName = getRequestBody(etLastName.text.toString().trim())
//            updateProfileRequestModel.email = getRequestBody(etEmail.text.toString().trim())

            if (!imagePath.isNullOrEmpty())

                updateUserInfoModel.driverImage = getMultipartImage("driverImage", imagePath)
//                updateProfileRequestModel.driverImage = getMultipartImage("driverImage", imagePath)


            ProgressDialog.showProgressBar(context!!, getString(R.string.updating), false)
            basicInformationViewModel.updateDriverProfile(updateUserInfoModel)
        }

    }

    private fun editable(boolean: Boolean) {
        etFirstName.isEnabled = boolean
        etLastName.isEnabled = boolean
        etEmail.isEnabled = boolean
        if (boolean) {
            etFirstName.requestFocus()
            etFirstName.setSelection(etFirstName.text!!.length)
            ivProfile.setOnClickListener(this)
        }
        else{
            ivProfile.setOnClickListener(null)

        }

    }

    private fun setData() {
        setToolbar()
        editable(false)
        val user = basicInformationViewModel.getUserProfile()
        etFirstName.setText(user.firstName)
        etLastName.setText(user.lastName)
        etPhone.setText(user.phone!!.number)
        etEmail.setText(user.email)
        countryCodePicker.setCountryForPhoneCode(user.phone!!.code.replace("+", "").toInt())

        ///ivProfile.loadImage(context!!, user.profilePhoto)

        /**
         * will use driverImage to get the driver image  and profile photo to get the rider image
         */
        if (user.driverImage.isNotEmpty()) {
            ivProfile.loadProfileImageFromURL(context!!, user.driverImage)
        }
    }

    private fun addObsever() {
        basicInformationViewModel.updateDriverProfileLiveData.observe(this, Observer {
            ProgressDialog.hideProgressBar()
            if (it!!.isSuccessful) {

                if (isResponseValid(activity!!, it.body()!!)) {

                    imagePath = ""

                    btnEditUpdate.text = getString(R.string.edit)
                    editable(false)

                    basicInformationViewModel.saveUser()
                    basicInformationViewModel.saveToken()

                    Toasty.success(context!!, it.body()!!.message).show()
                }
            } else {
                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }
        })
    }


    private fun openCamera() {

        compDispPerm.add(
            rxPermission.requestEach(
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

                .subscribeWith(object : DisposableObserver<Permission>() {

                    override fun onComplete() {
                    }

                    override fun onNext(t: Permission) {

                        if (t.state() == Permission.State.GRANTED && t.name() == Manifest.permission.CAMERA) {

                            Pix.start(
                                this@BasicInformationFragment,
                                DriverRegisterFragment.CAMERA_REQUEST
                            )
                        }
                    }

                    override fun onError(e: Throwable) {
                    }
                })
        )

    }


    private fun setProfileImage(isImageLocal: Boolean, path: String) {

        if (isImageLocal) {

            imagePath = path

            ivProfile.loadProfileImageFromURI(context!!, imagePath)

            DriverRegisterFragment.registerRequest.driverImage =
                getMultipartImage("driverImage", imagePath)

        } else {

            ivProfile.loadProfileImageFromURL(context!!, path)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            DriverRegisterFragment.CAMERA_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {

                    val data = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)

                    setProfileImage(true, data!![0])
                }
                return

            }
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