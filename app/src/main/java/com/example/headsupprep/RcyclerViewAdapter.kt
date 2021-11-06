package com.example.headsupprep

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.headsupprep.Celebrity
import com.example.headsupprep.databinding.CelebrityItemRowBinding

class RcyclerViewAdapter(private val activity: Data,private var celebrities: ArrayList<Celebrity>): RecyclerView.Adapter<RcyclerViewAdapter.ItemViewHolder>(){

    class ItemViewHolder(val binding: CelebrityItemRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            CelebrityItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val celebrity = celebrities[position]

        holder.binding.apply {
            tvCelebrity.text = "${celebrity.name} - ${celebrity.taboo1} - ${celebrity.taboo2} - ${celebrity.taboo3}"
            tvCelebrity.setOnClickListener { activity.updateCelebrity(celebrity) }
            btDeleteCelebrity.setOnClickListener { activity.deleteCelebrity(celebrity) }
        }
    }

    override fun getItemCount() = celebrities.size

    fun update(celebrities: ArrayList<Celebrity>){
        this.celebrities = celebrities
        notifyDataSetChanged()
    }
}