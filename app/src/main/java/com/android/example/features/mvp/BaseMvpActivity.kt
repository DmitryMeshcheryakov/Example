package com.android.example.features.mvp

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.android.example.core.ui.activities.BaseActivity
import com.android.example.core.ui.screenmanagers.BaseScreenManager
import com.arellomobile.mvp.MvpDelegate

abstract class BaseMvpActivity : BaseActivity, BaseMvpView {

    constructor() : super()

    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    private var mMvpDelegate: MvpDelegate<out BaseActivity>? = null

    override fun initScreenManager(): BaseScreenManager = ScreenManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getMvpDelegate()?.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        getMvpDelegate()?.onAttach()
    }

    override fun onResume() {
        super.onResume()

        getMvpDelegate()?.onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        getMvpDelegate()?.onSaveInstanceState(outState)
        getMvpDelegate()?.onDetach()
    }

    override fun onStop() {
        super.onStop()

        getMvpDelegate()?.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()

        getMvpDelegate()?.onDestroyView()

        if (isFinishing) {
            getMvpDelegate()?.onDestroy()
        }
    }

    /**
     * @return The [MvpDelegate] being used by this Activity.
     */
    fun getMvpDelegate(): MvpDelegate<*>? {
        if (mMvpDelegate == null) {
            mMvpDelegate = MvpDelegate(this)
        }
        return mMvpDelegate
    }

    override fun showMainScreen() {
        (screenManager as? ScreenManager?)?.showMainScreen()
    }

    override fun finishScreen() {
        finish()
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }
}