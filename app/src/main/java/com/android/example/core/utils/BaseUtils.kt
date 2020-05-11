package com.android.example.core.utils

import android.content.Context
import android.content.pm.PackageManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import java.util.*

object BaseUtils {

    fun showBottomViewOnKeyboardShow(activityRootLay: View?, listener: (show: Boolean) -> Unit) {
        activityRootLay?.viewTreeObserver?.addOnGlobalLayoutListener {
            val heightDiff = activityRootLay.rootView.height - activityRootLay.height
            listener.invoke(heightDiff < dpToPx(activityRootLay.context, 200f))
        }
    }

    fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (context == null)
            return false

        for (permission in permissions) {
            if (!hasPermission(context, permission))
                return false
        }
        return true
    }

    fun hasPermission(context: Context, permission: String): Boolean {
        val res = context.checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    fun dpToPx(context: Context, value: Float): Float {
        return value * context.resources.displayMetrics.density + 0.5f
    }

    fun pxToDp(context: Context, value: Float): Float {
        return value / context.resources.displayMetrics.density
    }
}

fun <O> Iterable<O>.search(query: CharSequence?): List<O> {
    return this.filter { it.toString().containsForSearch(query) }
}

fun String.containsForSearch(query: CharSequence?): Boolean {

    val whiteSpaces = "\\s".toRegex()
    val str = this
        .toLowerCase(Locale.getDefault())
        .replace(whiteSpaces, "")
    val q = query?.toString().orEmpty()
        .toLowerCase(Locale.getDefault())
        .replace(whiteSpaces, "")

    return str.contains(q)
}

fun RecyclerView.onScrollToBottom(listener: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1)) {
                listener.invoke()
            }
        }
    })
}

fun View.onFocus(listener: () -> Unit) {
    setOnFocusChangeListener { v, hasFocus -> if (hasFocus) listener.invoke() }
}

fun View.onFocusLost(listener: () -> Unit) {
    setOnFocusChangeListener { v, hasFocus -> if (!hasFocus) listener.invoke() }
}

fun TextView.onSubmit(listener: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        return@setOnEditorActionListener when (actionId) {
            EditorInfo.IME_ACTION_SEND,
            EditorInfo.IME_ACTION_GO,
            EditorInfo.IME_ACTION_NEXT,
            EditorInfo.IME_ACTION_DONE,
            EditorInfo.IME_ACTION_SEARCH -> {
                listener.invoke()
                true
            }
            else -> false
        }
    }
}


fun TextView.afterTextChange(listener: (s: Editable?) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            listener.invoke(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun TabLayout.onTabSelected(listener: (tab: TabLayout.Tab?) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {}

        override fun onTabUnselected(tab: TabLayout.Tab?) {}

        override fun onTabSelected(tab: TabLayout.Tab?) {
            listener.invoke(tab)
        }
    })
}

fun ImageView?.loadImage(any: Any?) {
    this?.let { Glide.with(it).load(any).into(it) }
}
