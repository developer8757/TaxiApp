package com.rontaxi.view.home.rider.help

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.model.RaiseIssueRequest
import com.rontaxi.model.SubmitQueryRequest

class HelpViewModel(app: Application) : AndroidViewModel(app) {
    lateinit var helpReasonsLiveData: HelpReasonsLiveData

    lateinit var submitQueryLiveData: SubmitQueryLiveData

    lateinit var raiseIssueLiveData: RaiseIssueLiveData

    fun getHelpReasons(type: Int) {
        helpReasonsLiveData.getHelpPoints(type)
    }

    fun submitQueryLiveData(request: SubmitQueryRequest) {
        submitQueryLiveData.submitQuery(request)
    }

    fun raiseIssueLiveData(request: RaiseIssueRequest) {
        raiseIssueLiveData.raiseIssueQuery(request)
    }
}