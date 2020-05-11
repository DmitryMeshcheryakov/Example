package com.android.example.core.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.fragment.app.FragmentActivity
import com.android.example.R
import com.android.example.core.utils.loadImage
import com.google.android.material.bottomsheet.BottomSheetDialog


abstract class BaseDialog {

    private var isPaused: Boolean = false
    protected var dialog: Dialog? = null

    protected open var layoutId: Int = R.layout.dlg_simple
    protected open var themeId: Int? = null

    protected open var titleResId: Int? = null
    protected open var titleString: CharSequence? = null

    protected open var descriptionResId: Int? = null
    protected open var descriptionString: CharSequence? = null

    protected open var positiveButtonResId: Int? = null
    protected open var positiveButtonString: String? = null

    protected open var negativeButtonResId: Int? = null
    protected open var negativeButtonString: String? = null

    protected open var neutralButtonResId: Int? = null
    protected open var neutralButtonString: String? = null

    protected open var showNegativeBtn: Boolean? = null
    protected open var showNeutralBtn: Boolean? = null
    protected open var showPositiveBtn: Boolean? = null

    protected open var image: Any? = null

    protected open var isCancelable: Boolean = true

    protected open var onDismissListener: (() -> Unit)? = null
    protected open var onNegativeClickListener: (() -> Unit)? = null
    protected open var onNeutralClickListener: (() -> Unit)? = null

    protected open var customViewParams: ((view: View?, dialog: Dialog?) -> Unit)? = null

    protected abstract fun createDialog(context: Context, view: View): Dialog

    protected open fun createView(context: Context): View? {
        val view = LayoutInflater.from(context).inflate(layoutId, null)
        bindTitle(view)
        bindDescription(view)
        bindImage(view)
        bindNegativeButton(view)
        bindNeutralButton(view)
        return view
    }

    protected open fun bindTitle(view: View?) {
        runCatching {
            val titleTv = view?.findViewById<TextView?>(R.id.titleTv)
            when {
                (titleResId != null) -> titleTv?.setText(titleResId!!)
                (titleString != null) -> titleTv?.text = titleString
                else -> titleTv?.visibility = View.GONE
            }
        }
    }

    protected open fun bindDescription(view: View?) {
        runCatching {
            val descriptionTv = view?.findViewById<TextView?>(R.id.descriptionTv)
            when {
                (descriptionResId != null) -> descriptionTv?.setText(descriptionResId!!)
                (descriptionString != null) -> descriptionTv?.text = descriptionString
                else -> descriptionTv?.visibility = View.GONE
            }
        }
    }

    protected open fun bindImage(view: View?) {
        runCatching {
            val imageIv = view?.findViewById<ImageView?>(R.id.imageIv)
            when {
                (image != null) -> {
                    imageIv?.visibility = View.VISIBLE
                    imageIv?.loadImage(image)
                }
                else -> imageIv?.visibility = View.GONE
            }
        }
    }

    protected open fun bindDismiss(dialog: Dialog?) {
        dialog?.setOnDismissListener { onDismissListener?.invoke() }
    }

    protected open fun bindPositiveButton(view: View?, listener: (() -> Unit)?) {
        runCatching {
            val positiveBtn = view?.findViewById<TextView?>(R.id.positiveBtn)
            when {
                (positiveButtonResId != null && showPositiveBtn != false) -> {
                    positiveBtn?.setText(positiveButtonResId!!)
                    positiveBtn?.setOnClickListener {
                        listener?.invoke()
                        dialog?.dismiss()
                    }
                }
                (positiveButtonString != null && showPositiveBtn != false) -> {
                    positiveBtn?.text = positiveButtonString
                    positiveBtn?.setOnClickListener {
                        listener?.invoke()
                        dialog?.dismiss()
                    }
                }
                showPositiveBtn != false -> {
                    positiveBtn?.setText(R.string.ok)
                    positiveBtn?.setOnClickListener {
                        listener?.invoke()
                        dialog?.dismiss()
                    }
                }
                else -> {
                    positiveBtn?.visibility = View.GONE
                }
            }
        }
    }

    protected open fun bindNegativeButton(view: View?) {
        runCatching {
            val negativeBtn = view?.findViewById<TextView?>(R.id.negativeBtn)
            when {
                (negativeButtonResId != null && showNegativeBtn != false) -> {
                    negativeBtn?.setText(negativeButtonResId!!)
                    negativeBtn?.setOnClickListener {
                        onNegativeClickListener?.invoke()
                        dialog?.dismiss()
                    }
                    negativeBtn?.visibility = View.VISIBLE
                }
                (negativeButtonString != null && showNegativeBtn != false) -> {
                    negativeBtn?.text = negativeButtonString!!
                    negativeBtn?.setOnClickListener {
                        onNegativeClickListener?.invoke()
                        dialog?.dismiss()
                    }
                    negativeBtn?.visibility = View.VISIBLE
                }
                showNegativeBtn == true -> {
                    negativeBtn?.setText(R.string.no)
                    negativeBtn?.setOnClickListener {
                        onNegativeClickListener?.invoke()
                        dialog?.dismiss()
                    }
                    negativeBtn?.visibility = View.VISIBLE
                }
                else -> {
                    negativeBtn?.visibility = View.GONE
                }
            }
        }
    }

    protected open fun bindNeutralButton(view: View?) {
        runCatching {
            val neutralBtn = view?.findViewById<TextView?>(R.id.neutralBtn)
            when {
                (neutralButtonResId != null && showNeutralBtn != false) -> {
                    neutralBtn?.setText(neutralButtonResId!!)
                    neutralBtn?.setOnClickListener {
                        onNeutralClickListener?.invoke()
                        dialog?.dismiss()
                    }
                    neutralBtn?.visibility = View.VISIBLE
                }
                (neutralButtonString != null && showNeutralBtn != false) -> {
                    neutralBtn?.text = neutralButtonString!!
                    neutralBtn?.setOnClickListener {
                        onNeutralClickListener?.invoke()
                        dialog?.dismiss()
                    }
                    neutralBtn?.visibility = View.VISIBLE
                }
                showNeutralBtn == true -> {
                    neutralBtn?.setText(R.string.cancel)
                    neutralBtn?.setOnClickListener {
                        onNeutralClickListener?.invoke()
                        dialog?.dismiss()
                    }
                    neutralBtn?.visibility = View.VISIBLE
                }
                else -> {
                    neutralBtn?.visibility = View.GONE
                }
            }
        }
    }

    open inner class Builder {

        open fun setTheme(@StyleRes resId: Int?): Builder {
            themeId = resId
            return this
        }

        open fun setImage(img: Any?): Builder {
            image = img
            return this
        }

        open fun showPositiveButton(show: Boolean): Builder {
            showPositiveBtn = show
            return this
        }

        open fun setNeutralClickListener(listener: () -> Unit): Builder {
            onNeutralClickListener = listener
            return this
        }

        open fun setNegativeClickListener(listener: () -> Unit): Builder {
            onNegativeClickListener = listener
            return this
        }

        open fun showNegativeButton(show: Boolean): Builder {
            showNegativeBtn = show
            return this
        }

        open fun showNeutralButton(show: Boolean): Builder {
            showNeutralBtn = show
            return this
        }

        open fun setOnDismissListener(listener: () -> Unit): Builder {
            onDismissListener = listener
            return this
        }

        open fun setTitle(@StringRes resId: Int?): Builder {
            titleResId = resId
            return this
        }

        open fun setTitle(text: CharSequence?): Builder {
            titleString = text
            return this
        }

        open fun setDescription(@StringRes resId: Int?): Builder {
            descriptionResId = resId
            return this
        }

        open fun setDescription(text: CharSequence?): Builder {
            descriptionString = text
            return this
        }

        open fun setPositiveButton(@StringRes resId: Int?): Builder {
            positiveButtonResId = resId
            return this
        }

        open fun setPositiveButton(text: String?): Builder {
            positiveButtonString = text
            return this
        }

        open fun setNegativeButton(@StringRes resId: Int?): Builder {
            negativeButtonResId = resId
            return this
        }

        open fun setNegativeButton(text: String?): Builder {
            negativeButtonString = text
            return this
        }

        open fun setNeutralButton(@StringRes resId: Int?): Builder {
            neutralButtonResId = resId
            return this
        }

        open fun setNeutralButton(text: String?): Builder {
            neutralButtonString = text
            return this
        }

        open fun setCancelable(cancelable: Boolean?): Builder {
            isCancelable = cancelable ?: true
            return this
        }

        open fun addCustomViewParams(listener: ((view: View?, dialog: Dialog?) -> Unit)?): Builder {
            customViewParams = listener
            return this
        }

        /**
         * Layout requirements:
         * dialog title (TextView) = @id/titleTv,
         * dialog description (TextView) = @id/descriptionTv
         *
         * Buttons:
         * dialog positive button (view: TextView) = @id/positiveBtn,
         * dialog neutral button (view: TextView) = @id/neutralBtn,
         * dialog negative button (view: TextView) = @id/negativeBtn,
         */
        open fun setLayoutId(@LayoutRes resId: Int): Builder {
            layoutId = resId
            return this
        }

        open fun show(context: FragmentActivity?) {
            isPaused = false
            context?.let {
                createView(context)?.let { view ->
                    dialog = createDialog(it, view)
                    dialog?.setCancelable(isCancelable)
                    bindDismiss(dialog)
                    customViewParams?.invoke(view, dialog)

                    dialog?.show()
                    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    context.window?.decorView?.height?.let { (dialog as? BottomSheetDialog?)?.behavior?.setPeekHeight(it) }
                }
            }
        }

        open fun dismiss(context: FragmentActivity?) {
            isPaused = false
            val isShowing = dialog?.isShowing == true
            if (isShowing) dialog?.dismiss()
        }

        fun resume(context: FragmentActivity?) {
            if (isPaused) show(context)
            isPaused = false
        }

        fun pause(context: FragmentActivity?) {
            val isShowing = dialog?.isShowing == true
            isPaused = isShowing
            if (isShowing) dialog?.dismiss()
        }
    }
}