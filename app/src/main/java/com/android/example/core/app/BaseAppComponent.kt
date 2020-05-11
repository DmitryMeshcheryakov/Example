package com.android.example.core.app

import com.android.example.core.ui.dialogs.DialogBiometric

interface BaseAppComponent {

    fun inject(dialogBiometric: DialogBiometric)
}