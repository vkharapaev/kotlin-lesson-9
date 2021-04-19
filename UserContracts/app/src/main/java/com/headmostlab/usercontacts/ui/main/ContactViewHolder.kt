package com.headmostlab.usercontacts.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.headmostlab.usercontacts.databinding.ContractItemBinding

class ContactViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        ContractItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
    ) {
    private val binding = ContractItemBinding.bind(itemView)

    fun bind(contract: UiContact) {
        binding.textView.text = contract.name
        binding.numbers.text = contract.numbers
    }
}
