package com.headmostlab.usercontacts.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ContactAdapter : ListAdapter<UiContact, ContactViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContactViewHolder(parent)
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<UiContact>() {
            override fun areItemsTheSame(oldItem: UiContact, newItem: UiContact) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: UiContact, newItem: UiContact) =
                oldItem == newItem
        }
    }

}
