package com.android.example.core.exceptions

import java.lang.Exception

class ValidationException(
    message: String? = null,
    var field: String? = null
) : Exception(message)