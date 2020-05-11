package com.android.example.core.repository

interface ApiRepository<Request, Response> {

    fun request(request: Request? = null): Response
}