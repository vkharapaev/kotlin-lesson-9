package com.headmostlab.usercontacts.ui.main

import com.headmostlab.usercontacts.domain.Contact

sealed class AppState {
    object Loading : AppState()
    class Loaded(val contacts: List<UiContact>) : AppState()
    class Error(val error: Throwable) : AppState()
}
