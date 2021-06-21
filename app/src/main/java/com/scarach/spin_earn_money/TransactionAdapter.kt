@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.scarach.spin_earn_money

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.scarach.spin_earn_money.databinding.TransactionItemBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter(
    val context: Context,
    private val transactionList: ArrayList<Transaction>,
) : Adapter<TransactionAdapter.TransactionViewHolder>() {
    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TransactionItemBinding.bind(itemView)
        var title: TextView = binding.title
        var time: TextView = binding.time
        var coin: TextView = binding.coin

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.title.text = transaction.title
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH)
        try {
            holder.time.text = convertToCustomFormat(transaction.time)
        } catch (ex: ParseException) {
            println(ex.toString())
        }
        /*val date = dateFormat.parse(transaction.time)
        Log.d("TAG", "onBindViewHolder: $date")
        //holder.time.text = timeFormat.parse(date.toString()).toString()*/
        holder.coin.text = transaction.coin
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    private fun convertToCustomFormat(dateStr: String?): String {
        val utc = TimeZone.getTimeZone("UTC")
        val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
        val destFormat = SimpleDateFormat("dd-MMM-YYYY HH:mm aa")
        sourceFormat.timeZone = utc
        val convertedDate = sourceFormat.parse(dateStr)
        return destFormat.format(convertedDate)
    }
}