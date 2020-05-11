package com.android.example.core.ui.dialogs

import android.content.Context
import android.view.View
import com.android.example.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.android.example.core.ui.dialogs.DialogDateRange

class BottomDialogDateRange : DialogDateRange() {

    companion object {
        const val TAG = "dialog"
        fun builder(): Builder {
            return BottomDialogDateRange().Builder()
        }
    }

    override var layoutId: Int = R.layout.dlg_bottom_date_range

    override fun createDialog(context: Context, view: View): android.app.Dialog {
        val builder = themeId?.let { BottomSheetDialog(context, it) } ?: BottomSheetDialog(context)
        builder.setContentView(view)
        builder.create()
        return builder
    }
}