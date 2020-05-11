package com.android.example.core.exceptions

import java.lang.Exception

class ResponseException(
    var code: Int,
    message: String,
    var description: String? = null
) : Exception(message) {
    constructor() : this(-1, "", "")
}