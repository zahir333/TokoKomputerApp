package com.example.tokokomputer.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tokokomputer.R
import com.example.tokokomputer.model.Computer

class ComputerListAdapter(
   private val onItemClickListener: (Computer) -> Unit
): ListAdapter<Computer, ComputerListAdapter.ComputerViewHolder>(WORDS_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComputerViewHolder {
        return ComputerViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ComputerViewHolder, position: Int) {
        val computer = getItem(position)
        holder.bind(computer)
        holder.itemView.setOnClickListener {
            onItemClickListener(computer)
        }
    }

    class ComputerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        private val telephoneTextView: TextView = itemView.findViewById(R.id.telephoneTextView)
        fun bind(computer: Computer?) {
            nameTextView.text = computer?.name
            addressTextView.text = computer?.address
            telephoneTextView.text = computer?.telephone
        }

        companion object {
            fun create(parent: ViewGroup): ComputerListAdapter.ComputerViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_computer, parent, false)
                return ComputerViewHolder(view)
            }

        }

    }
    companion object{
        private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<Computer>(){
            override fun areItemsTheSame(oldItem: Computer, newItem: Computer): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Computer, newItem: Computer): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}