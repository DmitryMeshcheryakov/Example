package com.android.example.core.exceptions

import java.lang.Exception

class LoginConfirmationException(message: String): Exception(message) {

    var passwordRegExp: String? = null
    var passwordHint: String? = null

    constructor(message: String, passwordHint: String?, passwordRegExp: String?): this(message) {
        this.passwordHint = passwordHint
        this.passwordRegExp = passwordRegExp
    }
}