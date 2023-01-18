package com.example.expensetracking

import android.content.ClipData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracking.data.Entry
import com.example.expensetracking.databinding.EntryListItemBinding

/**
 * [ListAdapter] implementation for the recyclerview.
 */
class EntryListAdapter(private val onEntryClicked: (Entry) -> Unit) :
    ListAdapter<Entry, EntryListAdapter.EntryViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        return EntryViewHolder(
            EntryListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onEntryClicked(current)
        }
        holder.bind(current)
    }

    class EntryViewHolder(private var binding: EntryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: Entry) {
            binding.entryName.text = entry.entryName
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Entry>() {
            override fun areItemsTheSame(oldItem: Entry, newItem: Entry): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Entry, newItem: Entry): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}