package com.raywenderlich.rw_listmaker_sec2.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.rw_listmaker_sec2.databinding.DetailItemViewHolderBinding
import com.raywenderlich.rw_listmaker_sec2.model.TaskList

class DettailItemRecyclerViewAdapter(var list: TaskList) :
    RecyclerView.Adapter<DetailItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailItemViewHolder {
        val binding =
            DetailItemViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailItemViewHolder, position: Int) {
        holder.binding.textViewTask.text = list.tasks[position]
    }

    override fun getItemCount(): Int = list.tasks.size
}