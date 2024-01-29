package com.example.ekran.feature.main.chip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ekran.R
import com.example.ekran.model.Category
import com.google.android.material.chip.Chip

class ChipAdapter: RecyclerView.Adapter<ChipAdapter.ViewHolder>() {

    var category = ArrayList<Category>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val chip = itemView.findViewById<Chip>(R.id.categoryChip)

        fun bindChip(category: Category){
            chip.text = category.category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_search,parent,false))
    }

    override fun getItemCount(): Int  = category.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindChip(category[position])
}