package com.android.example.core.common.textwatchers

import android.text.Editable
import android.text.TextWatcher

class TrimTextWatcher: TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        if (!isInputCorrect(s.toString())) {
            s?.replace(0, s.length, buildCorrectString(s.toString()))
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    private fun isInputCorrect(str: String?): Boolean {
        return str?.startsWith(' ') != true
    }

    private fun buildCorrectString(str: String?): String {
        return str.orEmpty().trim()
    }
}