package com.android.example.core.ui.screenmanagers

import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import com.android.example.R
import com.android.example.core.ui.activities.BaseActivity
import com.android.example.core.ui.dialogs.BaseDialog
import com.android.example.core.ui.fragments.BaseFragment
import com.android.example.core.utils.WindowUtils
import java.lang.ref.WeakReference

open class BaseScreenManager(activity: AppCompatActivity) {

    private var checkpoints: List<Int> = emptyList()

    protected var activity: WeakReference<AppCompatActivity> = WeakReference(activity)

    protected val fragmentManager: FragmentManager?
        get() = activity.get()?.supportFragmentManager

    protected open fun getToolbar(): Toolbar? {
        val bar = (activity.get() as? BaseActivity?)?.getToolbar()
        activity.get()?.let {
            if (it.supportActionBar == null && bar != null)
                it.setSupportActionBar(bar)
        }
        return bar
    }

    @DrawableRes
    var rootToolbarIconResId: Int? = null // toolbar icon for first fragment
    var rootToolbarIconClickListener: (() -> Unit)? =
        null // toolbar icon click listener for first fragment
    var onActivityFinishListener: (() -> Unit)? =
        null //If not null invoked instead of activity finish


    @AnimatorRes
    @AnimRes
    var customFragmentTransitionOpen: Int? = null
    @AnimatorRes
    @AnimRes
    var customFragmentTransitionClose: Int? = null

    protected open var dialog: BaseDialog.Builder? = null
    protected open var progressDialog: BaseDialog.Builder? = null

    protected open val defaultFragmentTransition: Int = TRANSIT_FRAGMENT_OPEN
    protected open val needShowBackButton: Boolean = true

    init {
        fragmentManager?.addOnBackStackChangedListener {
            WindowUtils.hideSoftKeyboard(activity)
            getTopFragment()?.let { topFragment ->
                restoreToolbarState(topFragment)
            }
        }
    }

    fun addFragment(
        fragment: Fragment,
        addToBackStack: Boolean = true,
        animationOpen: Int? = customFragmentTransitionOpen,
        animationClose: Int? = customFragmentTransitionClose
    ) {
        (activity.get() as? BaseActivity)?.getFragmentLayoutId()?.let { layoutId ->
            val count = getFragmentCount().toString()
            val transaction = fragmentManager?.beginTransaction()

            if (animationOpen != null && animationClose != null)
                transaction?.setCustomAnimations(
                    animationOpen,
                    animationClose,
                    animationOpen,
                    animationClose
                )
            else
                transaction?.setTransition(defaultFragmentTransition)

            when {
                addToBackStack -> {
                    transaction?.add(layoutId, fragment, count)
                    transaction?.addToBackStack(count)
                }
                !addToBackStack -> {
                    transaction?.add(layoutId, fragment)
                }
            }
            transaction?.commitAllowingStateLoss()
        }
        if (fragment is BaseFragment) restoreToolbarState(fragment)
    }

    fun replaceFragment(
        fragment: Fragment,
        addToBackStack: Boolean = true,
        clearBackStack: Boolean = false,
        animationOpen: Int? = customFragmentTransitionOpen,
        animationClose: Int? = customFragmentTransitionClose
    ) {
        (activity.get() as? BaseActivity)?.getFragmentLayoutId()?.let { layoutId ->
            if (clearBackStack) clearBackStack()

            val count = getFragmentCount().toString()
            val transaction = fragmentManager?.beginTransaction()

            if (animationOpen != null && animationClose != null)
                transaction?.setCustomAnimations(
                    animationOpen,
                    animationClose,
                    animationOpen,
                    animationClose
                )
            else
                transaction?.setTransition(defaultFragmentTransition)

            when {
                addToBackStack -> {
                    transaction?.replace(layoutId, fragment, count)
                    transaction?.addToBackStack(count)
                }
                !addToBackStack -> {
                    transaction?.replace(layoutId, fragment)
                }
            }
            transaction?.commitAllowingStateLoss()
        }
        if (fragment is BaseFragment) restoreToolbarState(fragment)
    }

    open fun replaceRoot(
        fragment: Fragment,
        animationOpen: Int? = customFragmentTransitionOpen,
        animationClose: Int? = customFragmentTransitionClose
    ) {
        runCatching {
            if (getFragmentAt(0)?.javaClass?.name == fragment.javaClass.name) {
                checkpoints = emptyList()
                fragmentManager?.popBackStackImmediate("0", 0)
            } else {
                replaceFragment(
                    fragment,
                    true,
                    true,
                    animationOpen,
                    animationClose
                )
            }
        }
    }

    open fun replaceRootFirst(
        fragment: Fragment,
        animationOpen: Int? = customFragmentTransitionOpen,
        animationClose: Int? = customFragmentTransitionClose
    ) {
        if (getFragmentCount() == 0) {
            replaceFragment(
                fragment,
                true,
                true,
                animationOpen,
                animationClose
            )
        }
    }


    open fun showProgress(show: Boolean) {
        Handler().post {
            if (show) progressDialog?.show(activity.get())
            else progressDialog?.dismiss(activity.get())
        }
    }

    fun showDialog(dialogBuilder: BaseDialog.Builder?) {
        Handler().post {
            showProgress(false)
            dialog?.dismiss(activity.get())
            dialogBuilder?.show(activity.get())
            dialog = dialogBuilder
        }
    }

    fun setCheckpoint() {
        val currentFragmentId = getFragmentCount() - 1
        if (!checkpoints.contains(currentFragmentId))
            checkpoints = checkpoints.plus(currentFragmentId)
    }

    fun <T> backToFragment(fragment: Class<T>) {
        WindowUtils.hideSoftKeyboard(activity.get())

        var tag = -1
        fragmentManager?.fragments?.indices?.forEach {
            if (fragmentManager?.fragments.orEmpty()[it].javaClass == fragment)
                tag = getFragmentCount() - it
        }

        if (tag != -1) {
            checkpoints = checkpoints.filter { it < tag }
            fragmentManager?.popBackStackImmediate(tag.toString(), 0)
        } else
            back()
    }

    fun backToLastCheckpoint() {
        WindowUtils.hideSoftKeyboard(activity.get())
        if (checkpoints.isEmpty() || getFragmentCount() == 1)
            back()
        else {
            val to = checkpoints.last()
            fragmentManager?.popBackStackImmediate(to.toString(), 0)
            checkpoints = checkpoints.minus(to)
        }
    }

    fun backFromActivity() {
        val top = getTopFragment() as? BaseFragment?
        if (top != null) top.onBack()
        else back()
    }

    fun back() {
        WindowUtils.hideSoftKeyboard(activity.get())
        if (getFragmentCount() <= 1) {
            if (onActivityFinishListener != null) onActivityFinishListener?.invoke()
            else finishActivity()
        } else fragmentManager?.popBackStackImmediate()
    }

    open fun finishActivity() {
        activity.get()?.finish()
    }

    open fun restoreToolbarState(fragment: Fragment? = null) {
        val topFragment = getTopFragment()
        val fr = (fragment as? BaseFragment?) ?: (topFragment as? BaseFragment?)
        val toolbar = getToolbar()
        fr?.let {
            if (!it.ignoreState) {
                toolbar?.visibility = if (!it.showToolbar) View.GONE else View.VISIBLE
                setToolbarTitle(toolbar, it)
                setToolbarIcon(toolbar)
                setToolbarBarColor(toolbar, it.toolbarBarColor)
                setStatusBarColor(it.statusBarColor)
                setNavBarColor(it.navigationBarColor)
            }
        }
    }

    open fun onResume() {
        progressDialog?.resume(activity.get())
        dialog?.resume(activity.get())
    }

    open fun onPause() {
        progressDialog?.pause(activity.get())
        dialog?.pause(activity.get())
    }

    fun getFragmentCount(): Int {
        return fragmentManager?.backStackEntryCount ?: 0
    }

    fun getTopFragment(): Fragment? {
        return getFragmentAt(getFragmentCount() - 1)
    }

    private fun getFragmentAt(index: Int): Fragment? {
        return if (getFragmentCount() > 0)
            fragmentManager?.findFragmentByTag(index.toString())
        else null
    }

    private fun clearBackStack() {
        runCatching {
            checkpoints = emptyList()
            fragmentManager?.popBackStackImmediate("0", 0)
            fragmentManager?.popBackStackImmediate()
        }
    }

    protected open fun setToolbarTitle(toolbar: Toolbar?, fragment: BaseFragment?) {
        fragment?.titleId?.let { toolbar?.setTitle(it) }
        fragment?.titleString?.let { toolbar?.setTitle(it) }
    }

    protected open fun setToolbarIcon(toolbar: Toolbar?) {
        val fr = getTopFragment() as? BaseFragment?
        when {
            getFragmentCount() <= 1 && rootToolbarIconResId != null -> {
                rootToolbarIconResId?.let { icon ->
                    toolbar?.navigationIcon = activity.get()?.getDrawable(icon)
                    toolbar?.setNavigationOnClickListener { rootToolbarIconClickListener?.invoke() }
                }
            }
            getFragmentCount() <= 1 && rootToolbarIconResId == null -> {
                toolbar?.navigationIcon = null
                toolbar?.setNavigationOnClickListener(null)
            }
            fr?.navigationIcon != null -> {
                toolbar?.navigationIcon = activity.get()?.getDrawable(fr.navigationIcon!!)
                toolbar?.setNavigationOnClickListener { fr.navigationIconClickListener?.invoke() }
            }
            needShowBackButton -> {
                toolbar?.navigationIcon = activity.get()?.getDrawable(R.drawable.ic_action_back)
                toolbar?.setNavigationOnClickListener { backFromActivity() }
            }
            else -> {
                toolbar?.navigationIcon = null
                toolbar?.setNavigationOnClickListener(null)
            }
        }
    }

    open fun setToolbarBarColor(toolbar: Toolbar?, @ColorRes color: Int?) {
        activity.get()?.let { context ->
            color?.let { colorRes ->
                toolbar?.background = ColorDrawable(ContextCompat.getColor(context, colorRes))
            }
        }
    }

    open fun setStatusBarColor(@ColorRes color: Int?) {
        activity.get()?.let { context ->
            color?.let { colorRes ->
                WindowUtils.setStatusBarColor(
                    activity.get(),
                    ContextCompat.getColor(context, colorRes)
                )
            }
        }
    }

    open fun setNavBarColor(@ColorRes color: Int?) {
        activity.get()?.let { context ->
            color?.let { colorRes ->
                WindowUtils.setNavigationBarColor(
                    activity.get(),
                    ContextCompat.getColor(context, colorRes)
                )
            }
        }
    }

    open fun hidePrevFragment(topFragment: Fragment) {
        fragmentManager?.beginTransaction()?.show(topFragment)?.commitAllowingStateLoss()
        Handler().postDelayed({
            if (getFragmentCount() > 1)
                getFragmentAt(getFragmentCount() - 2)
                    ?.let { previousFragment ->
                        fragmentManager?.beginTransaction()
                            ?.hide(previousFragment)
                            ?.commitAllowingStateLoss()
                    }
        }, 300)
    }

    open fun showStartScreen() {}
}
