package com.rontaxi.view.home.rider.godview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rontaxi.R
import com.rontaxi.model.ReasonModel
import kotlinx.android.synthetic.main.item_cancellation_reasons.view.*

class CancellationReasonsAdapter: RecyclerView.Adapter<CancellationReasonsAdapter.Holder>() {

    var data= ArrayList<ReasonModel>()

    lateinit var cancellationInterface: CancellationReasonInterface

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {

        return Holder(LayoutInflater.from(p0.context).inflate(R.layout.item_cancellation_reasons, p0, false))
    }

    override fun getItemCount()=data.size

    override fun onBindViewHolder(p0: Holder, p1: Int) {

        p0.bind(p1)
    }

    inner class Holder(view: View): RecyclerView.ViewHolder(view){

        fun bind(position: Int){

            itemView.apply {

                tvReason.text=data[position].reason

                if(data[position].isSelected)
                    cbSelect.visibility=View.VISIBLE
                else
                    cbSelect.visibility=View.INVISIBLE

                setOnClickListener {

                    data.forEach {

                        it.isSelected=false
                    }
                    data[position].isSelected=true

                    notifyDataSetChanged()

                    cancellationInterface.onReasonSelected(data[position])
                }
            }

        }
    }

    interface CancellationReasonInterface{

        fun onReasonSelected(reason: ReasonModel)

    }
}