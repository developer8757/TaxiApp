package com.rontaxi.view.initial.driver.vehicledetails

import android.app.Activity
import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
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
import com.rontaxi.view.initial.driver.vehicletype.CarType
import com.rontaxi.view.initial.driver.vehicletype.VehicleTypeFragment
import com.vincent.filepicker.Constant
import com.vincent.filepicker.activity.NormalFilePickActivity
import com.vincent.filepicker.filter.entity.NormalFile
import com.whiteelephant.monthpicker.MonthPickerDialog
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_basic_information.*
import kotlinx.android.synthetic.main.fragment_upload.*
import kotlinx.android.synthetic.main.fragment_vehicle_detail.*
import kotlinx.android.synthetic.main.fragment_vehicle_detail.btnSubmit
import kotlinx.android.synthetic.main.tool_bar_generic.*
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class VehicleDetailsFragment : BaseFragment(), VehicleTypeFragment.VehicleTypeFragmentInterface {

    companion object {

        val CAMERA_REQUEST = 101

    }


    @Inject
    lateinit var vehicleDetailsViewModel: VehicleDetailsViewModel

    var carType: CarType? = null

    lateinit var vehicleDetailsInterface: VehicleDetailsInterface

    var calendar = Calendar.getInstance()

    var vehicleImage: String? = null


    var docPath: String? = null


    override fun getLayoutRes() = R.layout.fragment_vehicle_detail

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



        etBrand.setText(user.brand)
        etModel.setText(user.model)
        etYear.setText(user.year)
        etColor.setText(user.color)
        etRegisterationNumber.setText(user.carNumberId)

        user.carObj.let {
            this.carType = it
            etType.setText(carType?.name)
        }


        if (!vehicleImage.isNullOrEmpty()) {
            ivVehicle.loadImageFromUri(context!!, vehicleImage!!)

        } else if (user.driverImage.isNotEmpty()) {
            ivVehicle.loadImage(context!!, user.vehicleImage)
        }



        if (!docPath.isNullOrEmpty()) {
            tvRegDoc.text = ""
            tvRegDoc.isEnabled = false

        } else if (!user.regDoc.isEmpty()) {
            tvRegDoc.text = getFileNameFromUrl(user.regDoc)
            tvRegDoc.isEnabled = true
        }

        tvRegDoc.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        tvRegDoc.setOnClickListener {

            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(user.regDoc))
            startActivity(intent)

        }
    }


    private fun setToolbar() {

        val textTitle = TextView(context)



        textTitle.text =
            if (activity is InitialDriverActivity) getString(R.string.add_vehicle_details) else getString(
                R.string.vehicle_details
            )

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


        toolBarGeneric.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)

        toolBarGeneric.navigationIcon?.setTint(ContextCompat.getColor(activity!!, R.color.white))
        toolBarGeneric.setNavigationOnClickListener {

            if (activity is InitialDriverActivity) (activity as InitialDriverActivity).onBackPressed() else activity!!.supportFragmentManager.popBackStack()

        }

    }

    private fun setObservers() {
        vehicleDetailsViewModel.validateDocsLiveData.observe(this, observer)
    }

    override fun onPause() {
        super.onPause()
        // vehicleDetailsViewModel.validateDocsLiveData.removeObserver(observer)

    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun setInformation() {


    }

    private fun setClicks() {


        ibVehicleImage.setOnClickListener {
            Pix.start(this, DriverRegisterFragment.CAMERA_REQUEST)

        }

        btnSubmit.setOnClickListener {


            if (activity is InitialDriverActivity) {
                if (Validations.isValidFile(
                        context!!,
                        vehicleImage,
                        getString(R.string.upload_vehicle_image)
                    )
                    && Validations.isEmpty(context!!, etRegisterationNumber)
                    && Validations.isEmpty(context!!, etBrand)
                    && Validations.isEmpty(context!!, etModel)
                    && Validations.isEmpty(context!!, etYear)
                    && Validations.isEmpty(context!!, etColor)
                    && Validations.isEmpty(context!!, etType)
                    && Validations.isValidFile(
                        context!!,
                        docPath,
                        getString(R.string.please_upload_rc)
                    )
                ) {

                    DriverRegisterFragment.registerRequest.brand =
                        getRequestBody(etBrand.text.toString().trim())
                    DriverRegisterFragment.registerRequest.model =
                        getRequestBody(etModel.text.toString().trim())
                    DriverRegisterFragment.registerRequest.year =
                        getRequestBody(etYear.text.toString().trim())
                    DriverRegisterFragment.registerRequest.color =
                        getRequestBody(etColor.text.toString().trim())
                    DriverRegisterFragment.registerRequest.carId = getRequestBody(carType!!._id)
                    DriverRegisterFragment.registerRequest.carNumberId =
                        getRequestBody(etRegisterationNumber.text.toString().trim())


                    ProgressDialog.showProgressBar(
                        context!!,
                        getString(R.string.checking_details),
                        false
                    )

                    vehicleDetailsViewModel.validateVehiclePlate(etRegisterationNumber.text.toString().trim(),null)


                    // vehicleDetailsViewModel.validateVehiclePlate(et)

                }
            } else {

                /**
                 * didn't need to check images in update flow
                 */

                if (
                    Validations.isEmpty(context!!, etRegisterationNumber)
                    && Validations.isEmpty(context!!, etBrand)
                    && Validations.isEmpty(context!!, etModel)
                    && Validations.isEmpty(context!!, etYear)
                    && Validations.isEmpty(context!!, etColor)
                    && Validations.isEmpty(context!!, etType)
                ) {

                    DriverRegisterFragment.registerRequest.brand =
                        getRequestBody(etBrand.text.toString().trim())
                    DriverRegisterFragment.registerRequest.model =
                        getRequestBody(etModel.text.toString().trim())
                    DriverRegisterFragment.registerRequest.year =
                        getRequestBody(etYear.text.toString().trim())
                    DriverRegisterFragment.registerRequest.color =
                        getRequestBody(etColor.text.toString().trim())
                    DriverRegisterFragment.registerRequest.carId = getRequestBody(carType!!._id)
                    DriverRegisterFragment.registerRequest.carNumberId =
                        getRequestBody(etRegisterationNumber.text.toString().trim())


                    ProgressDialog.showProgressBar(
                        context!!,
                        getString(R.string.checking_details),
                        false
                    )

                    var user= getUser(context!!)

                    var phone="${user!!.phone!!.code}${user!!.phone!!.number}"

                    vehicleDetailsViewModel.validateVehiclePlate(etRegisterationNumber.text.toString().trim(), phone)


                    // vehicleDetailsViewModel.validateVehiclePlate(et)

                }
            }


        }




        cvRC.setOnClickListener {

            val intent4 = Intent(activity!!, NormalFilePickActivity::class.java)
            intent4.putExtra(Constant.MAX_NUMBER, 1)
            intent4.putExtra(NormalFilePickActivity.SUFFIX, arrayOf("pdf"))
            startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE)
        }

        etYear.setOnClickListener {

            getYearPicker()
            // getDatePicker()
        }

        etType.setOnClickListener {
            var fragment = VehicleTypeFragment()
            fragment.vehicleTypeFragmentInterface = this


            if (activity is InitialDriverActivity) (activity as InitialDriverActivity).replaceFragment(
                fragment,
                true
            )
            else (activity as HomeDriverActivity).replaceFragment(fragment, true)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        setObservers()
        when (requestCode) {

            DriverRegisterFragment.CAMERA_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {

                    val data = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)

                    vehicleImage = data!![0]


                    ivVehicle.loadImageFromUri(context!!, vehicleImage!!)

                    DriverRegisterFragment.registerRequest.vehicleImage =
                        getMultipartImage("vehicleImage", vehicleImage!!)

                }
                return
            }

            Constant.REQUEST_CODE_PICK_FILE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val list =
                        data!!.getParcelableArrayListExtra<NormalFile>(Constant.RESULT_PICK_FILE)
                    if (list.isEmpty()) {
                        return
                    }
                    docPath = list[0].path
                    tvRC.text = list[0].name

                    tvRC.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_check_circle_black_24dp,
                        0
                    );

                    var multipart= createMultipartBody("regDoc", docPath!!)

                    if(multipart!=null)
                        DriverRegisterFragment.registerRequest.permitDoc =multipart
                    else {
                        Toasty.error(context!!, getString(R.string.failed_fetch_doc)).show()
                        return
                    }



                    if (activity is HomeDriverActivity) {

                        setData()
                    }

                }

            }
        }
    }


    fun getYearPicker() {

        var builder =
            MonthPickerDialog.Builder(activity!!, object : MonthPickerDialog.OnDateSetListener {

                override fun onDateSet(selectedMonth: Int, selectedYear: Int) {

                }
            }, 2019, 5)


        builder.showYearOnly()
            .setOnYearChangedListener {

                etYear.setText(it.toString())


            }.build().show()
    }


    fun getDatePicker() {

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



                    etYear.setText("$year-$monthStr-$dayStr")

                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        picker.datePicker.maxDate = Date().time


        picker.show()
    }


    interface VehicleDetailsInterface {

        fun onVehicleDetailsRetrieved()


    }

    override fun onVehicleSelected(carType: CarType) {


        Handler().postDelayed(Runnable {
            this.carType = carType
            etType.setText(carType.name)
        }, 200)


    }

    var observer = Observer<Response<BaseResponseModel>> {


        ProgressDialog.hideProgressBar()


        if (it!!.isSuccessful) {


            vehicleDetailsInterface.onVehicleDetailsRetrieved()
            activity!!.supportFragmentManager.popBackStack()


        } else {

            if (activity is HomeDriverActivity) {
                if (getErrorMessage(it.errorBody()!!).code == 400) {

                    /**
                     * if number plate is already registered , let user continue in case of update flow
                     */

                    vehicleDetailsInterface.onVehicleDetailsRetrieved()
                    activity!!.supportFragmentManager.popBackStack()
                    return@Observer
                }
            }

            showAlert(
                context!!,
                getErrorMessage(it.errorBody()!!).message,
                getString(R.string.ok),
                {})
        }

    }
}