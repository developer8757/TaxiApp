package com.rontaxi.view.home.rider.help

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import com.rontaxi.R
import com.rontaxi.base.BaseFragment
import com.rontaxi.cache.getUser
import com.rontaxi.model.RaiseIssueRequest
import com.rontaxi.model.ReasonModel
import com.rontaxi.model.SubmitQueryRequest
import com.rontaxi.retrofit.getErrorMessage
import com.rontaxi.util.ProgressDialog
import com.rontaxi.util.Validations
import com.rontaxi.util.dpToPx
import com.rontaxi.util.showAlert
import com.rontaxi.view.erroraction.tackleError
import com.rontaxi.view.home.rider.godview.CancellationReasonsDialog
import kotlinx.android.synthetic.main.fragment_help.*
import kotlinx.android.synthetic.main.tool_bar_generic.*
import javax.inject.Inject

class HelpFragment : BaseFragment(), View.OnClickListener {

    @Inject
    lateinit var viewModel: HelpViewModel
    var bookingId = ""
    var selectedReasonModel: ReasonModel? = null
    val dialogReason: CancellationReasonsDialog by lazy {
        CancellationReasonsDialog(context!!)
    }

    override fun getLayoutRes() = R.layout.fragment_help
    override fun showTitleBar() = false
    var  isRaiseIssue=false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view?.viewTreeObserver?.addOnGlobalLayoutListener(keyBoardObserver)
        if (isLoaded)
            return
        etEmail.setText(getUser(context!!)!!.email)
        setToolbar()
        setListeners()
        addObservers()
    }

    private fun addObservers() {
        viewModel.helpReasonsLiveData.observe(this, Observer {
            ProgressDialog.hideProgressBar()
            if (it!!.isSuccessful) {
                showCancellationDialog(it.body()!!.data!!)
            } else {
                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }
        })

        viewModel.submitQueryLiveData.observe(this, Observer {
            ProgressDialog.hideProgressBar()
            if (it!!.isSuccessful) {
                showAlert(context!!, getString(R.string.query_submission), getString(R.string.ok)) {
                    (activity)!!.supportFragmentManager.popBackStackImmediate()
                }
            } else {
                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }
        })
        viewModel.raiseIssueLiveData.observe(this, Observer {
            ProgressDialog.hideProgressBar()
            if (it!!.isSuccessful) {
                showAlert(context!!, getString(R.string.query_submission), getString(R.string.ok)) {
                    (activity)!!.supportFragmentManager.popBackStackImmediate()
                }
            } else {
                tackleError(activity!!, getErrorMessage(it.errorBody()!!))
            }
        })


    }

    private fun setListeners() {
        etQuery.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
    }

    override fun onPause() {
        super.onPause()
        view?.viewTreeObserver?.removeOnGlobalLayoutListener(keyBoardObserver)
    }

    private fun showCancellationDialog(data: ArrayList<ReasonModel>) {
        dialogReason.reasonsArrayList.clear()
        dialogReason.reasonsArrayList.addAll(data)

        dialogReason.setTitle(getString(R.string.help_subject))
        dialogReason.cancellationDialogInterface = object : CancellationReasonsDialog.CancellationDialogInterface {
            override fun onDonePressed(reasonModel: ReasonModel) {
                selectedReasonModel = reasonModel
                etQuery.setText(reasonModel.reason)
            }
        }
        dialogReason.show()
    }

    override fun onClick(p0: View?)
    {
        when(p0?.id)
        {
            R.id.etQuery ->
            {
                getHelpReasons()
            }
            R.id.btnSubmit ->
            {
                getIssueRequest()
            }
        }

    }

    private fun getIssueRequest() {
        if (Validations.isEmpty(context!!, etEmail)
            && Validations.isValidEmail(context!!, etEmail)
            && Validations.isEmpty(context!!, etQuery)
            && Validations.isEmpty(context!!, etMessage)
        ) {

            ProgressDialog.showProgressBar(context!!, "", false)
            if (isRaiseIssue) {
                val raiseIssueRequest = RaiseIssueRequest()
                raiseIssueRequest.bookingId = bookingId
                raiseIssueRequest.reasonId = selectedReasonModel!!.reasonId
                raiseIssueRequest.comment=etMessage.text.toString()
                viewModel.raiseIssueLiveData(raiseIssueRequest)
            }
            else {

                val request = SubmitQueryRequest()
                request.email = etEmail.text.toString()
                request.reasonId = selectedReasonModel!!.reasonId
                request.comment = etMessage.text.toString()
                viewModel.submitQueryLiveData(request)
            }
        }
    }

    private fun getHelpReasons() {
        if (viewModel.helpReasonsLiveData.value != null) {
            showCancellationDialog(viewModel.helpReasonsLiveData.value!!.body()!!.data!!)
        } else {
            ProgressDialog.showProgressBar(context!!, "", false)
            if (isRaiseIssue)
                viewModel.getHelpReasons(2)
            else
                viewModel.getHelpReasons(3)
        }
    }

    private fun setToolbar() {
        val textTitle = TextView(context)
        textTitle.text = resources.getString(R.string.help)
        textTitle.setTextColor(resources.getColor(R.color.white))
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.sp_20))
        llMiddle.removeAllViews()
        llMiddle.addView(textTitle)
        llMiddle.gravity = (Gravity.START or Gravity.CENTER_VERTICAL)
        llEnd.removeAllViews()
        // llEnd.addView(ibFilter)
        // llEnd.addView(ibBell)
        toolBarGeneric.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolBarGeneric.background = resources.getDrawable(R.color.md_black_1000, null)
        toolBarGeneric.navigationIcon?.setTint(ContextCompat.getColor(activity!!, R.color.white))
        toolBarGeneric.setNavigationOnClickListener {
            activity!!.onBackPressed()
        }
    }

    var keyBoardObserver = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {

            val heightDiff = view!!.rootView.height - view!!.height
            if (heightDiff > dpToPx(context!!, 200f)) { // if more than 200 dp, it's probably a keyboard...
                // ... do something here
                btnSubmit.visibility = View.GONE
            } else {
                btnSubmit.visibility = View.VISIBLE
            }
        }
    }
}