package com.android.example.features.mvp

import android.graphics.Color
import android.os.Bundle
import androidx.annotation.LayoutRes
import com.android.example.R
import com.android.example.core.ui.fragments.BaseFragment
import com.arellomobile.mvp.MvpDelegate
import com.google.android.material.snackbar.Snackbar

abstract class BaseMvpFragment : BaseFragment, BaseMvpView {

    constructor() : super()

    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    private var mIsStateSaved: Boolean = false

    private var mMvpDelegate: MvpDelegate<out BaseFragment>? = null

    override var toolbarBarColor: Int? = R.color.colorPrimary

    override var statusBarColor: Int? = R.color.colorPrimaryDark

    override var navigationBarColor: Int? = R.color.colorBackgroundVariant

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getMvpDelegate().onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        mIsStateSaved = false

        getMvpDelegate().onAttach()
    }

    override fun onResume() {
        super.onResume()

        mIsStateSaved = false

        getMvpDelegate().onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        mIsStateSaved = true

        getMvpDelegate().onSaveInstanceState(outState)
        getMvpDelegate().onDetach()
    }

    override fun onStop() {
        super.onStop()

        getMvpDelegate().onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        getMvpDelegate().onDetach()
        getMvpDelegate().onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()

        //We leave the screen and respectively all fragments will be destroyed
        if (activity?.isFinishing == true) {
            getMvpDelegate().onDestroy()
            return
        }

        // When we rotate device isRemoving() return true for fragment placed in backstack
        // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
        if (mIsStateSaved) {
            mIsStateSaved = false
            return
        }

        // See https://github.com/Arello-Mobile/Moxy/issues/24
        var anyParentIsRemoving = false
        var parent = parentFragment
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving
            parent = parent.parentFragment
        }

        if (isRemoving || anyParentIsRemoving) {
            getMvpDelegate().onDestroy()
        }
    }

    /**
     * @return The [MvpDelegate] being used by this Fragment.
     */
    fun getMvpDelegate(): MvpDelegate<*> {
        if (mMvpDelegate == null) {
            mMvpDelegate = MvpDelegate(this)
        }

        return mMvpDelegate as MvpDelegate<out BaseFragment>
    }

    override fun showMainScreen() {
        (screenManager as? ScreenManager?)?.showMainScreen()
    }

    override fun showProgress(show: Boolean) {
        activity?.showProgress(show)
    }

    override fun finishScreen() {
        screenManager?.back()
    }

    override fun onBack() {
        showProgress(false)
        super.onBack()
    }

    override fun showError(error: String) {
        view?.let {
            val snackBar = Snackbar.make(it, error, Snackbar.LENGTH_SHORT)
            snackBar.setTextColor(Color.WHITE)
            val viewSnackBar = snackBar.view
            viewSnackBar.setBackgroundColor(Color.RED)
            snackBar.show()
        }
    }
}