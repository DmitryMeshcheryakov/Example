package com.android.example.core.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.android.example.core.ui.activities.BaseActivity
import com.android.example.core.ui.dialogs.BaseDialog
import com.android.example.core.utils.BaseUtils
import com.android.example.features.mvp.ScreenManager

abstract class BaseFragment : Fragment {

    constructor() : super()

    constructor(@LayoutRes layoutId: Int) : super(layoutId)

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
    @ColorRes
    open var toolbarBarColor: Int? = null
        set(value) {
            field = value
            screenManager?.restoreToolbarState(this)
        }
    open var showToolbar: Boolean = true
        set(value) {
            field = value
            screenManager?.restoreToolbarState(this)
        }

    @DrawableRes
    open var navigationIcon: Int? = null
        set(value) {
            field = value
            screenManager?.restoreToolbarState(this)
        }
    @StringRes
    open var titleId: Int? = null
        set(value) {
            field = value
            screenManager?.restoreToolbarState(this)
        }
    open var titleString: String? = null
        set(value) {
            field = value
            screenManager?.restoreToolbarState(this)
        }

    open var ignoreState: Boolean = false
        set(value) {
            field = value
            screenManager?.restoreToolbarState(this)
        }

    val screenManager
        get() = activity?.screenManager as? ScreenManager

    val activity
        get() = getActivity() as? BaseActivity?

    var navigationIconClickListener: (() -> Unit)? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!ignoreState) setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (!ignoreState) {
            super.onCreateOptionsMenu(menu, inflater)
            menu.clear()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        titleString?.let { outState.putString(KEY_TITLE_STRING, it) }
        titleId?.let { outState.putInt(KEY_TITLE_RES, it) }
        navigationIcon?.let { outState.putInt(KEY_NAV_ICON, it) }

        ignoreState.let { outState.putBoolean(KEY_IGNORE_STATE, it) }
        showToolbar.let { outState.putBoolean(KEY_SHOW_TOOLBAR, it) }

        navigationBarColor?.let { outState.putInt(KEY_COLOR_NAVBAR, it) }
        statusBarColor?.let { outState.putInt(KEY_COLOR_STATUSBAR, it) }
        toolbarBarColor?.let { outState.putInt(KEY_COLOR_TOOLBAR, it) }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getString(KEY_TITLE_STRING)?.let { titleString = it }
        savedInstanceState?.getInt(KEY_TITLE_RES)?.let { if (it != 0) titleId = it }
        savedInstanceState?.getInt(KEY_NAV_ICON)?.let { if (it != 0) navigationIcon = it }

        savedInstanceState?.getBoolean(KEY_IGNORE_STATE)?.let { ignoreState = it }
        savedInstanceState?.getBoolean(KEY_SHOW_TOOLBAR)?.let { showToolbar = it }

        savedInstanceState?.getInt(KEY_COLOR_NAVBAR)?.let { if (it != 0) navigationBarColor = it }
        savedInstanceState?.getInt(KEY_COLOR_STATUSBAR)?.let { if (it != 0) statusBarColor = it }
        savedInstanceState?.getInt(KEY_COLOR_TOOLBAR)?.let { if (it != 0) toolbarBarColor = it }
    }

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

    open fun startExternalActivity(
        intent: Intent?,
        requestCode: Int = -1,
        options: Bundle? = null
    ) {
        activity?.showPinOnStart = true
        super.startActivityForResult(intent, requestCode, options)
    }

    open fun showProgress(show: Boolean) {
        screenManager?.showProgress(show)
    }

    open fun showDialog(dialog: BaseDialog.Builder) {
        screenManager?.showDialog(dialog)
    }

    open fun onBack() {
        screenManager?.back()
    }

    open fun showStartScreen() {
        screenManager?.showStartScreen()
    }

    fun dpToPx(value: Float): Float? {
        return context?.let { BaseUtils.dpToPx(it, value) }
    }

    fun pxToDp(value: Float): Float? {
        return context?.let { BaseUtils.pxToDp(it, value) }
    }

    companion object {
        private const val KEY_NAV_ICON = "KEY_NAV_ICON"
        private const val KEY_TITLE_RES = "KEY_TITLE_RES"
        private const val KEY_TITLE_STRING = "KEY_TITLE_STRING"

        private const val KEY_SHOW_TOOLBAR = "KEY_SHOW_TOOLBAR"
        private const val KEY_IGNORE_STATE = "KEY_IGNORE_STATE"

        private const val KEY_COLOR_TOOLBAR = "KEY_COLOR_TOOLBAR"
        private const val KEY_COLOR_NAVBAR = "KEY_COLOR_NAVBAR"
        private const val KEY_COLOR_STATUSBAR = "KEY_COLOR_STATUSBAR"
    }
}