package com.android.example.core.repository

interface LocalRepository<E> {

    fun get(vararg args: Any?) : E?
    fun put(entity: E?)
    fun update(entity: E?, vararg args: Any?) = put(entity)
    fun remove(vararg args: Any?) = clear()
    fun clear() = put(null)
}