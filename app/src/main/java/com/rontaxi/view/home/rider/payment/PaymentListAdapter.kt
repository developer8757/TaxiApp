package com.rontaxi.view.home.rider.payment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rontaxi.R
import com.rontaxi.util.showAlert
import java.util.*
import kotlinx.android.synthetic.main.payment_list_view.view.*

class PaymentListAdapter : RecyclerView.Adapter<PaymentListAdapter.DataHolder>() {

    var cardList = ArrayList<Payment>()

    lateinit var paymentListInterface: PaymentListInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.payment_list_view, parent, false)
        return DataHolder(view)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {
        holder.bind(cardList[position])

    }

    inner class DataHolder(view: View) : RecyclerView.ViewHolder(view) {
        var heightInitial = 0

        init {
            itemView.swipeLayout?.post {
                heightInitial = itemView.swipeLayout.layoutParams.height
                itemView.llDelete.layoutParams.height = heightInitial
            }
        }

        fun bind(data: Payment) {

            if(data.category==0)
            {
                itemView.swipeLayout.dragLock(true)
            }
            if (data.category == 0) {
                itemView.tvCard.text = "Cash"//data.type!!.trim().toUpperCase()
                itemView.ivPayment.setImageResource(R.drawable.cash_payment)
                itemView.tvExpDate.text = ""
                itemView.tvExpDate.visibility=View.GONE
            } else if (data.category == 1) {

                itemView.ivPayment.setImageResource(R.drawable.credit_card)
                itemView.tvCard.text = "**** **** **** " + data?.last4
                itemView.tvExpDate.text = "Exp Date: " + data!!.exp_month + "/" + data!!.exp_year
                itemView.tvExpDate.visibility=View.VISIBLE

            }


            itemView.ivDelete.setOnClickListener {
                    paymentListInterface.removeCard(cardList[adapterPosition], adapterPosition)
            }

            if (data.isSelected == true)
                itemView.checkPayment.visibility = View.VISIBLE
            else
                itemView.checkPayment.visibility = View.GONE

            itemView.clShowCards.setOnClickListener {
                paymentListInterface.onItemClick(cardList[adapterPosition], adapterPosition)

            }

        }
    }

    interface PaymentListInterface {
        fun onItemClick(cardData: Payment, position: Int)
        fun removeCard(cardData: Payment, position: Int)

    }

}

