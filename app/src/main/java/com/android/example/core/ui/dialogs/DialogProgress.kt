package com.android.example.core.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.android.example.R
import com.android.example.core.ui.dialogs.BaseDialog

class DialogProgress : BaseDialog() {

    companion object {
        const val TAG = "Dialog"

        fun builder(): Builder {
            return DialogProgress().Builder()
        }
    }

    override var layoutId: Int = R.layout.dlg_progress

    override fun createDialog(context: Context, view: View): Dialog {
        val dialogBuilder =
            themeId?.let { AlertDialog.Builder(context, it) } ?: AlertDialog.Builder(context)
        dialogBuilder.setView(view)
        return dialogBuilder.create()
    }

    override fun createView(context: Context): View? {
        return LayoutInflater.from(context).inflate(layoutId, null)
    }
}