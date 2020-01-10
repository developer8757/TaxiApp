package com.rontaxi.view.home.rider.addtip

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.rontaxi.R
import com.rontaxi.base.BaseFragment
import com.rontaxi.model.AddTipModel
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.Validations
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.rider.HomeRiderActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_add_tip.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import javax.inject.Inject

class AddTipFragment : BaseFragment(), View.OnClickListener {

    @Inject
    lateinit var addTipViewModel: AddTipViewModel

    var receiverId=""

    var bookingId=""
    override fun getLayoutRes() = R.layout.fragment_add_tip

    override fun showTitleBar() = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setListeners()
        addObserver()
        setToolBar()

    }

    private fun addObserver() {

        addTipViewModel.addTipLiveData.observe(this, Observer {

            ProgressDialog.hideProgressBar()
            it?.apply {
                if (isSuccessful) {
                    Toasty.success(context!!, "Success").show()

                    (activity)!!.supportFragmentManager.popBackStackImmediate()

                } else {
                    tackleError(activity!!, getErrorMessage(it.errorBody()!!))
                }
            }

        })

    }

    private fun setListeners() {
        btnPayAmount.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        addTipAmount()

    }

    private fun addTipAmount() {

        if(Validations.isEmpty(context!!,etTipAmount))
        {
            ProgressDialog.showProgressBar(context!!, "", false)

            val tipAmount = etTipAmount.text.toString()

            val addTipModel = AddTipModel()

            addTipModel.receiverId = receiverId

            addTipModel.tip = tipAmount.toInt()
            addTipModel.bookingId=bookingId

            addTipViewModel.addTipAmount(addTipModel)

        }

    }

    private fun setToolBar() {
        val textTitle = TextView(context)
        textTitle.text = getString(R.string.add_tip)
        textTitle.setTextColor(resources.getColor(R.color.white))
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_16))
        llMiddle.removeAllViews()
        llMiddle.addView(textTitle)
        llEnd.removeAllViews()
        toolBarGeneric.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)

        toolBarGeneric.setNavigationOnClickListener {
            (activity as HomeRiderActivity).onBackPressed()
        }
    }
}