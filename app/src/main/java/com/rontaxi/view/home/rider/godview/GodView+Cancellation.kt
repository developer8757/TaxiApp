package com.rontaxi.view.home.rider.godview

import com.rontaxi.model.ReasonModel
import com.rontaxi.util.ProgressDialog

fun GodViewFragment.getCancellationReasons(){


  /*  if(godViewModel.getCancellationReasonsLiveData.value!=null){

        showCancellationDialog(godViewModel.getCancellationReasonsLiveData.value?.body()?.data!!)

    }else{*/


        ProgressDialog.showProgressBar(context!!, "",false)
        godViewModel.getCancellationReasons()
   // }


}

fun GodViewFragment.showCancellationDialog(data: ArrayList<ReasonModel>){

    cancellationDialog= CancellationReasonsDialog(context!!)
    cancellationDialog!!.reasonsArrayList.addAll(data)
    cancellationDialog!!.cancellationDialogInterface=object: CancellationReasonsDialog.CancellationDialogInterface{

        override fun onDonePressed(reasonModel: ReasonModel) {

            godViewModel.cancelBooking(reasonModel.reasonId)
        }


    }

    cancellationDialog!!.show()


}