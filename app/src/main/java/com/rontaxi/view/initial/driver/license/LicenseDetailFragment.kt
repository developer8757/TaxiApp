package com.rontaxi.view.initial.driver.license

import android.app.Activity
import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.Gravity
import android.widget.DatePicker
import android.widget.TextView
import com.rontaxi.base.BaseFragment
import com.dizzipay.railsbank.base.BaseResponseModel
import com.fxn.pix.Pix
import com.rontaxi.R
import com.rontaxi.cache.getUser
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.*
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.view.initial.driver.InitialDriverActivity
import com.rontaxi.view.initial.driver.register.DriverRegisterFragment
import kotlinx.android.synthetic.main.fragment_license_details.*
import kotlinx.android.synthetic.main.fragment_license_details.btnSubmit
import kotlinx.android.synthetic.main.fragment_vehicle_detail.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class LicenseDetailFragment : BaseFragment() {

    lateinit var licenseDetailsInterface: LicenseDetailsInterface

    @Inject
    lateinit var licenseDetailsViewModel: LicenseDetailsViewModel

    var calendar = Calendar.getInstance()

    var licenseImage: String? = null

    override fun getLayoutRes() = R.layout.fragment_license_details

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

    private fun setObservers() {
        licenseDetailsViewModel.validateDocsLiveData.observe(this, observer)
    }

    private fun setData() {

        val user = getUser(activity!!.applicationContext)!!



        etLicenseNumber.setText(user.licenseNumber)
        etIssuedOn.setText(user.licenseIssuedOn)
        etExpiredOn.setText(user.licenseExpiryDate)

        if (!licenseImage.isNullOrEmpty()) {
            ivLicense.loadImageFromUri(context!!, licenseImage!!)

        } else if (user.licenseDoc.isNotEmpty()) {
            ivLicense.loadImage(context!!, user.licenseDoc)
        }
    }


    private fun setClicks() {

        ibDriverImage.setOnClickListener {

            Pix.start(this, DriverRegisterFragment.CAMERA_REQUEST)

        }

        etIssuedOn.setOnClickListener {

            //  getYearPicker()

            getDatePicker(etIssuedOn, true, false)
        }
        etExpiredOn.setOnClickListener {

            getDatePicker(etExpiredOn, false, true)
        }

        btnSubmit.setOnClickListener {

            hideKeyBoard(context!!, view!!)

            if (activity is InitialDriverActivity) {

                if (Validations.isValidFile(
                        context!!,
                        licenseImage,
                        getString(R.string.upload_license)
                    )
                    && Validations.isEmpty(context!!, etLicenseNumber)
                    && Validations.isEmpty(context!!, etIssuedOn)
                    && Validations.isEmpty(context!!, etExpiredOn)
                ) {
                    ProgressDialog.showProgressBar(
                        context!!,
                        getString(R.string.checking_details),
                        false
                    )
                    DriverRegisterFragment.registerRequest.licenseNumber =
                        getRequestBody(etLicenseNumber.text.toString().trim())
                    DriverRegisterFragment.registerRequest.licenseIssuedOn =
                        getRequestBody(etIssuedOn.text.toString().trim())
                    DriverRegisterFragment.registerRequest.licenseExpiryDate =
                        getRequestBody(etExpiredOn.text.toString().trim())


                    licenseDetailsViewModel.validateLicense(etLicenseNumber.text.toString().trim(),null)
                }

            } else {

                if (Validations.isEmpty(context!!, etLicenseNumber)
                    && Validations.isEmpty(context!!, etIssuedOn)
                    && Validations.isEmpty(context!!, etExpiredOn)
                ) {
                    ProgressDialog.showProgressBar(
                        context!!,
                        getString(R.string.checking_details),
                        false
                    )
                    DriverRegisterFragment.registerRequest.licenseNumber =
                        getRequestBody(etLicenseNumber.text.toString().trim())
                    DriverRegisterFragment.registerRequest.licenseIssuedOn =
                        getRequestBody(etIssuedOn.text.toString().trim())
                    DriverRegisterFragment.registerRequest.licenseExpiryDate =
                        getRequestBody(etExpiredOn.text.toString().trim())


                    var user= getUser(context!!)

                    var phone="${user!!.phone!!.code}${user!!.phone!!.number}"

                    licenseDetailsViewModel.validateLicense(etLicenseNumber.text.toString().trim(),phone)
                }


            }

        }


    }


    fun getDatePicker(et: TextInputEditText, shouldBackDate: Boolean, shouldFutureDate: Boolean) {

        var picker = DatePickerDialog(
            context!!,
            object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    var monthStr = "${month + 1}"
                    if ((month + 1) < 10)
                        monthStr = "0${monthStr}"
                    var dayStr = "$dayOfMonth"
                    if (dayOfMonth < 10)
                        dayStr = "0${dayStr}"



                    et.setText("$year-$monthStr-$dayStr")

                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )


        if (shouldBackDate)
            picker.datePicker.maxDate = Date().time

        if (shouldFutureDate)
            picker.datePicker.minDate = Date().time + 86400000


        picker.show()
    }

    private fun setToolbar() {
        val textTitle = TextView(context)
        textTitle.text = getString(R.string.upload_driving_license)
        textTitle.setTextColor(resources.getColor(R.color.white))
        textTitle.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            getResources().getDimension(R.dimen.sp_20)
        )
        llMiddle.removeAllViews()
        llMiddle.addView(textTitle)
        llMiddle.gravity = (Gravity.START or Gravity.CENTER_VERTICAL)
        llEnd.removeAllViews()
        // llEnd.addView(ibFilter)
        // llEnd.addView(ibBell)
        toolBarGeneric.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolBarGeneric.navigationIcon?.setTint(ContextCompat.getColor(activity!!, R.color.white))
        toolBarGeneric.setNavigationOnClickListener {

            if (activity is InitialDriverActivity) (activity as InitialDriverActivity).onBackPressed() else activity!!.supportFragmentManager.popBackStack()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            DriverRegisterFragment.CAMERA_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {

                    val data = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)

                    licenseImage = data!![0]

                    ivLicense.loadImageFromUri(context!!, licenseImage!!)

                    DriverRegisterFragment.registerRequest.licenseDoc =
                        getMultipartImage("licenseDoc", licenseImage!!)

                }
                return
            }
        }
    }


    interface LicenseDetailsInterface {

        fun onLicenseDetailsRetrived()
    }

    var observer = Observer<Response<BaseResponseModel>> {


        ProgressDialog.hideProgressBar()
        if (it!!.isSuccessful) {
            activity!!.supportFragmentManager.popBackStack()

            licenseDetailsInterface.onLicenseDetailsRetrived()


        } else {
            showAlert(
                context!!,
                getErrorMessage(it.errorBody()!!).message,
                getString(R.string.ok),
                {})
        }


    }

}