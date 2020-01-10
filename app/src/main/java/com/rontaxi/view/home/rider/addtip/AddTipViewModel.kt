package com.rontaxi.view.home.rider.addtip

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.rontaxi.model.AddTipModel

class AddTipViewModel (app: Application): AndroidViewModel(app) {
    lateinit var addTipLiveData: AddTipLiveData

    fun addTipAmount(tipModel: AddTipModel) {
        addTipLiveData.addTip(tipModel)
    }
}