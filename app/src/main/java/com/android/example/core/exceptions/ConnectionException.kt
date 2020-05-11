package com.android.example.core.exceptions

class ConnectionException(var code: Int,
                          message: String
) : Exception(message) {
    companion object {
        const val CODE_NO_INTERNET = 0
        const val CODE_CONNECTION = 1
        const val CODE_TIMEOUT = 2
        const val CODE_UNKNOWN = 3
    }
}