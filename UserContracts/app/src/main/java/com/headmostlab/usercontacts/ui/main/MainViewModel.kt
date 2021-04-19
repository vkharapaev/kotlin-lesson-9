package com.headmostlab.usercontacts.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.headmostlab.usercontacts.data.ContactRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val contactRepository: ContactRepository,
    private val appState: MutableLiveData<AppState> = MutableLiveData(),
    private val compositeDisposables: CompositeDisposable = CompositeDisposable()
) : ViewModel() {
    fun getContacts(): LiveData<AppState> {
        appState.value = AppState.Loading
        contactRepository.getContracts().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.map { UiContact(it.name, it.numbers?.joinToString(separator = "\n")) }
            }
            .subscribe({
                appState.value = AppState.Loaded(it)
            }, {
                appState.value = AppState.Error(it)
            }).also { compositeDisposables.add(it) }
        return appState
    }

    override fun onCleared() {
        compositeDisposables.clear()
    }
}
