package com.manav.demoapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manav.demoapplication.databinding.ItemAirlinesBinding
import com.manav.demoapplication.response.AllAirlines
import com.manav.demoapplication.response.AllAirlinesItem


class AdapterAirLines(
    private var mList: AllAirlines,
    private val listener: AirlineCLickedListener
) :
    RecyclerView.Adapter<AdapterAirLines.ViewHolder>() {
    lateinit var binding: ItemAirlinesBinding

    interface AirlineCLickedListener {
        fun onAirlineCLicked(airlinesItem: AllAirlinesItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemAirlinesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = mList[position]
        holder.bind(binding, items, listener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            binding: ItemAirlinesBinding,
            items: AllAirlinesItem,
            listener: AirlineCLickedListener
        ) {
            binding.tvCarrierName.text = items.name
            binding.tvCountryName.text = items.country
            binding.tvEstablished.text = items.established
            binding.tvHeadQuarters.text = items.head_quaters
            Glide.with(itemView.context).load(items.logo).into(binding.imgLogo)

            itemView.setOnClickListener {
                listener.onAirlineCLicked(items)
            }
        }
    }

    fun setList(mList: AllAirlines) {
        this.mList = mList
        notifyDataSetChanged()
    }
}