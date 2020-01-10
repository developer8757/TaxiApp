package com.rontaxi.view.home.driver.home.earnings.adapterearningdriver

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rontaxi.R
import com.rontaxi.model.drivingearning.DriverEarningBooking
import com.rontaxi.util.loadImage
import com.rontaxi.util.loadProfileImageFromURL
import kotlinx.android.synthetic.main.item_driver_earning_today.view.*
import java.sql.Timestamp

class DriverEarningAdapter(context: Context) :
    RecyclerView.Adapter<DriverEarningAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.item_driver_earning_today,
                viewGroup,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    var data = ArrayList<DriverEarningBooking>()


    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.bind(data.get(p1))

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(driverEarning: DriverEarningBooking) {
            itemView.apply {
                tvEarningDriverName.text =
                    "${driverEarning.riderObj.firstName} ${driverEarning.riderObj.lastName}"
                val timeStamp = Timestamp(driverEarning.createdAt.toLong())
                tvDriverEarningTime.text =
                    DateFormat.format("dd-MM-yyyy hh:mm a", timeStamp).toString()
                //    tvDriverEarning.text="${"$"} ${driverEarning.totalCost}"
                tvDriverEarning.text = driverEarning.displayAmount
                imvEarningDriver.loadProfileImageFromURL(
                    context,
                    driverEarning.riderObj.profileImage
                )
                driverEarning.paymentMethod.apply {
                    when (this) {
                        0 -> {
                            tvCustomerPaymentMethod.text =
                                resources.getString(R.string.cash_payment)
                        }
                        1 -> {
                            tvCustomerPaymentMethod.text =
                                resources.getString(R.string.card_payment)
                        }
                    }
                }
            }


        }
    }
}