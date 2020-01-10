package com.rontaxi.view.home.rider.godview

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.rontaxi.R
import com.rontaxi.model.ReasonModel
import com.rontaxi.view.home.rider.ridehistory.BlockDriverModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.dialog_cancelleation_reasons.*

class CancellationReasonsDialog(context: Context): Dialog(context) {

    var reasonsArrayList= ArrayList<ReasonModel>()
    lateinit var adapter:CancellationReasonsAdapter
    
    lateinit var cancellationDialogInterface: CancellationDialogInterface
    
    var reasonModel: ReasonModel?=null


    var titleTop=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_cancelleation_reasons)

        rvReasons.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        adapter=CancellationReasonsAdapter()

        adapter.cancellationInterface=object : CancellationReasonsAdapter.CancellationReasonInterface{

            override fun onReasonSelected(reason: ReasonModel) {
                reasonModel=reason
            }
        }
        adapter.data.addAll(reasonsArrayList)
        rvReasons.adapter=adapter
        rvReasons.adapter!!.notifyDataSetChanged()

        tvCancel.setOnClickListener {

            dismiss()
        }

        tvDone.setOnClickListener {
            
            if(reasonModel==null){
            
                Toasty.info(context,context.getString(R.string.select_reason)).show()
                
                return@setOnClickListener
            }
            cancellationDialogInterface.onDonePressed(reasonModel!!)
                
            dismiss()
        }

        if(titleTop.isNotEmpty())
            tvTitle.text=titleTop
    }



    fun setTitle(title: String){
        titleTop=title
    }

    interface CancellationDialogInterface{
        fun onDonePressed(reasonModel: ReasonModel)
    }


}