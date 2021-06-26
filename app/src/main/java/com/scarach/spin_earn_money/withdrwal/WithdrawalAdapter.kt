package com.scarach.spin_earn_money.withdrwal

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.scarach.spin_earn_money.AdminWithdrawal
import com.scarach.spin_earn_money.R
import com.scarach.spin_earn_money.databinding.WithdrawalItemBinding

class WithdrawalAdapter(val context: Context, val withdrawalArrayList:ArrayList<AdminWithdrawal>):
    RecyclerView.Adapter<WithdrawalAdapter.WithdrawalViewHolder>() {
    class WithdrawalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = WithdrawalItemBinding.bind(itemView)
        val img = binding.withdrawalImg
        val tv = binding.title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WithdrawalViewHolder {
        return WithdrawalViewHolder(LayoutInflater.from(context).inflate(R.layout.withdrawal_item, parent, false))
    }

    override fun onBindViewHolder(holder: WithdrawalViewHolder, position: Int) {
       val listData = withdrawalArrayList[position]
        val uri:Uri = listData.imgurl.toUri()
        holder.img.setImageURI(uri)
        holder.tv.text = "${listData.name}\nminimum withdrawal: Rs${listData.minimum}"
    }

    override fun getItemCount(): Int {
       return withdrawalArrayList.size
    }
}


