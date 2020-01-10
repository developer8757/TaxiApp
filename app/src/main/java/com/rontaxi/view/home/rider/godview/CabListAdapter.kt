package com.rontaxi.view.home.rider.godview

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rontaxi.R
import com.rontaxi.model.nearbycabs.CabsDetails
import com.rontaxi.util.loadProfileImageFromURL
import com.rontaxi.view.initial.driver.vehicletype.CarType
import kotlinx.android.synthetic.main.item_cabs.view.*

class CabListAdapter: RecyclerView.Adapter<CabListAdapter.CabViewHolder>() {



    lateinit var  cabListListner: CabListListener
    open var data=ArrayList<CabsDetails>()

    var matrix = ColorMatrix()
    var filter:ColorMatrixColorFilter?=null

    init {
        matrix.setSaturation(0.0f)
        filter=ColorMatrixColorFilter(matrix)

    }



    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): CabViewHolder {

        return CabViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_cabs,viewGroup,false))
    }

    override fun getItemCount()=data.size


    override fun onBindViewHolder(viewHolder: CabViewHolder, position: Int) {
        viewHolder.bindView(position)
    }

    inner class CabViewHolder(view: View): RecyclerView.ViewHolder(view){

        fun bindView(position: Int){

            itemView.apply {

               ivCab.loadProfileImageFromURL(context!!,data[position].image)


                tvCabType.text=data[position].carType

                if(data[position].isSelected){
                    ivCab.setColorFilter(null)
                }else{
                    ivCab.setColorFilter(filter)

                }
               tvFarePrice.text=data[position].farePrice

                setOnClickListener {
                    data.forEach {
                        it.isSelected=false
                    }

                    data[position].isSelected=true


                    cabListListner.onCabSelected(data[position])

                    notifyDataSetChanged()


                }

            }
        }

    }


    interface CabListListener{

        fun onCabSelected(selectedCab: CabsDetails)

    }
}