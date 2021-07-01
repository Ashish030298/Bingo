package com.scarach.spin_earn_money

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.scarach.spin_earn_money.databinding.WithdrawItemBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WithdrawAdapter(val context: Context,
                      private val withdrawalList: ArrayList<Withdrawal>):
    RecyclerView.Adapter<WithdrawAdapter.WithdrawalViewHolder>() {
    class WithdrawalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = WithdrawItemBinding.bind(itemView)
        val title: TextView = binding.withdrawTypeTv
        val time: TextView = binding.withdrawTiime
        val coin: TextView = binding.withdrawCoin

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): WithdrawalViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.withdraw_item, parent, false)
        return WithdrawalViewHolder(view)
    }

    override fun onBindViewHolder(holder: WithdrawalViewHolder, position: Int) {
         val withdraw = withdrawalList[position]
        holder.title.text = withdraw.withdrawalAccount + " \"${withdraw.withdrawalType.trim()}\""
        holder.time.text = getDate(withdraw.timeStamp.toLong(),"dd/MM/yyyy hh:mm:ss")
        holder.coin.text = withdraw.withdrawalCoin.toString()

    }

    override fun getItemCount(): Int {
        return withdrawalList.size
    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }
}