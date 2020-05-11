package com.android.example.core.ui.activities

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.example.core.ui.dialogs.BaseDialog
import com.android.example.core.ui.screenmanagers.BaseScreenManager
import com.android.example.core.utils.BaseUtils
import com.android.example.core.utils.DayNightUtils
import com.android.example.core.utils.LocaleUtils

abstract class BaseActivity : AppCompatActivity {

    constructor() : super()

    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    open var showPinOnStart = false

    var screenManager: BaseScreenManager? = null
        get() {
            if (field == null) initScreenManager()
            return field
        }

    abstract fun getToolbar(): Toolbar?

    abstract fun getFragmentLayoutId(): Int?

    abstract fun initScreenManager(): BaseScreenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenManager = initScreenManager()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleUtils.attachBaseContext(base) ?: base)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        DayNightUtils.onConfigurationChanged(newConfig)
        showPinOnStart = false
        recreate()
    }

    override fun onBackPressed() {
        onBack()
    }

    override fun onResume() {
        super.onResume()
        screenManager?.onResume()
    }

    override fun onPause() {
        super.onPause()
        screenManager?.onPause()
    }

    open fun showProgress(show: Boolean) {
        screenManager?.showProgress(show)
    }

    open fun showDialog(dialog: BaseDialog.Builder) {
        screenManager?.showDialog(dialog)
    }

    open fun onBack() {
        screenManager?.backFromActivity()
    }

    open fun showStartScreen() {
        screenManager?.showStartScreen()
    }

    fun dpToPx(value: Float): Float {
        return BaseUtils.dpToPx(this, value)
    }

    fun pxToDp(value: Float): Float {
        return BaseUtils.pxToDp(this, value)
    }
}