package com.raywenderlich.rw_sec2_listmaker.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.rw_sec2_listmaker.databinding.DetailItemViewHolderBinding
import com.raywenderlich.rw_sec2_listmaker.model.TaskList

class DettailItemRecyclerViewAdapter(var list: TaskList) :
    RecyclerView.Adapter<DetailItemViewHolder>() {
    lateinit var onItemClick: ((String) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailItemViewHolder {
        val binding =
            DetailItemViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailItemViewHolder, position: Int) {
        holder.binding.textViewTask.text = list.tasks[position]
        holder.itemView.setOnClickListener {
            onItemClick.invoke(list.tasks[position])
        }
    }

    override fun getItemCount(): Int = list.tasks.size
}