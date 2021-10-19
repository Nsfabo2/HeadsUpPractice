package com.example.headsupprep

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.headsupprep.Celebrity
import com.example.headsupprep.databinding.CelebrityItemRowBinding

class RcyclerViewAdapter(private var celebrities: ArrayList<Celebrity>): RecyclerView.Adapter<RcyclerViewAdapter.ItemViewHolder>(){

    class ItemViewHolder(val binding: CelebrityItemRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            CelebrityItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val celebrity = celebrities[position]

        holder.binding.apply {
            NameTV.text = celebrity.name
            Taboo1TV.text = celebrity.taboo1
            Taboo2TV.text = celebrity.taboo2
            Taboo3TV.text = celebrity.taboo3
        }
    }

    override fun getItemCount() = celebrities.size

    fun update(celebrities: ArrayList<Celebrity>){
        this.celebrities = celebrities
        notifyDataSetChanged()
    }
}