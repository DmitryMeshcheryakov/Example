package com.android.example.core.features.launch

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat.getSystemService
import com.android.example.core.features.launch.model.LaunchItem
import com.android.example.core.repository.BaseInteractor
import javax.inject.Inject


open class BaseLaunchInteractor : BaseInteractor() {

    @Inject
    lateinit var launchCacheDateSource: LaunchCacheDateSource

    @Inject
    lateinit var context: Context

    open fun hasForAuth(): Boolean {
        val data = launchCacheDateSource.get()
        return data?.onlyAuthZone ?: false
    }

    open fun hasForNotAuth(): Boolean {
        val data = launchCacheDateSource.get()
        return if (data != null) !data.onlyAuthZone else false
    }

    open fun getItem(): LaunchItem<*>? {
        return launchCacheDateSource.get()
    }

    open fun putItem(item: LaunchItem<*>?) {
        launchCacheDateSource.put(item)
    }

    open fun clear() {
        putItem(null)
    }

    open fun createShortcut(
        intent: Intent,
        id: String,
        @DrawableRes icon: Int?,
        @StringRes shortLabel: Int?,
        @StringRes longLabel: Int? = shortLabel
    ): ShortcutInfo? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            runCatching {
                intent.action = id
                val nfcShortcut = ShortcutInfo.Builder(context, id)
                shortLabel?.let { nfcShortcut.setShortLabel(context.getString(it)) }
                longLabel?.let { nfcShortcut.setLongLabel(context.getString(it)) }
                icon?.let { nfcShortcut.setIcon(Icon.createWithResource(context, it)) }
                return nfcShortcut.setIntent(intent).build()
            }
        }
        return null
    }

    open fun addShortcuts(shortcuts: List<ShortcutInfo>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Handler(Looper.getMainLooper()).post {
                runCatching {
                    val shortcutManager = getSystemService(context, ShortcutManager::class.java)
                    shortcutManager?.setDynamicShortcuts(shortcuts)
                }
            }
        }
    }

    open fun removeShortcut(ids: List<String?>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Handler(Looper.getMainLooper()).post {
                runCatching {
                    val shortcutManager = getSystemService(context, ShortcutManager::class.java)
                    shortcutManager?.removeDynamicShortcuts(ids)
                }
            }
        }
    }

    override fun doOnExit() {
        clear()
    }
}