package com.raywenderlich.rw_sec2_listmaker.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.rw_sec2_listmaker.databinding.ListSelectionViewHolderBinding
import com.raywenderlich.rw_sec2_listmaker.model.TaskList

class ListSelectionRecyclerViewAdapter(
    private val lists: MutableList<TaskList>,
    val clickListener: ListSelectionRecyclerViewClickListener
) : RecyclerView.Adapter<ListSelectionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSelectionViewHolder {
        val binding = ListSelectionViewHolderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ListSelectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListSelectionViewHolder, position: Int) {
        holder.binding.itemNumber.text = (position + 1).toString()
        holder.binding.itemString.text = lists[position].name
        holder.itemView.setOnClickListener {
            clickListener.listItemClicked(lists[position])
        }
    }

    override fun getItemCount(): Int = lists.size

    fun listsUpdated() {
        notifyItemInserted(lists.size - 1)
    }

    interface ListSelectionRecyclerViewClickListener {
        fun listItemClicked(list: TaskList)
    }
}