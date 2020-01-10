package com.rontaxi.view.initial.driver.vehicletype

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.rontaxi.base.BaseFragment
import com.rontaxi.R
import com.rontaxi.view.initial.driver.InitialDriverActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_vehicle_type.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import javax.inject.Inject

class VehicleTypeFragment : BaseFragment() {


    lateinit var vehicleTypeFragmentInterface: VehicleTypeFragmentInterface

    @Inject
    lateinit var vehicleTypeViewModel: VehicleTypeViewModel

    @Inject
    lateinit var adapter: VehicleTypeAdapter


    override fun getLayoutRes() = R.layout.fragment_vehicle_type

    override fun showTitleBar() = false


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setToolbar()

        rvVehicles.post {

            rvVehicles.layoutManager =
                LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            rvVehicles.adapter = adapter


        }


        vehicleTypeViewModel.vehicleTypeLiveData.observe(this, Observer {

            if (it!!.isSuccessful) {
                adapter.data = it.body()!!.data
                adapter.notifyDataSetChanged()

            } else {

            }


        })

        btnNext.setOnClickListener {
            var carType: CarType? = null
            adapter.data.forEach {

                if (it.isSelected) {
                    carType = it
                    vehicleTypeFragmentInterface.onVehicleSelected(carType!!)
                    activity!!.supportFragmentManager.popBackStack()

                    return@forEach
                }

            }

            if (carType == null)
                Toasty.error(context!!, getString(R.string.select_a_vehicle_type)).show()
        }

        vehicleTypeViewModel.getVehicleType()

    }


    private fun setToolbar() {

        val textTitle = TextView(context)

        textTitle.text = getString(R.string.select_vehicle_type)
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


    interface VehicleTypeFragmentInterface {

        fun onVehicleSelected(carType: CarType)

    }


}