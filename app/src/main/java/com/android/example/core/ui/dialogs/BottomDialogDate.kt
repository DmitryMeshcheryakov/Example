package com.android.example.core.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.view.View
import com.android.example.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomDialogDate: DialogDate() {

    companion object {
        const val TAG = "BottomDialogDate"
        fun builder(): Builder {
            return BottomDialogDate().Builder()
        }
    }

    override var layoutId: Int = R.layout.dlg_bottom_date

    override fun createDialog(context: Context, view: View): Dialog {
        val builder = themeId?.let { BottomSheetDialog(context, it) } ?: BottomSheetDialog(context)
        builder.setContentView(view)
        builder.create()
        return builder
    }
}