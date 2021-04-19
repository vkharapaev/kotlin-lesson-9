package com.headmostlab.usercontacts.ui.main

import android.content.ContentProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.headmostlab.usercontacts.data.ContactRepository

class MainViewModelFactory(private val contactRepository: ContactRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(contactRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
