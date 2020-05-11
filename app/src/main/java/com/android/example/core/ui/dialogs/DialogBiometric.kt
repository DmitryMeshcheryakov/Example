package com.android.example.core.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.android.example.R
import com.android.example.core.app.BaseApplication
import com.android.example.core.modules.biometric.BiometricHelper
import com.android.example.core.modules.biometric.BiometricStatus
import javax.crypto.Cipher
import javax.inject.Inject

open class DialogBiometric : BaseDialog() {

    companion object {
        fun builder() = DialogBiometric().Builder()
    }

    @Inject
    lateinit var biometricHelper: BiometricHelper

    override var layoutId: Int = R.layout.dlg_biometric

    protected var cipher: Cipher? = null

    protected var errorImgRes: Int? = null
    protected var alertImgRes: Int? = null
    protected var successImgRes: Int? = null

    protected var titleErrorRes: Int? = null
    protected var titleErrorString: CharSequence? = null

    protected var titleAlertRes: Int? = null
    protected var titleAlertString: CharSequence? = null

    protected var titleSuccessRes: Int? = null
    protected var titleSuccessString: CharSequence? = null

    protected var listener: ((cipher: Cipher?) -> Unit)? = null
    protected var onPositiveClickListener: (() -> Unit)? = null

    override fun createDialog(context: Context, view: View): Dialog {
        val dialogBuilder = themeId?.let { AlertDialog.Builder(context, it) } ?: AlertDialog.Builder(context)
        dialogBuilder.setView(view)
        return dialogBuilder.create()
    }

    override fun createView(context: Context): View? {
        BaseApplication.component.inject(this)
        val view = super.createView(context)
        bindPositiveButton(view, onPositiveClickListener)

        val statusImageIv = view?.findViewById<ImageView?>(R.id.statusIv)
        val statusTitleTv = view?.findViewById<TextView?>(R.id.statusTv)

        cipher?.let {
            biometricHelper.scan(it) { cipher, status, description ->
                val descr: String? = if (description.isNullOrEmpty()) null else description
                when (status) {
                    BiometricStatus.ERROR -> {
                        statusTitleTv?.text = descr ?: titleErrorString ?: context.getString(
                            titleErrorRes ?: R.string.default_error_biometric
                        )
                        statusImageIv?.setImageResource(errorImgRes ?: R.drawable.ic_biometric_error)
                    }
                    BiometricStatus.ALERT -> {
                        statusTitleTv?.text =
                            descr ?: titleAlertString ?: context.getString(titleAlertRes ?: R.string.biometric_alert)
                        statusImageIv?.setImageResource(alertImgRes ?: R.drawable.ic_biometric_alert)
                    }
                    BiometricStatus.SUCCESS -> {
                        statusTitleTv?.text =
                            descr ?: titleSuccessString ?: context.getString(
                                titleSuccessRes ?: R.string.biometric_success
                            )
                        statusImageIv?.setImageResource(successImgRes ?: R.drawable.ic_biometric_success)
                        listener?.invoke(cipher)
                        dialog?.dismiss()
                    }
                }
            }
        }
        return view
    }

    override fun bindDismiss(dialog: Dialog?) {
        dialog?.setOnDismissListener {
            biometricHelper.exitScan()
            onDismissListener?.invoke()
        }
    }

    inner class Builder : BaseDialog.Builder() {

        fun setCipherListener(listener: (cipher: Cipher?) -> Unit): Builder {
            this@DialogBiometric.listener = listener
            return this
        }

        fun setCipher(cipher: Cipher): Builder {
            this@DialogBiometric.cipher = cipher
            return this
        }

        fun setDefaultAlertText(@StringRes res: Int): Builder {
            this@DialogBiometric.titleAlertRes = res
            return this
        }

        fun setDefaultAlertText(img: CharSequence): Builder {
            this@DialogBiometric.titleAlertString = img
            return this
        }

        fun setDefaultSuccessText(@StringRes res: Int): Builder {
            this@DialogBiometric.titleSuccessRes = res
            return this
        }

        fun setDefaultSuccessText(img: CharSequence): Builder {
            this@DialogBiometric.titleSuccessString = img
            return this
        }

        fun setDefaultErrorText(@StringRes res: Int): Builder {
            this@DialogBiometric.titleErrorRes = res
            return this
        }

        fun setDefaultErrorText(img: CharSequence): Builder {
            this@DialogBiometric.titleErrorString = img
            return this
        }

        fun setErrorImage(@DrawableRes res: Int): Builder {
            this@DialogBiometric.errorImgRes = res
            return this
        }

        fun setAlertImage(@DrawableRes res: Int): Builder {
            this@DialogBiometric.alertImgRes = res
            return this
        }

        fun setSuccessImage(@DrawableRes res: Int): Builder {
            this@DialogBiometric.successImgRes = res
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
         */
        override fun setLayoutId(@LayoutRes resId: Int): Builder {
            super.setLayoutId(resId)
            return this
        }
    }
}