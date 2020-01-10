package com.rontaxi.view.home.rider.payment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.rontaxi.R
import com.rontaxi.base.BaseFragment
import com.rontaxi.cache.getUser
import com.rontaxi.cache.saveUser
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.showAlert
import com.rontaxi.util.showAlertWithCancel
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.rider.HomeRiderActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import javax.inject.Inject


class PaymentFragment : BaseFragment(), View.OnClickListener, PaymentListAdapter.PaymentListInterface {

    @Inject
    lateinit var paymentViewModel: PaymentViewModel

    override fun getLayoutRes() = R.layout.fragment_payment

    var cardDataList = ArrayList<Payment>()

    override fun showTitleBar() = false

    var selectedPosition = 0

    val paymentListAdapter: PaymentListAdapter by lazy {
        PaymentListAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setToolBar()
        setRecyclerView()

        getCard()
        showHideLayout()

        if(!isLoaded){
            addObserver()
        }
    }

    private fun showHideLayout() {
        if (cardDataList != null) {
            clSaveCards.visibility = View.VISIBLE
        } else {
            clSaveCards.visibility = View.GONE
        }
    }

    private fun getCard() {
        ProgressDialog.showProgressBar(context!!, "", false)
        paymentViewModel.getCard()
    }

    private fun setRecyclerView() {
        val mLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        paymentListAdapter.paymentListInterface = this
        paymentListAdapter.cardList = cardDataList
        rvPaymentList?.layoutManager = mLayoutManager
        rvPaymentList?.adapter = paymentListAdapter
    }

    private fun addObserver() {

        paymentViewModel.paymentListLiveData.observe(this, Observer {

            ProgressDialog.hideProgressBar()
            it?.apply {
                if (isSuccessful) {
                    this.body()?.data?.let { cardList ->

                        var user= getUser(context!!)

                            user!!.payment?.clear()
                            user!!.payment=ArrayList<Payment>().apply {

                                this.addAll(cardList)
                            }

                        saveUser(context!!,user)


                        cardDataList.clear()
                        cardDataList.addAll(cardList)
                        paymentListAdapter.notifyDataSetChanged()
                    }

                } else {
                    tackleError(activity!!, getErrorMessage(it.errorBody()!!))
                }
            }
        })

        paymentViewModel.defaultPaymentLiveData.observe(this, Observer {
            it?.apply {
                ProgressDialog.hideProgressBar()

                if (isSuccessful) {


                    getCard()


                    /*var user= getUser(context!!)
                    var selectedPayment=cardDataList[selectedPosition]

                    user!!.payment!!.forEach {

                        it.isSelected=false

                    }

                    var userPayment=user!!.payment!!.single{

                        it.id.equals(selectedPayment.id)
                    }

                    user.payment!!.remove(userPayment)

                    userPayment.isSelected=true

                    user.payment!!.add(userPayment)


                    saveUser(context!!, user)





                    for (i in 0..(cardDataList.size - 1)) {
                        cardDataList[i].isSelected = false

                        if (i == selectedPosition) {
                            cardDataList[i].isSelected = true
                            Log.d("position", "" + i)
                        }
                        paymentListAdapter.notifyDataSetChanged()
                    }*/
                } else {
                    tackleError(activity!!, getErrorMessage(it.errorBody()!!))
                }
            }

        })

        paymentViewModel.deleteCardLiveData.observe(this, Observer {

            it?.apply {
                ProgressDialog.hideProgressBar()
                if (isSuccessful) {
                    Toasty.success(context!!, it.message()).show()
                } else {
                    tackleError(activity!!, getErrorMessage(it.errorBody()!!))

                }
            }
        })
    }

    override fun removeCard(cardData: Payment, position: Int) {
        if (cardData.isSelected) {
            showAlert(context!!, getString(R.string.default_source_not_deleted), getString(R.string.ok)) { }
        } else {
            cardData.id?.let {
                paymentViewModel.deleteSelectedCard(it)
            }
            cardDataList.removeAt(position)
            paymentListAdapter.notifyDataSetChanged()
        }
    }


    override fun onItemClick(cardData: Payment, position: Int) {

        showAlertWithCancel(context!!,
            getString(R.string.r_u_sure_set_default_payment),
            getString(R.string.ok), getString(R.string.cancel), {
                ProgressDialog.showProgressBar(context!!, "", false)

                selectedPosition = position
                paymentViewModel.getPaymentMethod(cardData.id!!)

            }, {})

    }

    override fun onClick(p0: View?) {
        addFragment()

    }

    private fun addFragment() {
        val addCardFragment = AddCardFragment()

        (activity as HomeRiderActivity).replaceFragment(addCardFragment, true)

    }

    private fun setToolBar() {
        val textTitle = TextView(context)
        textTitle.text = getString(R.string.payment_method)
        textTitle.setTextColor(resources.getColor(R.color.white))
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_16))
        llMiddle.removeAllViews()
        llMiddle.addView(textTitle)
        llEnd.removeAllViews()
        toolBarGeneric.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolBarGeneric.setNavigationOnClickListener {
            (activity as HomeRiderActivity).onBackPressed()

        }
        val ibPlus = ImageButton(context!!)
        ibPlus.setImageResource(R.drawable.ic_add_black_24dp)
        ibPlus.background = resources.getDrawable(android.R.color.transparent, null)
        ibPlus.setOnClickListener(this)
        llEnd.addView(ibPlus)
    }
}