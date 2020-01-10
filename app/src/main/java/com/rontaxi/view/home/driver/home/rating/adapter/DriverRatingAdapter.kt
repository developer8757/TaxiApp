package com.rontaxi.view.home.driver.home.rating.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rontaxi.R
import com.rontaxi.model.driverrating.RatingData
import com.rontaxi.util.loadProfileImageFromURL
import kotlinx.android.synthetic.main.item_driver_rating.view.*

class DriverRatingAdapter : RecyclerView.Adapter<DriverRatingAdapter.Holder>() {


    var driverRatingArrayList = ArrayList<RatingData>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {

        return Holder(LayoutInflater.from(p0.context).inflate(R.layout.item_driver_rating, p0, false))
    }


    override fun getItemCount(): Int = driverRatingArrayList.size


    override fun onBindViewHolder(p0: Holder, p1: Int) {
        p0.bind(driverRatingArrayList[p1])
    }


    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(ratingData: RatingData) {
            itemView?.apply {

                itemView.tvRiderName.text = ratingData.riderName
                itemView.tvComment.text = ratingData.reviewText
                itemView.tvDate.text = ratingData.date
                itemView.ratingRiderDetail.rating = ratingData.rating.toFloat()
                itemView.ivRiderPic.loadProfileImageFromURL(context, ratingData.profilePhoto)
            }
        }
    }
}
