package com.android.example.core.modules.resources

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.widget.Toast
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.android.example.R
import com.android.example.core.utils.BaseUtils
import com.android.example.core.utils.LocaleUtils

class ResourcesHelper(private val context: Context) {

    private val ctx: Context
        get() = LocaleUtils.attachBaseContext(context, false) ?: context

    fun getString(@StringRes resId: Int): String = ctx.getString(resId)

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String =
        ctx.getString(resId, *formatArgs)

    fun getQuantityString(@PluralsRes resId: Int, count: Int): String =
        ctx.resources.getQuantityString(resId, count, count)

    fun getStringArray(@ArrayRes resId: Int): Array<String> =
        ctx.resources.getStringArray(resId)

    fun getColor(@ColorRes resId: Int): Int = ContextCompat.getColor(context, resId)

    fun getDimen(@DimenRes resId: Int) = ctx.resources.getDimensionPixelOffset(resId)

    fun copyToClipboard(value: String, toast: Boolean = true) {
        val clipboardService = context.getSystemService(Context.CLIPBOARD_SERVICE)
        val clipboardManager = clipboardService as ClipboardManager
        val clipData = ClipData.newPlainText("", value)
        clipboardManager.setPrimaryClip(clipData)
        if (toast)
            Toast.makeText(ctx, R.string.clipboard, Toast.LENGTH_SHORT).show()
    }

    fun clearClipboard() {
        runCatching {
            val clipService =
                ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("", "")
            clipService.setPrimaryClip(clipData)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                clipService.clearPrimaryClip()
            }
        }
    }

    fun invertColor(@ColorInt color: Int): Int {
        val r = color shr 16 and 0xFF
        val g = color shr 8 and 0xFF
        val b = color shr 0 and 0xFF

        return Color.rgb(255 - r, 255 - g, 255 - b)
    }

    fun getHexColor(@ColorInt color: Int): String {
        return String.format("#%06X", 0xFFFFFF and color)
    }

    fun dpToPx(value: Float): Float {
        return BaseUtils.dpToPx(ctx, value)
    }

    fun pxToDp(value: Float): Float {
        return BaseUtils.pxToDp(ctx, value)
    }

}