package com.rontaxi.view.initial.driver.vehicletype

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rontaxi.R
import com.rontaxi.util.loadImage
import kotlinx.android.synthetic.main.item_vehicle_type.view.*

class VehicleTypeAdapter : RecyclerView.Adapter<VehicleTypeAdapter.Holder>(){


    var data=ArrayList<CarType>()




    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        return Holder(LayoutInflater.from(p0.context).inflate(R.layout.item_vehicle_type,p0,false))
    }

    override fun getItemCount()=data.size

    override fun onBindViewHolder(p0: Holder, p1: Int) {
        p0.bind(p1)
    }

    inner class Holder(view: View): RecyclerView.ViewHolder(view){

        fun bind(position : Int){

            itemView?.apply {

                ivCarType.loadImage(context!!,data[position].image)
                tvCarType.text=data[position].name
                tvDetails.text=data[position].description
                tvMaxPassegerCount.text=data[position].passengerCapacity+context.getString(R.string.max_person)

                if(data[position].isSelected){

                    clDetails.visibility=View.VISIBLE
                    ivTick.visibility=View.VISIBLE
                }else{
                    clDetails.visibility=View.GONE
                    ivTick.visibility=View.INVISIBLE

                }


                setOnClickListener {

                    data.forEach {
                        it.isSelected=false

                    }

                    data[position].isSelected=true

                    notifyDataSetChanged()



                }

            }

        }

    }

    interface VehicleTypeInterface{

        fun onVehicleSelected(carType: CarType)
    }
}