package com.android.example.core.common.textwatchers

import android.text.Editable
import android.text.TextWatcher

class SymbolsTextWatcher(var predefinedSymbols: String?): TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        val correct = buildCorrectStr(s?.toString())
        if (correct != s?.toString()) {
            s?.replace(0, s.length,  correct)
        }
    }

    private fun buildCorrectStr(str: String?): String {
        return str?.filter { predefinedSymbols?.contains(it) ?: true }.orEmpty()
    }

    companion object {
        const val DIGITS = "0123456789"
        const val CYRILIC_LOW = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
        const val CYRILIC_UPR = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
        const val LATIN_LOW = "abcdefghijklmnopqrstuvwxyz"
        const val LATIN_UPR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        const val SPACE = " "
    }
}