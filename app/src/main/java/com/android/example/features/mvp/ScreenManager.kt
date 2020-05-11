package com.android.example.features.mvp

import androidx.appcompat.app.AppCompatActivity
import com.android.example.App
import com.android.example.R
import com.android.example.core.ui.dialogs.BaseDialog
import com.android.example.core.ui.dialogs.Dialog
import com.android.example.core.ui.screenmanagers.BaseScreenManager

class ScreenManager(activity: AppCompatActivity) : BaseScreenManager(activity) {

    init {
        App.component().inject(this)
    }

    override fun showStartScreen() {}

    override var progressDialog: BaseDialog.Builder? = Dialog.builder().setLayoutId(R.layout.dialog_progress).setCancelable(false)

    fun showMainScreen() {}
}