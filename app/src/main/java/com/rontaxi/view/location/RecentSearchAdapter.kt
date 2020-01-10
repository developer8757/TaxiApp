package com.rontaxi.view.location

import android.location.Address
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.rontaxi.R
import com.rontaxi.model.map.RecentSearchModel
import kotlinx.android.synthetic.main.item_location_search.view.*

class RecentSearchAdapter: RecyclerView.Adapter<RecentSearchAdapter.Holder>() {



    var data=ArrayList<RecentSearchModel>()

    lateinit var locationRecentSearchAdapterInterface: RecentSearchAdapterInterface

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {

        return Holder(LayoutInflater.from(p0.context).inflate(R.layout.item_location_search,p0,false))
    }

    override fun getItemCount(): Int {

        return data.size
    }

    override fun onBindViewHolder(p0: Holder, p1: Int) {
        p0.bind(p1)
    }

    inner class Holder(view: View): RecyclerView.ViewHolder(view){

        fun bind(position: Int){


            itemView?.apply {
                tvLocation.text=data[position].primaryAddress
                tvLocality.text=data[position]
                    .secondaryAddress

                setOnClickListener {

                    locationRecentSearchAdapterInterface.onAddressTapped(data[position])
                }


            }



        }

    }

    interface RecentSearchAdapterInterface{

        fun onAddressTapped(prediction: RecentSearchModel)
    }

}