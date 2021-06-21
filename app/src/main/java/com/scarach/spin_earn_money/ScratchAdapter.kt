package com.scarach.spin_earn_money

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scarach.spin_earn_money.databinding.ScratchItemBinding


class ScratchAdapter(val context: Context, private val scratchList: ArrayList<Scratch>) :
    RecyclerView.Adapter<ScratchAdapter.ScratchViewHolder>() {
    class ScratchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ScratchItemBinding.bind(itemView)
        var scratchTv = binding.randomScratch
        var scratchCardView = binding.scratchCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScratchViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.scratch_item, parent, false)
        return ScratchViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScratchViewHolder, position: Int) {
        val scratch = scratchList[position]
    }

    override fun getItemCount(): Int {
        return scratchList.size
    }

}