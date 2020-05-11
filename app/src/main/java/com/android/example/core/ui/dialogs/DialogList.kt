package com.android.example.core.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.android.example.R
import com.android.example.core.model.items.DlgItem
import com.android.example.core.ui.adapters.DlgAdapter
import com.android.example.core.ui.dialogs.BaseDialog

open class DialogList : BaseDialog() {

    companion object {
        const val TAG = "DialogList"

        fun builder(): Builder {
            return DialogList().Builder()
        }
    }

    override var layoutId: Int = R.layout.dlg_list

    protected var itemLayoutId: Int? = null
    protected var searchHintResId: Int? = null
    protected var searchHintString: String? = null
    protected var enableCompoundButton: Boolean? = null
    protected var enableDividers: Boolean = false
    protected var enableEmptySelection: Boolean = false
    protected var enableMultipleSelection = false
    protected var dismissOnItemClick: Boolean = true
    protected var enableSearchView: Boolean = false
    protected var items: List<DlgItem>? = null
    protected var onPositiveSelectionClickListener: ((selectedItems: List<DlgItem>) -> Unit)? = null
    protected var onItemClickListener: ((item: DlgItem) -> Unit)? = null

    override fun createView(context: Context): View? {
        val view = super.createView(context)
        val recyclerView = view?.findViewById<RecyclerView?>(R.id.recyclerView)
        val searchView = view?.findViewById<TextInputEditText?>(R.id.searchView)
        val ilSearchView = view?.findViewById<TextInputLayout?>(R.id.ilsearchView)

        when {
            (searchHintResId != null) -> searchView?.setHint(searchHintResId!!)
            (searchHintString != null) -> searchView?.hint = searchHintString
        }

        val list = items.orEmpty()
        if (!enableEmptySelection && list.none { it.checked })
            list.firstOrNull()?.checked = true

        when (enableCompoundButton) {
            true -> list.forEach { it.checkable = true }
            false -> list.forEach { it.checkable = false }
        }

        val adapter = DlgAdapter(context, itemLayoutId ?: R.layout.item_dlg_list_checkbox).apply {
            this.enableDividers = this@DialogList.enableDividers
            this.enableEmptySelection = this@DialogList.enableEmptySelection
            this.enableMultipleSelection = this@DialogList.enableMultipleSelection
            this.items = list
        }
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.itemAnimator?.changeDuration = 0
        recyclerView?.setHasFixedSize(true)

        searchView?.visibility = if (enableSearchView) View.VISIBLE else View.GONE
        ilSearchView?.visibility = if (enableSearchView) View.VISIBLE else View.GONE
        searchView?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                adapter.filter(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        adapter.onItemClickListener = {
            onItemClickListener?.invoke(it)
            if (dismissOnItemClick) dialog?.dismiss()
        }

        bindPositiveButton(view) { onPositiveSelectionClickListener?.invoke(adapter.items) }

        return view
    }

    override fun createDialog(context: Context, view: View): Dialog {
        val dialogBuilder = themeId?.let { AlertDialog.Builder(context, it) } ?: AlertDialog.Builder(context)
        dialogBuilder.setView(view)
        return dialogBuilder.create()
    }

    inner class Builder : BaseDialog.Builder() {

        fun setSearchHint(@StringRes resId: Int?): Builder {
            this@DialogList.searchHintResId = resId
            return this
        }

        fun setSearchHint(text: String?): Builder {
            this@DialogList.searchHintString = text
            return this
        }

        fun enableMultipleSelection(enable: Boolean = true): Builder {
            this@DialogList.enableMultipleSelection = enable
            return this
        }

        fun dismissOnItemClick(dismiss: Boolean = true): Builder {
            this@DialogList.dismissOnItemClick = dismiss
            return this
        }

        fun enableSearch(enable: Boolean = true): Builder {
            this@DialogList.enableSearchView = enable
            return this
        }

        fun enableCompoundButton(enable: Boolean = true): Builder {
            this@DialogList.enableCompoundButton = enable
            return this
        }

        fun enableDividers(enable: Boolean = true): Builder {
            this@DialogList.enableDividers = enable
            return this
        }

        fun enableEmptySelection(enable: Boolean = true): Builder {
            this@DialogList.enableEmptySelection = enable
            return this
        }

        fun setItemClickListener(listener: (item: DlgItem) -> Unit): Builder {
            this@DialogList.onItemClickListener = listener
            return this
        }

        fun setPositiveClickListener(listener: (selectedItems: List<DlgItem>) -> Unit): Builder {
            this@DialogList.onPositiveSelectionClickListener = listener
            return this
        }

        fun setItems(items: List<DlgItem>): Builder {
            this@DialogList.items = items
            return this
        }

        override fun setTheme(resId: Int?): Builder {
            super.setTheme(resId)
            return this
        }

        override fun setImage(img: Any?): Builder {
            super.setImage(img)
            return this
        }
        override fun setNegativeClickListener(listener: () -> Unit): Builder {
            super.setNegativeClickListener(listener)
            return this
        }

        override fun setNeutralClickListener(listener: () -> Unit): Builder {
            super.setNeutralClickListener(listener)
            return this
        }

        override fun showPositiveButton(show: Boolean): Builder {
            super.showPositiveButton(show)
            return this
        }

        override fun showNegativeButton(show: Boolean): Builder {
            super.showNegativeButton(show)
            return this
        }

        override fun showNeutralButton(show: Boolean): Builder {
            super.showNeutralButton(show)
            return this
        }

        override fun setOnDismissListener(listener: () -> Unit): Builder {
            super.setOnDismissListener(listener)
            return this
        }

        override fun setTitle(@StringRes resId: Int?): Builder {
            super.setTitle(resId)
            return this
        }

        override fun setTitle(text: CharSequence?): Builder {
            super.setTitle(text)
            return this
        }

        override fun setDescription(@StringRes resId: Int?): Builder {
            super.setDescription(resId)
            return this
        }

        override fun setDescription(text: CharSequence?): Builder {
            super.setDescription(text)
            return this
        }

        override fun setPositiveButton(@StringRes resId: Int?): Builder {
            super.setPositiveButton(resId)
            return this
        }

        override fun setPositiveButton(text: String?): Builder {
            super.setPositiveButton(text)
            return this
        }

        override fun setNegativeButton(@StringRes resId: Int?): Builder {
            super.setNegativeButton(resId)
            return this
        }

        override fun setNegativeButton(text: String?): Builder {
            super.setNegativeButton(text)
            return this
        }

        override fun setNeutralButton(@StringRes resId: Int?): Builder {
            super.setNeutralButton(resId)
            return this
        }

        override fun setNeutralButton(text: String?): Builder {
            super.setNeutralButton(text)
            return this
        }

        override fun setCancelable(cancelable: Boolean?): Builder {
            super.setCancelable(cancelable)
            return this
        }

        override fun addCustomViewParams(listener: ((view: View?, dialog: Dialog?) -> Unit)?): Builder {
            super.addCustomViewParams(listener)
            return this
        }

        /**
         * Layout requirements:
         * dialog image (ImageView) = @id/imageIv
         * search field (EditText) = @id/searchView
         */
        override fun setLayoutId(@LayoutRes resId: Int): Builder {
            super.setLayoutId(resId)
            return this
        }

        /**
         * Item layout requirements:
         * image (ImageView) = @id/imageIv,
         * title (TextView) = @id/titleTv,
         * description (TextView) = @id/descriptionTv
         * if switch enabled (Switch) = @id/toggle
         * if checkbox enabled (CheckBox) = @id/checkbox
         * if radioButton enabled (RadioButton) = @id/radio
         */
        fun setItemLayoutId(@LayoutRes resId: Int): Builder {
            this@DialogList.itemLayoutId = resId
            return this
        }
    }
}