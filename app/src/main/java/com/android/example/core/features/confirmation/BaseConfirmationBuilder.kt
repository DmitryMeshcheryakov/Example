package com.android.example.core.features.confirmation

import com.android.example.core.features.confirmation.model.ConfirmationType

open class BaseConfirmationBuilder(var selectionId: String) {

    var url: String? = null
    var title: String? = null
    var listener: ((confirmationData: String?) -> Unit)? = null
    var dismissListener: (() -> Unit)? = null
    var defaultNoConfirmation: ConfirmationType = ConfirmationType.NO_CONFIRM

    open fun setUrl(url: String?): BaseConfirmationBuilder {
        this.url = url
        return this
    }

    open fun setTitle(title: String?): BaseConfirmationBuilder {
        this.title = title
        return this
    }

    open fun setDefaultNoConfirmation(confirmationType: ConfirmationType): BaseConfirmationBuilder {
        this.defaultNoConfirmation = confirmationType
        return this
    }

    open fun setListener(listener: (confirmationData: String?) -> Unit): BaseConfirmationBuilder {
        this.listener = listener
        return this
    }

    open fun onDismiss(listener: () -> Unit): BaseConfirmationBuilder {
        this.dismissListener = listener
        return this
    }

    open fun dissmiss() {
        dismissListener?.invoke()
        dismissListener = null
        listener = null
    }

    open fun invoke(confirmationData: String?) {
        listener?.invoke(confirmationData)
        listener = null
        dismissListener = null
    }
}