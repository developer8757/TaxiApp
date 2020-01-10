package com.rontaxi.view.home.driver.home.map

import com.rontaxi.model.ReasonModel
import com.rontaxi.util.ProgressDialog
import com.rontaxi.view.home.rider.godview.CancellationReasonsDialog

fun MapViewFragment.getCancellationReasons(){


/*    if(mapViewModel.getCancellationReasonsLiveData.value!=null){

        showCancellationDialog(mapViewModel.getCancellationReasonsLiveData.value?.body()?.data!!)

    }else{*/


        ProgressDialog.showProgressBar(context!!, "",false)
        mapViewModel.getCancellationReasons()
   // }


}

fun MapViewFragment.showCancellationDialog(data: ArrayList<ReasonModel>){

    cancellationDialog= CancellationReasonsDialog(context!!)
    cancellationDialog!!.reasonsArrayList.addAll(data)
    cancellationDialog!!.cancellationDialogInterface=object: CancellationReasonsDialog.CancellationDialogInterface{


        override fun onDonePressed(reasonModel: ReasonModel) {
            mapViewModel.cancelBooking(reasonModel.reasonId)
        }



    }

    cancellationDialog!!.show()


}