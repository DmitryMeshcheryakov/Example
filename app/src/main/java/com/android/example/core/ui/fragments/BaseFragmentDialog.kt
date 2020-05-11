package com.android.example.core.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatDialogFragment
import com.android.example.core.ui.activities.BaseActivity
import com.android.example.core.ui.dialogs.BaseDialog

abstract class BaseFragmentDialog : AppCompatDialogFragment() {

    @ColorRes
    open var navigationBarColor: Int? = null
        set(value) {
            field = value
            screenManager?.setNavBarColor(value)
        }
    @ColorRes
    open var statusBarColor: Int? = null
        set(value) {
            field = value
            screenManager?.setStatusBarColor(value)
        }

    val screenManager
        get() = activity?.screenManager

    val activity
        get() = getActivity() as? BaseActivity?

    override fun startActivity(intent: Intent?) {
        activity?.showPinOnStart = false
        super.startActivity(intent)
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        activity?.showPinOnStart = false
        super.startActivity(intent, options)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        activity?.showPinOnStart = false
        super.startActivityForResult(intent, requestCode)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        activity?.showPinOnStart = false
        super.startActivityForResult(intent, requestCode, options)
    }

    override fun onResume() {
        super.onResume()
        screenManager?.restoreToolbarState(this)
    }

    open fun startExternalActivity(intent: Intent?, requestCode: Int = -1, options: Bundle? = null) {
        activity?.showPinOnStart = true
        super.startActivityForResult(intent, requestCode, options)
    }

    open fun showProgress(show: Boolean) {
        screenManager?.showProgress(show)
    }

    open fun showDialog(dialog: BaseDialog.Builder) {
        screenManager?.showDialog(dialog)
    }

    open fun showStartScreen() {
        screenManager?.showStartScreen()
    }

    open fun onBack() {
        dismiss()
    }
}