package com.rontaxi.view.initial.driver.upload

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.rontaxi.R
import com.rontaxi.base.BaseFragment
import com.rontaxi.cache.getUser
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.*
import com.rontaxi.view.erroraction.isResponseValid
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.view.initial.driver.InitialDriverActivity
import com.rontaxi.view.initial.driver.license.LicenseDetailFragment
import com.rontaxi.view.initial.driver.otp.DriverOtpFragment
import com.rontaxi.view.initial.driver.register.DriverRegisterFragment
import com.rontaxi.view.initial.driver.vehicledetails.VehicleDetailsFragment
import com.rontaxi.view.splash.SplashActivity
import com.vincent.filepicker.Constant
import com.vincent.filepicker.activity.NormalFilePickActivity
import com.vincent.filepicker.filter.entity.NormalFile
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_upload.*
import kotlinx.android.synthetic.main.fragment_upload.btnSubmit
import kotlinx.android.synthetic.main.fragment_vehicle_detail.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import java.io.File
import javax.inject.Inject


class UploadDocumentFragment : BaseFragment(), VehicleDetailsFragment.VehicleDetailsInterface,
    LicenseDetailFragment.LicenseDetailsInterface {

    companion object {

        val INSURANCE_REQUEST = 201
        val PERMIT_REQUEST = 202


    }


    val vehicleDetailsFragment: VehicleDetailsFragment by lazy {
        VehicleDetailsFragment()
    }

    val licenseDetailFragment: LicenseDetailFragment by lazy {

        LicenseDetailFragment()
    }

    @Inject
    lateinit var uploadDocsViewModel: UploadDocsViewModel

//    @Inject
//    lateinit var basicInformationViewModel: BasicInformationViewModel

    var insuranceDoc: String? = null
    var permitDoc: String? = null

    var vehicleDetailsBoolean = ""
    var licenseDetailsBoolean = ""


    override fun getLayoutRes() = R.layout.fragment_upload
    override fun showTitleBar() = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setToolbar()
        setClicks()
        setObservers()

        if (activity is HomeDriverActivity) {
            setData()
        }

    }

    private fun setData() {

        val user = getUser(activity!!.applicationContext)!!

        btnSubmit.text = getString(R.string.update_documents)
        tvVehicleDetails.text = getString(R.string.vehicle_details)
        tvInsurance.text = getString(R.string.vehicle_insurance)
        tvPermit.text = getString(R.string.vehicle_permit)
        tvLicenseDetails.text = getString(R.string.license_details)


        if (!insuranceDoc.isNullOrEmpty()) {
            tvInsuranceDoc.text = getFileNameFromPath(insuranceDoc!!)
            tvInsuranceDoc.isEnabled = false

        } else if (!user.insuranceDoc.isEmpty()) {
            tvInsuranceDoc.text = getFileNameFromUrl(user.insuranceDoc)
            tvInsuranceDoc.isEnabled = true
        }


        if (!permitDoc.isNullOrEmpty()) {
            tvPermitDoc.text = getFileNameFromPath(permitDoc!!)
            tvPermitDoc.isEnabled = false

        } else if (!user.permitDoc.isEmpty()) {
            tvPermitDoc.text = getFileNameFromUrl(user.permitDoc)
            tvPermitDoc.isEnabled = true
        }


        tvInsuranceDoc.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        tvPermitDoc.paintFlags = Paint.UNDERLINE_TEXT_FLAG


        tvPermitDoc.setOnClickListener {

            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(user.permitDoc))
            startActivity(intent)

        }

        tvInsuranceDoc.setOnClickListener {

            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(user.insuranceDoc))
            startActivity(intent)

        }

    }


    private fun setToolbar() {

        val textTitle = TextView(context)

        textTitle.text = getString(R.string.upload_document)
        textTitle.setTextColor(resources.getColor(R.color.white))
        textTitle.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            getResources().getDimension(R.dimen.sp_20)
        );


        llMiddle.removeAllViews()
        llMiddle.addView(textTitle)
        llMiddle.gravity = (Gravity.START or Gravity.CENTER_VERTICAL)



        llEnd.removeAllViews()
        // llEnd.addView(ibFilter)
        // llEnd.addView(ibBell)

        toolBarGeneric?.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolBarGeneric?.navigationIcon?.setTint(ContextCompat.getColor(activity!!, R.color.white))
        toolBarGeneric?.setNavigationOnClickListener {


            if (activity is InitialDriverActivity) (activity as InitialDriverActivity).onBackPressed() else activity!!.supportFragmentManager.popBackStack()

        }

    }

    private fun setObservers() {

        uploadDocsViewModel.registerLiveData.observe(this, Observer {

            ProgressDialog.hideProgressBar()

            if (it!!.isSuccessful) {
                Toasty.success(context!!, it!!.body()!!.message).show()

                uploadDocsViewModel.saveToken()


                var frag = DriverOtpFragment()
                frag.phone = DriverRegisterFragment.phone
                frag.navigation = DriverOtpFragment.NAVIGATION.REGISTERATION

                (activity as InitialDriverActivity).replaceFragment(frag, true)
            } else {

                showAlert(
                    context!!,
                    getErrorMessage(it.errorBody()!!).message,
                    getString(R.string.ok),
                    {})

            }
        })


        uploadDocsViewModel.updateDriverProfileLiveData.observe(this, Observer {
            ProgressDialog.hideProgressBar()
            if (it!!.isSuccessful) {

                if (isResponseValid(activity!!, it.body()!!)) {

                    Toasty.success(context!!, it.body()!!.message).show()

                    // Logout the user on changning his information


                    // uploadDocsViewModel.logout()


                    /**
                     * user not able to hit logout API after change in the vehicle information
                     * so we are clearing all offline data and logs out user from our side
                     */

                    uploadDocsViewModel.clearData()
                    uploadDocsViewModel.disconnectSockets()

                    showAlert(
                        context!!,
                        "Information updated successfully, We are going to log out your Profile as it needs to be re-approved from admin",
                        getString(R.string.ok)
                    ) {

                        val intent = Intent(activity!!, SplashActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)

                    }


                }
            } else {
                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }
        })


        uploadDocsViewModel.logoutLiveData.observe(this, Observer {
            it!!.apply {
                ProgressDialog.hideProgressBar()
                if (it.isSuccessful) {
                    Toasty.success(activity!!, it.body()!!.message).show()
                    uploadDocsViewModel.clearData()
                    uploadDocsViewModel.disconnectSockets()


                    val intent = Intent(activity!!, SplashActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else
                    tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }
        })

    }


    private fun setClicks() {
        cvVehicleDetails.setOnClickListener {

            vehicleDetailsFragment.vehicleDetailsInterface = this



            if (activity is InitialDriverActivity) (activity as InitialDriverActivity).replaceFragment(
                vehicleDetailsFragment,
                true
            ) else (activity as HomeDriverActivity).replaceFragment(vehicleDetailsFragment, true)


        }
        cvLicenseDetails.setOnClickListener {

            licenseDetailFragment.licenseDetailsInterface = this


            if (activity is InitialDriverActivity) (activity as InitialDriverActivity).replaceFragment(
                licenseDetailFragment,
                true
            ) else (activity as HomeDriverActivity).replaceFragment(licenseDetailFragment, true)


        }


        cvInsurance.setOnClickListener {
            val intent4 = Intent(activity!!, NormalFilePickActivity::class.java)
            intent4.putExtra(Constant.MAX_NUMBER, 1)
            intent4.putExtra(NormalFilePickActivity.SUFFIX, arrayOf("pdf"))
            startActivityForResult(intent4, INSURANCE_REQUEST)

        }

        cvPermit.setOnClickListener {
            val intent4 = Intent(activity!!, NormalFilePickActivity::class.java)
            intent4.putExtra(Constant.MAX_NUMBER, 1)
            intent4.putExtra(NormalFilePickActivity.SUFFIX, arrayOf("pdf"))
            startActivityForResult(intent4, PERMIT_REQUEST)
        }

        btnSubmit.setOnClickListener {


            if (activity is InitialDriverActivity) {


                /**
                 * registration flow
                 */

                if (InternetCheck.isConnectedToInternet(context!!)
                    && Validations.isValidFile(
                        context!!,
                        vehicleDetailsBoolean,
                        getString(R.string.upload_vehicle_details)
                    )
                    && Validations.isValidFile(
                        context!!,
                        licenseDetailsBoolean,
                        getString(R.string.upload_license_details)
                    )
                    && Validations.isValidFile(
                        context!!,
                        insuranceDoc,
                        getString(R.string.upload_insurance)
                    )
                    && Validations.isValidFile(
                        context!!,
                        permitDoc,
                        getString(R.string.upload_permit)
                    )
                ) {

                    ProgressDialog.showProgressBar(
                        context!!,
                        getString(R.string.register_driver),
                        true
                    )

                    uploadDocsViewModel.register(DriverRegisterFragment.registerRequest)

                }


            } else {
                /**
                 * update flow
                 */

                if (InternetCheck.isConnectedToInternet(context!!) && (!vehicleDetailsBoolean.isNullOrEmpty()
                            || !licenseDetailsBoolean.isNullOrEmpty()
                            || !insuranceDoc.isNullOrEmpty()
                            || !permitDoc.isNullOrEmpty())
                ) {
                    ProgressDialog.showProgressBar(context!!, getString(R.string.updating), false)
                    uploadDocsViewModel.updateDriverProfile(DriverRegisterFragment.registerRequest)
                } else {
                    Toasty.info(context!!, "Nothing to update", Toast.LENGTH_LONG, true).show()
                }

            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            INSURANCE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    val list =
                        data!!.getParcelableArrayListExtra<NormalFile>(Constant.RESULT_PICK_FILE)
                    if (list.isEmpty()) {
                        return
                    }



                    insuranceDoc = list[0].path

                    var multipart= createMultipartBody("insuranceDoc", list[0].path)

                    if(multipart!=null)
                        DriverRegisterFragment.registerRequest.insuranceDoc =multipart
                    else {
                        Toasty.error(context!!, getString(R.string.failed_fetch_doc)).show()

                        return
                    }

                    tvInsurance.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_check_circle_black_24dp,
                        0
                    );


                    /**
                     * if this is update flow update insurance file name  again after selection
                     */
                    if (activity is HomeDriverActivity) {
                        setData()
                    }

                }
            }

            PERMIT_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    val list =
                        data!!.getParcelableArrayListExtra<NormalFile>(Constant.RESULT_PICK_FILE)
                    if (list.isEmpty()) {
                        return
                    }
                    permitDoc = list[0].path


                    var multipart= createMultipartBody("permitDoc", list[0].path)

                    if(multipart!=null)
                        DriverRegisterFragment.registerRequest.permitDoc =multipart
                    else {
                        Toasty.error(context!!, getString(R.string.failed_fetch_doc)).show()
                        return

                    }



                    tvPermit.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_check_circle_black_24dp,
                        0
                    );

                    /**
                     * if this is update flow update permit file name  again after selection
                     */

                    if (activity is HomeDriverActivity) {
                        setData()
                    }

                }
            }

        }
    }


    override fun onVehicleDetailsRetrieved() {

        Handler().postDelayed(Runnable {

            vehicleDetailsBoolean = "available"

            tvVehicleDetails.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_check_circle_black_24dp,
                0
            );

        }, 200)


    }

    override fun onLicenseDetailsRetrived() {

        Handler().postDelayed(Runnable {

            licenseDetailsBoolean = "available"

            tvLicenseDetails.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_check_circle_black_24dp,
                0
            );

        }, 200)


    }
}