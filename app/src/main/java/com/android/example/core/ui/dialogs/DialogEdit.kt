package com.android.example.core.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.android.example.R
import com.android.example.core.ui.dialogs.BaseDialog

open class DialogEdit: BaseDialog() {

    companion object {
        const val TAG = "DialogEdit"

        const val INPUT_TYPE_PASSWORD = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        const val INPUT_TYPE_NUMERIC = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL or InputType.TYPE_NUMBER_FLAG_DECIMAL
        const val INPUT_TYPE_NUMERIC_SIGNED = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL or InputType.TYPE_NUMBER_FLAG_SIGNED

        fun builder(): Builder {
            return DialogEdit().Builder()
        }
    }


    override var layoutId: Int = R.layout.dlg_edit

    protected var hintString: String? = null
    protected var valueString: String? = null
    protected var hintResId: Int? = null
    protected var valueResId: Int? = null
    protected var inputType: Int? = null
    protected var maxLength: Int? = 300
    protected var addBtnImageResId: Int? = null
    protected var addBtnImageBitmap: Bitmap? = null
    protected var onAdditionalBtnClick: (() -> Unit)? = null
    protected var onPositiveClickListener: ((text: Editable?) -> Unit)? = null

    override fun createView(context: Context): View? {
        val view = super.createView(context)
        val editText = view?.findViewById<EditText?>(R.id.editText)
        val additionalBtn = view?.findViewById<ImageButton?>(R.id.additionalBtn)


        bindPositiveButton(view) { onPositiveClickListener?.invoke(editText?.text) }

        when {
            (valueResId != null) -> editText?.setText(valueResId!!)
            (valueString != null) -> editText?.setText(valueString)
        }
        when {
            (hintResId != null) -> editText?.setHint(hintResId!!)
            (hintString != null) -> editText?.hint = hintString
        }
        when {
            (addBtnImageResId != null) -> additionalBtn?.setImageResource(addBtnImageResId!!)
            (addBtnImageBitmap != null) -> additionalBtn?.setImageBitmap(addBtnImageBitmap)
            else -> additionalBtn?.visibility = View.GONE
        }

        additionalBtn?.setOnClickListener { onAdditionalBtnClick?.invoke() }
        inputType?.let { editText?.inputType = it }
        maxLength?.let { editText?.filters = arrayOf(InputFilter.LengthFilter(it)) }

        if (inputType == INPUT_TYPE_PASSWORD && addBtnImageResId == null && addBtnImageBitmap == null) {
            additionalBtn?.visibility = View.VISIBLE
            additionalBtn?.setImageResource(R.drawable.ic_visibility)
            additionalBtn?.setOnClickListener {
                if (editText?.transformationMethod is PasswordTransformationMethod) {
                    additionalBtn.setImageResource(R.drawable.ic_visibility_off)
                    editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    additionalBtn.setImageResource(R.drawable.ic_visibility)
                    editText?.transformationMethod = PasswordTransformationMethod.getInstance()
                }
            }
        }
        return view
    }

    override fun createDialog(context: Context, view: View): Dialog {
        val dialogBuilder = themeId?.let { AlertDialog.Builder(context, it) } ?: AlertDialog.Builder(context)
        dialogBuilder.setView(view)
        return dialogBuilder.create()
    }

    inner class Builder : BaseDialog.Builder() {

        fun setAdditionalBtnImage(@DrawableRes resId: Int?): Builder {
            this@DialogEdit.addBtnImageResId = resId
            return this
        }

        fun setAdditionalBtnImage(image: Bitmap?): Builder {
            this@DialogEdit.addBtnImageBitmap = image
            return this
        }

        fun setPositiveClickListener(listener: (text: Editable?) -> Unit): Builder {
            this@DialogEdit.onPositiveClickListener = listener
            return this
        }

        fun setOnAdditionalBtnClickListener(listener: () -> Unit): Builder {
            this@DialogEdit.onAdditionalBtnClick = listener
            return this
        }

        fun setInputType(inputType: Int?): Builder {
            this@DialogEdit.inputType = inputType
            return this
        }

        fun setMaxLength(maxLength: Int?): Builder {
            this@DialogEdit.maxLength = maxLength
            return this
        }

        fun setValue(text: String?): Builder {
            this@DialogEdit.valueString = text
            return this
        }

        fun setValue(@StringRes resId: Int?): Builder {
            this@DialogEdit.valueResId = resId
            return this
        }

        fun setHint(text: String?): Builder {
            this@DialogEdit.hintString = text
            return this
        }

        fun setHint(@StringRes resId: Int?): Builder {
            this@DialogEdit.hintResId = resId
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
         * For this dialog layout must have EditText with @id/editText
         * other layout requirements:
         * dialog image (ImageView) = @id/imageIv
         * additional button (ImageButton) = @id/additionalBtn
         */
        override fun setLayoutId(@LayoutRes resId: Int): Builder {
            super.setLayoutId(resId)
            return this
        }
    }
}